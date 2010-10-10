package org.jarco.code.internal;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.xpath.XPathExpressionException;

import org.jarco.code.external.IClass;
import org.jarco.code.external.ICodeRepository;
import org.jarco.code.external.IPackage;
import org.jarco.code.external.IProject;
import org.jarco.code.external.IPropertiesDocument;
import org.jarco.code.external.IRepositorySPI;
import org.jarco.code.external.IRepositorySPIRef;
import org.jarco.code.external.IXmlDocument;
import org.jarco.code.internal.maven.MavenRef;
import org.jarco.code.internal.system.SystemProject;
import org.jarco.collections.ImmutableList;
import org.jarco.collections.ImmutableNamedCollection;
import org.jarco.collections.ImmutableNamedList;
import org.jarco.collections.ImmutableNamedMap;
import org.jarco.collections.ImmutableNamedSet;
import org.jarco.collections.ImmutableSet;
import org.jarco.collections.MapOfSet;
import org.jarco.control.report.DependenciesReport;
import org.jarco.tags.external.ITag;
import org.xml.sax.SAXException;


public class CodeRepositoryInternal implements ICodeRepository {
	
	private List<IProject> _projects=new ArrayList<IProject>();
	private MapOfSet<String,IProject> map_pn2project=new MapOfSet<String,IProject>();
	private Map<String,IPackage> map_pn2package=new HashMap<String,IPackage>();
	private AuxilliaryClassLoader cl;
	private Map<String,ProjectInternal> map_component2project=new HashMap<String,ProjectInternal>();
//	private String repoPath;
	private DependenciesReport rep_dependencies;
	private Map<String,IPackage> map_packageByName=new HashMap<String,IPackage>();
	
	private SystemProject systemProject;
	private List<IPropertiesDocument> lst_propertiesDocuments=new ArrayList<IPropertiesDocument>();
	private List<IXmlDocument> lst_xmlDocuments= new ArrayList<IXmlDocument>();
	
	private IClass anyClass;
	
	private IRepositorySPI repoSPI;

	IClass getAnyClass()
	{
		return anyClass;
	}
	
	public String toString()
	{
		return "Repository "+getName();
	}
	
	public String getName()
	{
		return repoSPI.getName();
	}
	
	public CodeRepositoryInternal(DependenciesReport rep, String jdkPath, IRepositorySPI repoSPI) throws IOException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, XPathExpressionException, SAXException
	{
		rep_dependencies=rep;
		cl = new AuxilliaryClassLoader(this, this.getClass().getClassLoader());
//		this.repoPath=repoPath;
		this.repoSPI = repoSPI;

		systemProject = new SystemProject(this);
		_projects.add(systemProject);
		
		ImmutableNamedSet<IPackage> sysPkg = systemProject.getPackages();
		List<IProject> lstProjects = new ArrayList<IProject>();
		lstProjects.add(systemProject);
		
		String rt_jar_path = jdkPath+File.separator+"jre"+File.separator+"lib"+File.separator+"rt.jar";
		JarFile jf = new JarFile(rt_jar_path);
		Enumeration<JarEntry> en = jf.entries();
		while(en.hasMoreElements())
		{
			JarEntry je = en.nextElement();
			String jen = je.getName();
			if(jen.endsWith(".class"))
			{
				String cn = jen;
				if(cn.indexOf("/")==-1)
				{
					System.err.println("PF21 Could not determine package for "+cn);
					continue;
				}
				String pn = cn.substring(0,cn.lastIndexOf("/")).replace("/", ".");
				PackageInternal pkg=createOrGetPackageByName(systemProject,pn);
				cn=cn.replace("/", ".");
				cn=cn.substring(0,cn.length()-".class".length());
//				System.out.println("PF64 Resolving parent for "+cn);
				String parent = NameUtilities.resolveParent(cn);
				ClassInternal c=new ClassInternal(this,systemProject,pkg,cn,NameUtilities.classShortNameFromClassLongName(cn),rt_jar_path,jen,parent);
				systemProject.addClass(c);
//				System.out.println("PF10 Adding system class "+c+ " to package "+pkg);
				pkg.addClass(c);
			}
		}		
		for(IPackage pkg:sysPkg)
		{
//			System.out.println("PIF05 Defining package "+pkg.getName());
			map_pn2project.putAll(pkg.getName(), lstProjects);
			map_pn2package.put(pkg.getName(), pkg);
		}

		
//		load(new MavenRef("bcel","bcel","5.1","jar"),false,"");
//		for(MavenRef mv : mavenComponents)
//		{
//			load(mv,true,"");
//		}
		
		for(IRepositorySPIRef cmp : repoSPI.getCanonicalComponents())
		{
			load(cmp,false,"");
		}
		for(IRepositorySPIRef cmp : repoSPI.getComponents())
		{
			load(cmp,true,"");
		}
		
		PackageInternal nopkg = createOrGetPackageByName(systemProject,"");
		anyClass = new AnyClass(this,nopkg);
		nopkg.addClass(anyClass);
	}
	
	public void flush()
	{
		rep_dependencies.close();
	}
	
	public ImmutableNamedMap<IPackage> getPackages()
	{
		return new ImmutableNamedMap(map_pn2package);
	}
	
