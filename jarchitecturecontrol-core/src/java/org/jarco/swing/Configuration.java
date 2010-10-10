/**
 * 
 */
package org.jarco.swing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jarco.code.internal.maven.MavenRef;
import org.jarco.control.report.DependenciesReport;
import org.jarco.control.specifications.model.FM;;
public class Configuration
  {
	@FM(kind=FM.kind.component)
	  public String prefix;
	@FM(kind=FM.kind.component)
	  public MavenRef[] mr;
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
		return mr;
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
	  
  }