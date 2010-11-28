/**
 * 
 */
package org.jarco.code.internal.maven;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathExpressionException;

import org.jarco.code.external.IProject;
import org.jarco.code.external.IRepositorySPIRef;
import org.jarco.code.internal.CodeRepositoryInternal;
import org.jarco.collections.ImmutableList;
import org.jarco.collections.ImmutableMap;
import org.jarco.configuration.ConfigurationSet;
import org.jarco.control.report.DependenciesReport;
import org.jarco.control.specifications.model.FM;
import org.jarco.persistence.FromXmlFactory;
import org.jarco.swing.tree.IExposableAsANode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MavenRef implements IRepositorySPIRef, IExposableAsANode
{
	
//	private DependenciesReport pw;
	private static DependenciesReport pw;
	
	public static void registerDependenciesReport(DependenciesReport _pw)
	{
		pw=_pw;
	}
	
//	public MavenRef(String repoPath,DependenciesReport pw)
	public MavenRef(String repoPath)
	{
//		this.pw=pw;
		this.repoPath = repoPath;
	}
	public MavenRef(String repoPath, String group, String component, String version,
			String extension) {
//		public MavenRef(String repoPath, DependenciesReport pw, String group, String component, String version,
//				String extension) {
//		this.pw=pw;
		this.group=group;
		this.component=component;
		this.version=version;
		this.extension=extension;
		this.repoPath = repoPath;
	}

	public String getPomFileName()
	{
		return repoPath+File.separator+
		group.replace(".",File.separator)+File.separator+
		component+File.separator+version+File.separator+
		component+"-"+version+".pom";
	}
	
//	public MavenRef() {
//	}
	@FM(kind=FM.kind.component)
	private String group;
	@FM(kind=FM.kind.component)
	private String component;
	@FM(kind=FM.kind.component)
	private String version;
	@FM(kind=FM.kind.component)
	private String extension;
	@FM(kind=FM.kind.component)
	private String repoPath;

	  public static MavenRef fromXml(FromXmlFactory f, Element e)
	  {
		  String repoPath = e.getAttribute("repo-path");
		  String group = e.getAttribute("group");
		  String component = e.getAttribute("component");
		  String version = e.getAttribute("version");
		  String extension = e.getAttribute("extension");
//		  MavenRef mr = new MavenRef(repoPath,pw,group,component,version,extension);
		  MavenRef mr = new MavenRef(repoPath,group,component,version,extension);
		  return mr;
	  }
	  public String toXml()
	  {
		StringBuffer sb=new StringBuffer();
		sb.append("<maven-ref ");
		sb.append("group=\""+group+"\" ");
		sb.append("component=\""+component+"\" ");
		sb.append("version=\""+version+"\" ");
		sb.append("extension=\""+extension+"\" ");
		sb.append("repo-path=\""+repoPath+"\" ");
		sb.append("/>");
		return sb.toString();
	  }
//	private String archiveFileName;
//	private String repoPath;
//	private String pomFileName;
	private Document pom;

	public String getArchiveFileName()
	{
		//System.out.println("PF78 "+component+" "+extension);
		if(extension.compareTo("pom")!=0)
		{
		return repoPath+File.separator+
		group.replace(".",File.separator)+File.separator+
		component+File.separator+version+File.separator+
		component+"-"+version+"."+extension;
		};
		return null;
	}
	public String getName() {
		return component+"-"+version+"."+extension;	
	}
	public String getComponent()
	{
		return component;
	}
	public String getCompositeId()
	{
		return group+"/"+component;		
	}
	public String getVersion()
	{
		return version;
	}
	public String toString()
	{
		return group+"."+component+"(version="+version+",extension="+extension+")";
	}


	public ImmutableList<IRepositorySPIRef> getDependencies() throws XPathExpressionException, SAXException, IOException {
		//TODO V1.2 Gérer les exclusions
		analyzePom();
		Element project = pom.getDocumentElement();

		NodeList nl_dependencies = project.getElementsByTagName("dependencies");
		List<IRepositorySPIRef> lmr = new ArrayList<IRepositorySPIRef>();
		loop_dependencies:for (int j=0;j<nl_dependencies.getLength();j++)
		{
			Element dependencies = (Element)(nl_dependencies.item(j));
			
			if(dependencies.getParentNode().getNodeName().compareTo("project")!=0)
			{
				//on ne s'intéresse pas aux dependences des plugin
				continue loop_dependencies;
			}
			
			if(dependencies==null)
			{
//				pw.println("No dependency found in "+getName());
				pw.noDependencyFoundIn(getName());
				return new ImmutableList<IRepositorySPIRef>(new ArrayList<IRepositorySPIRef>());
			};

			NodeList nl = dependencies.getElementsByTagName("dependency");

//TODO V2 Revoir la sémantique exacte de Optional & Scope et ce qu'il convient de faire
			loop_dependency: for(int i=0;i<nl.getLength();i++)
			{
				Node n = nl.item(i);
				Element e = (Element)n;
				NodeList nlo = e.getElementsByTagName("optional");
				if(nlo.getLength()>0)
				{
					if(nlo.item(0).getTextContent().trim().compareTo("true")==0)
					{
						continue loop_dependency;
					}
				}
				NodeList nls = e.getElementsByTagName("scope");
				if(nls.getLength()>0)
				{
					if(nls.item(0).getTextContent().trim().compareTo("test")==0)
					{
						continue loop_dependency;
					}
//					if(nls.item(0).getTextContent().trim().compareTo("provided")==0)
//					{
//						continue loop_dependency;
//					}
				}
//				MavenRef mr = new MavenRef(repoPath,pw);
				MavenRef mr = new MavenRef(repoPath);
				mr.group=e.getElementsByTagName("groupId").item(0).getTextContent();
				if(mr.group.startsWith("${"))
				{
					if(mr.group.compareTo("${project.groupId}")==0 || mr.group.compareTo("${groupId}")==0)
					{
						looking_for_groupId:
						{
							NodeList nl_groupId = project.getElementsByTagName("groupId");
							loop_groups:for(int k=0;k<nl_groupId.getLength();k++)
							{
								Element e_groupId = (Element)(nl_groupId.item(k));
								if(e_groupId.getParentNode().getNodeName().compareTo("project")!=0)
									continue loop_groups;
								mr.group = e_groupId.getTextContent();
								break looking_for_groupId;
							}
							NodeList nl_parent = project.getElementsByTagName("parent");
							if(nl_parent.getLength()>0)
							{
								Element parent = (Element) nl_parent.item(0);
								Element groupId = (Element) parent.getElementsByTagName("groupId").item(0);
								mr.group = groupId.getTextContent();
								break looking_for_groupId;
							}
//							pw.println("PF26 Could not resolve group in "+mr);
							pw.couldNotResolveGroup(mr);
						};
						
					}
					else 
					{
						String pv = resolveProperty(mr.group.substring(2,mr.group.length()-1));
						if(pv==null)
						{
//							pw.println("PF27 Could not resolve group in "+mr);
							pw.couldNotResolveGroup(mr);
							continue;
						};
						mr.group=pv;
					}
				}
				mr.component=e.getElementsByTagName("artifactId").item(0).getTextContent();
//				pw.println("Looking for "+mr);
				//TODO V2 Implémenter un meilleur calcul de la version
				NodeList nl_version = e.getElementsByTagName("version");
				mr.version=nl_version.getLength()>0 ? nl_version.item(0).getTextContent() : null;
				if(mr.version!=null && mr.version.startsWith("${"))
				{
					if(mr.version.compareTo("${project.version}")==0)
					{
						Node version = project.getElementsByTagName("version").item(0);
						mr.version = version.getTextContent();
					}
					else if(mr.version.compareTo("${pom.version}")==0)
					{
						Node version = project.getElementsByTagName("version").item(0);
						mr.version = version.getTextContent();
					}
					else
					{
						String pv = resolveProperty(mr.version.substring(2,mr.version.length()-1));
						if(pv==null)
						{
							System.err.println("PF22 Could not resolve version "+mr.version+" in "+mr+" (dependency of "+this.getPomFileName()+")");
							continue;
						}
						mr.version=pv;
					};
				}
				if(mr.version!=null)
				{
					if(!checkVersionExists(mr))
					{
//						System.out.println("PF24 Version "+mr.version+" of "+mr+" is missing, loading most recent"+" (dependency of "+this.pomFileName+")");
						mr.version=null;
					};
				}
				if(mr.version==null)
					mr.version=getMostRecentVersion(mr);
				if(mr.version!=null)
				{
					//TODO V1.2 Récupérer le POM et lire l'extension
					mr.extension="jar"; // calculé en chargeant le pom de la cible project/packaging, facultatif
					lmr.add(mr);
				}
				else
				{
					System.err.println("PF23 Could not find any version for "+mr+" (dependency of "+this.getPomFileName()+")");
				}
			}
		}
		return new ImmutableList<IRepositorySPIRef>(lmr);
	}

	private boolean checkVersionExists(MavenRef mr) {
		String directoryName = repoPath+File.separator+
		mr.group.replace(".",File.separator)+File.separator+
		mr.component;
		File versionFile = new File(directoryName+File.separator+mr.version);
		if(!versionFile.exists())
		{
			pw.couldNotFindDirectory(versionFile);
			return false;
		}
		File[] archives = versionFile.listFiles(IProject.ARCHIVE_FILE_FILTER);
		if (archives==null || archives.length==0)
		{
			pw.couldNotFindArchives(versionFile);
			return false;
		}
		String pomFileName = repoPath+File.separator+
		mr.group.replace(".",File.separator)+File.separator+
		mr.component+File.separator+mr.version+File.separator+
		mr.component+"-"+mr.version+".pom";
		if(!new File(pomFileName).exists())
		{
			pw.couldNotFindFile(pomFileName);
			return false;
		}
		return true;
	}
	private String getMostRecentVersion(MavenRef mr) {
		String directoryName = repoPath+File.separator+
		mr.group.replace(".",File.separator)+File.separator+
		mr.component;
		File dir = new File(directoryName);
		//System.out.println("Looking in "+directoryName);
		File[] subdirs = dir.listFiles(new FileFilter(){
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
		TreeSet<File> ts = new TreeSet<File>(new Comparator<File>(){
			public int compare(File arg0, File arg1) {
				//TODO V2 Améliorer la comparaison des version
				return arg0.getName().compareTo(arg1.getName());
			}
		});
		if(subdirs!=null)
		{
			for(File f:subdirs)
			{
				if(hasComponent(f))
					ts.add(f);
			};
	//		System.out.println(ts);
//			pw.println("Infered version "+ts.last().getName()+" for "+mr);
			if(ts.size()==0)
			{
				pw.couldNotFindSubDir(dir);
				return null;
			};
			return ts.last().getName();
		}
		else
		{
			pw.couldNotFindVersion(dir);
			return null;
		}
	}
	private boolean hasComponent(File f)
	{
		File[] c = f.listFiles(IProject.ARCHIVE_FILE_FILTER);
		if(c.length==0)
		{
			pw.skippingBecauseNoComponent(f);
			return false;
		}
//		System.out.println("Found "+c.length+" components in "+f);
		return true;
	}
	
    private void analyzePom() throws SAXException, IOException
    {
		pom = db.parse(new File(getPomFileName()));
    }
	public boolean isConcrete()
	{
		return getArchiveFileName() != null;
	}
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

   
    public ImmutableMap<String,String> getProperties() throws SAXException, IOException
    {
    	analyzePom();
    	Element project = pom.getDocumentElement();
    	NodeList properties = project.getElementsByTagName("properties");
    	Map<String,String> rc=new HashMap<String,String>();
    	for(int i=0;i<properties.getLength();i++)
    	{
    		NodeList nl_property = ((Element)(properties.item(i))).getChildNodes();
    		for(int j=0;j<nl_property.getLength();j++)
    		{
    			if(nl_property instanceof Element)
    			{
		    		String pn = nl_property.item(j).getNodeName().trim();
		    		String pv = nl_property.item(j).getTextContent().trim();
		    		rc.put(pn, pv);
    			}
    		}
    	};
    	rc.put("version", version);
    	rc.put("groupId",group);
    	rc.put("project.groupId", group);
    	rc.put("artefactId",component);
    	rc.put("project.artefactId",component);
    	return new ImmutableMap<String,String>(rc);
    }

    public IRepositorySPIRef getParentRef() throws SAXException, IOException
    {
		analyzePom();
		Element e_project = pom.getDocumentElement();
		NodeList nl_parent = e_project.getElementsByTagName("parent");
		MavenRef mr=null;
		if(nl_parent.getLength()>0)
		{
			Element e_parent = (Element)(nl_parent.item(0));
			String groupId = e_parent.getElementsByTagName("groupId").item(0).getTextContent();
			String artifactId = e_parent.getElementsByTagName("artifactId").item(0).getTextContent();
			String version = e_parent.getElementsByTagName("version").item(0).getTextContent();
//			mr = new MavenRef(repoPath,pw,groupId,artifactId,version,"pom");
			mr = new MavenRef(repoPath,groupId,artifactId,version,"pom");
		}
		return mr;
    }

    public String resolveProperty(String name)
    {
    	try
    	{
    	ImmutableMap<String,String> props=getProperties();
//    	System.out.println("PF30 resolving "+name);
//    	System.out.println("PF30 current properties:"+props);
    	if(props.containsKey(name))
    	{
    		String rc=props.get(name);
//    		System.out.println("PIF30 Found "+rc+" for "+name);
    		return rc;
    	}
    	IRepositorySPIRef pi = getParentRef();
    	if(pi!=null)
    	{
//        	System.out.println("PF30 going to parent "+pi+" to find property "+name);
    		return pi.resolveProperty(name);
    	}
    	throw new RuntimeException("Could not resolve property "+name+" in project "+this);
    	}
    	catch(Throwable t)
    	{
    		t.printStackTrace();
    		return null;
    	}
    }
    public String getExtension()
    {
    	return extension;
    }

	@Override
	public String toLabel() {
		return toString();
	}
}