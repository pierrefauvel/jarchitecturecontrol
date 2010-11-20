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
import org.jarco.control.report.DependenciesReport;
import org.jarco.control.specifications.model.FM;
import org.jarco.swing.tree.IExposableAsANode;
import org.jarco.xml.FromXmlFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
public class Configuration implements IExposableAsANode
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
	  private FileWriter fw;
	  
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

	public DependenciesReport getDependenciesReport() throws IOException {
		if(fw==null)
		{
			File f = new File("reports/"+getReportPrefix());
			f.mkdirs();
			fw=new FileWriter("reports/"+getReportPrefix()+".ja2dependencies");	
		}
		return new DependenciesReport(fw);
	}

	public void add(MavenRef uoFils) {
		mr.add(uoFils);
	}

	public void remove(MavenRef uoFils) {
		mr.remove(uoFils);
	}
	  public static Configuration fromXml(FromXmlFactory f, Element e)
	  {
		  Configuration cfg=new Configuration();
		  cfg.prefix = e.getAttribute("prefix");
		  cfg.repopath=e.getAttribute("repopath");
		  cfg.jdkPath=e.getAttribute("jdkPath");
		  NodeList nl = e.getElementsByTagName("maven-ref");
		  for(int i=0;i<nl.getLength();i++)
		  {
			  Element ei = (Element)(nl.item(i));
			  cfg.add((MavenRef) f.fromXml(ei));
		  }
		  return cfg;
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
		return toString();
	}
	  
  }