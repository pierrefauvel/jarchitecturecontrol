/**
 * 
 */
package org.jarco.configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jarco.code.internal.maven.MavenRef;
import org.jarco.control.report.DependenciesGReport;
import org.jarco.control.report.itf.IDependenciesReport;
import org.jarco.swing.components.FM;
import org.jarco.swing.components.IExposableAsANode;
import org.jarco.xml.FromXmlFactory;
import org.jarco.xml.IPersistableAsXml;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
public class Configuration implements IExposableAsANode, IPersistableAsXml
  {
	@FM(kind=FM.kind.component)
	  public String prefix;
	@FM(kind=FM.kind.treenode)
	  public List<MavenRef> mr=new ArrayList<MavenRef>();
	@FM(kind=FM.kind.component)
	  public String repopath;
	@FM(kind=FM.kind.component)
	  public String jdkPath;
	@FM(kind=FM.kind.ignore)
//	  private FileWriter fw;
	private IDependenciesReport dr;
	  
	public Configuration()
	{
	}
	  
	public String getReportPrefix() {
		return prefix;
	}

	public MavenRef[] getMavenComponentReferences() {
		return mr.toArray(new MavenRef[]{});
	}

	public String getMavenRepositoryPath() {
		return repopath;
	}

	public String getJDKPath() {
		return jdkPath;
	}

	public IDependenciesReport getDependenciesReport() throws IOException {
/*		if(fw==null)
		{
			File f = new File("reports/"+getReportPrefix());
			f.mkdirs();
			fw=new FileWriter("reports/"+getReportPrefix()+".ja2dependencies");	
		}
		return new DependenciesGReport(fw);*/
		if(dr==null)
		{
			File dir = new File("reports");
			dir.mkdirs();
			dr = new DependenciesGReport(dir,getReportPrefix());
		}
		return dr;
	}

	public void add(MavenRef uoFils) {
		mr.add(uoFils);
	}

	public void remove(MavenRef uoFils) {
		mr.remove(uoFils);
	}
	  public void fromXml(FromXmlFactory f, Element e)
//	  public static Configuration fromXml(FromXmlFactory f, Element e)
	  {
//		  Configuration cfg=new Configuration();
		  this.prefix = e.getAttribute("prefix");
		  this.repopath=e.getAttribute("repopath");
		  this.jdkPath=e.getAttribute("jdkPath");
		  NodeList nl = e.getElementsByTagName("maven-ref");
		  for(int i=0;i<nl.getLength();i++)
		  {
			  Element ei = (Element)(nl.item(i));
			  this.add((MavenRef) f.fromXml(ei));
		  }
	  }
	  public String toXml()
	  {
			StringBuffer sb=new StringBuffer();
			sb.append("<configuration ");
			sb.append("prefix=\""+prefix+"\" ");
			sb.append("repopath=\""+repopath+"\" ");
			sb.append("jdkPath=\""+jdkPath+"\" ");
			sb.append(">");
			for(MavenRef mri:mr)
			{
				sb.append(mri.toXml());
			}
			sb.append("</configuration>");
			return sb.toString();
	  }

	@Override
	public String toLabel() {
		return "<html><b>Configuration: </b><b>prefix=</b>"+prefix+" <b>repo-path=</b>"+repopath+" <b>jdkPath=</b>"+jdkPath+"</html>";
	}
	  
  }