	//TODO V2 A reprendre lors de l'externalisation de maven
	public IProject findProject(IRepositorySPIRef mr, boolean includeInScope) throws XPathExpressionException, IOException, SAXException
	{
		return load(mr,includeInScope,"");
	}
	
	private IProject load(IRepositorySPIRef mr,boolean includeInScope, String indent) throws IOException, SAXException, XPathExpressionException
	{
		//TODO V2 Gérer les inclusions multiples
		ProjectInternal project=null;
		try
		{
//			String key = mr.group+"/"+mr.component;
			String key = mr.getCompositeId();
			if(map_component2project.containsKey(key))
			{
//				MavenRef foundRef = map_component2project.get(key).getMavenRef();
//				if(mr.version.compareTo(foundRef.version)!=0)
				IRepositorySPIRef foundRef = map_component2project.get(key).getRef();
				if(mr.getVersion().compareTo(foundRef.getVersion())!=0)
				{
					//TODO V2 Gérer le rechargement des projets si la version est plus récente ??? Faire 2 passes, une pour calculer les versions des composants, la seconde pour charger ?
					System.out.println("PF76 Multiple use of project "+mr+", used cached instead: "+foundRef);
				}
				rep_dependencies.found(indent,mr,foundRef.getVersion());
				return map_component2project.get(key);
			}
			
			project = new ProjectInternal(this,mr);
			_projects.add(project);
			map_component2project.put(key,project);
	
			for(IClass c :  project.getClasses())
			{
				String pn = c.getPackage().getName();
				if(map_pn2project.containsKey(pn))
				{
//					System.out.println("Multiple package "+pn+" definition "+project.getName());
					map_pn2project.get(pn).add(project);
					((PackageInternal)(map_pn2package.get(pn))).addProject(project);
				}
				else
				{
//					List<IProject> lst=new ArrayList<IProject>();
//					lst.add(project);
					map_pn2project.put(pn,project);
					map_pn2package.put(pn,createOrGetPackageByName(project,pn));
				};
			}
			for(IRepositorySPIRef mri : project.getRef().getDependencies())
			{
				load(mri,false,indent+"\t");
			}
//			System.out.println("Found "+project.getClasses().count()+" classes in "+project.getName());
			rep_dependencies.loaded(indent,mr,mr.getVersion());
			
			return project;
		}
		catch(Throwable t)
		{
			System.err.println("PF18 Could not load "+mr+":"+t.getClass()+":"+t.getMessage());
			t.printStackTrace(System.out);
			return null;
		}
		finally
		{
			if(project!=null)
				project.flush();
		}
	}
	ImmutableSet<IProject> resolveProjectForPackage(String name) {
		if(!map_pn2project.containsKey(name))
			return null;
		return new ImmutableSet<IProject>(map_pn2project.get(name));
	}
	
	public ImmutableList<IProject> getProjects() {
		return new ImmutableList<IProject>(_projects);
	}
	 ClassLoader getMavenClassLoader() {
		return cl;
	}

	 PackageInternal createOrGetPackageByName(IProject project,String pn) {
			PackageInternal pi = (PackageInternal)map_packageByName.get(pn);
			if(pi==null)
			{
				pi=new PackageInternal(this,project,pn);
				systemProject.addPackage(pi);
				map_packageByName.put(pn,pi);
			};
			return pi;
	}

	@Override
	public IClass getClassByName(String name) {
		String pn = NameUtilities.packageFromClassLongName(name);
		IPackage pkg = getPackages().getByName(pn);
		if(pkg==null)
			throw new RuntimeException("PF18 Could not find pkg "+pn+ " for "+name);
		IClass ft = pkg.getClasses().getByName(NameUtilities.classShortNameFromClassLongName(name));
		if(ft==null)
			throw new RuntimeException("PF01 Class missing for "+name+" in package "+pkg.getName());
		return ft;
	}

	public SystemProject getSystemProject() {
		return systemProject; 
	}

	void add(IPropertiesDocument doc)
	{
		lst_propertiesDocuments.add(doc);
	}
	
	public ImmutableList<IPropertiesDocument> getPropertiesDocument() {
		return new ImmutableList<IPropertiesDocument>(lst_propertiesDocuments);
	}

	void add(IXmlDocument doc)
	{
		lst_xmlDocuments.add(doc);
	}

	public ImmutableList<IXmlDocument> getXmlDocuments() {
		return new ImmutableList<IXmlDocument>(lst_xmlDocuments);
	}

	public void addTag(ITag t) {
		throw new UnsupportedOperationException("Could not add tag "+t+" to a repository");
	}

	public ImmutableNamedSet<ITag> getTags() {
		throw new UnsupportedOperationException("Could not get tags of a repository");
	}

	public ICodeRepository getRepository() {
		return this;
	}
	
	private MapOfSet<String,IClass> mos=new MapOfSet<String,IClass>();
	
	 void registerInnerClass(String parent, ClassInternal classImpl)
	{
		mos.put(parent, classImpl);
	}
	 ImmutableSet<IClass> getInnerClasses(IClass parent)
	{
		return new ImmutableSet<IClass>(mos.get(parent.getLongName()));
	}

}
