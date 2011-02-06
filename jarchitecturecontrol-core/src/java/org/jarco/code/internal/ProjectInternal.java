package org.jarco.code.internal;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jarco.code.external.IClass;
import org.jarco.code.external.ICodeRepository;
import org.jarco.code.external.IPackage;
import org.jarco.code.external.IProject;
import org.jarco.code.external.IPropertiesDocument;
import org.jarco.code.external.IRepositorySPIRef;
import org.jarco.code.external.IXmlDocument;
import org.jarco.code.internal.maven.MavenRef;
import org.jarco.collections.ImmutableList;
import org.jarco.collections.ImmutableMap;
import org.jarco.collections.ImmutableNamedCollection;
import org.jarco.collections.ImmutableNamedList;
import org.jarco.collections.ImmutableNamedSet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.xpath.*;


public class ProjectInternal extends ACodeElementInternal implements IProject {

	private IRepositorySPIRef ref;
	
	private File f;
//	private PrintWriter pw;
 	
//	private Map<String,String> map_packageName2jarEntryName=new HashMap<String,String>();
	private List<IClass> lst_classes=new ArrayList<IClass>();
	private JarFile jf ;
	
	private IProject parentProject;

	private Set<IPackage> hs_packages = new HashSet<IPackage>();
	private List<IPropertiesDocument> lst_propertiesDocuments=new ArrayList<IPropertiesDocument>();
	private List<IXmlDocument> lst_xmlDocuments= new ArrayList<IXmlDocument>();
	
	//private Document pom;

//	private static XPathFactory xpf;
//	private static XPath xp;
//	private static XPathExpression xp_dependencies;
	
	private static DocumentBuilder db;
	static
	{
		try
		{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		db=dbf.newDocumentBuilder();

//		xpf = XPathFactory.newInstance();
//		xp = xpf.newXPath();
//		xp_dependencies = xp.compile("/project/dependencies/dependency");
		
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
	}
	ProjectInternal(CodeRepositoryInternal repo, IRepositorySPIRef ref) throws IOException, SAXException
	{
		super(repo);
		f = new File("log/"+ref.toString()+".log");
//		pw = new PrintWriter(new FileWriter(f));
		this.ref=ref;
//		this.repoPath=repoPath;
		
		

//		MavenRef mr_parent = getParentProject();
//		if(mr_parent!=null)
//		{
//			System.out.println("PF26 Parent "+mr_parent+" found for "+ref);
//		}

	//	System.out.println("Loading "+archiveFileName);
//		if(archiveFileName!=null)
		
//		System.out.println("PF77 "+getName()+" isConcrete="+ref.isConcrete());
		
		if(ref.isConcrete())
		{
		jf=new JarFile(new File(ref.getArchiveFileName()));
		Enumeration<JarEntry> en = jf.entries();
		
		while(en.hasMoreElements())
		{
			JarEntry je =en.nextElement();
			String jen = je.getName();
			if(jen.endsWith(".class"))
			{
				String cn = jen;
				if(ref.getExtension().compareTo("war")==0)
				{
					if(cn.startsWith("WEB-INF/classes/"))
						cn=cn.substring("WEB-INF/classes/".length());
				}
				String pn=null;
				if(cn.indexOf("/")==-1)
				{
//					System.err.println("PF20 Could not determine package for "+cn);
					pn="";
				}
				else 
					pn = cn.substring(0,cn.lastIndexOf("/")).replace("/", ".");
				PackageInternal pkg=repo.createOrGetPackageByName(this,pn);
				hs_packages.add(pkg);
				cn=cn.replace("/", ".");
				cn=cn.substring(0,cn.length()-".class".length());
				String parent = NameUtilities.resolveParent(cn);
				ClassInternal c=new ClassInternal(repo,this,pkg,cn,cn.substring(pn.length()+1),ref.getArchiveFileName(),jen,parent);
				lst_classes.add(c);
				pkg.addClass(c);
	//			setJarEntryNameFromClass(pn,jen);
			}
			else if(jen.endsWith(".xml"))
			{
				XmlDocumentInternal xdi = new XmlDocumentInternal(getRepository(),this,ref.getArchiveFileName(),jen);
				add(xdi);
				repo.add(xdi);
			}
			else if(jen.endsWith(".properties"))
			{
				PropertiesDocumentInternal pdi = new PropertiesDocumentInternal(getRepository(),this,ref.getArchiveFileName(),jen);
				add(pdi);
				repo.add(pdi);
			}
		}
		}
	};

	public ImmutableNamedSet<IPackage> getPackages()
	{
//		System.out.println("PF57 "+lst_packages);
		return new ImmutableNamedSet<IPackage>(hs_packages);
	}
	
//	public void setJarEntryNameFromClass(String pn, String jen) {
//		map_packageName2jarEntryName.put(pn,jen);
//	}
//	
	public ImmutableList<IClass> getClasses() {
		return new ImmutableList<IClass>(lst_classes);
	}
	public String getName()
	{
		return ref.getName();
	}
	
	public String toLabel()
	{
		return "<b>"+ref.getName()+"</b> "+ref.getVersion();
	}
	
	public byte[] loadClassByteCode(String className,String packageName) throws IOException {

		String jarEntryName=null;
		if(ref.getExtension().compareTo("war")==0)
			jarEntryName = "WEB-INF/classes/"+className.replace(".","/");
		else
			jarEntryName = className.replace(".","/");
		jarEntryName+=".class";
		
		JarEntry je = jf.getJarEntry(jarEntryName);
		
		//System.out.println("Reading "+jarEntryName+ " in "+archiveName);
		
		if(je==null)
		{
//			System.out.println("Could not read jarEntry "+jarEntryName+" in "+archiveName);
			throw new IOException("Could not read jarEntry "+jarEntryName+" in "+ref.getName());
		};
		
		InputStream is=jf.getInputStream(je);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int LENGTH = 1024;
		byte[] b=new byte[LENGTH];
		int r=0;
		while ( (r=is.read(b,0,LENGTH))!=-1)
		{
			baos.write(b,0,r);
		};
		return baos.toByteArray();
	}    
    
    	
	public void flush()
	{
//		pw.close();
//		if(f.length()==0)
//			f.delete();
//		pw=null;
//		f=null;
	}
	public IRepositorySPIRef getRef(){
		return ref;
	}
	
	public String toString()
	{
		return "Project "+ref.getComponent();
	}
	
//	private Document getPom() {
//		return pom;
//	}
	void add(IPropertiesDocument doc)
	{
		lst_propertiesDocuments.add(doc);
	}
	
	public ImmutableList<IPropertiesDocument> getPropertiesDocuments() {
		return new ImmutableList<IPropertiesDocument>(lst_propertiesDocuments);
	}

	void add(IXmlDocument doc)
	{
		lst_xmlDocuments.add(doc);
	}

	public ImmutableList<IXmlDocument> getXmlDocuments() {
		return new ImmutableList<IXmlDocument>(lst_xmlDocuments);
	}

    public IProject getParentProject() 
    {
    	if(parentProject==null)
    	{
    		try
    		{
    			IRepositorySPIRef parentRef = ref.getParentRef();
     		}
    		catch(Throwable t)
    		{
    			t.printStackTrace();
    			return null;
    		}
    	}
    	return parentProject;
    }
}
