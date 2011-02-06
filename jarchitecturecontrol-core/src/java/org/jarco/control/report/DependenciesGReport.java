package org.jarco.control.report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.transform.TransformerException;

import org.jarco.code.external.IRepositorySPIRef;
import org.jarco.code.internal.maven.MavenRef;
import org.jarco.control.report.filesystem.Indent;
import org.jarco.control.report.genericreport.FormattedText;
import org.jarco.control.report.genericreport.GenericReport;
import org.jarco.control.report.itf.IDependenciesReport;

public class DependenciesGReport implements IDependenciesReport {

//	private PrintWriter pw;
//	private int indent;
	private File r;
	private GenericReport gr;
	
	public static final String KIND_DEPENDENCIES_SET = "dependencies-set" ;
	public static final String KIND_OK_MESSAGE = "ok-message";
	public static final String KIND_KO_MESSAGE = "ko-message";
	public static final String KIND_WARNING_MESSAGE = "warning-message";
	
	public DependenciesGReport(File dirpath, String name) throws IOException {
//		pw=new PrintWriter(fileWriter);
		
		MavenRef.registerDependenciesReport(this);
		r = new File(dirpath.getCanonicalPath()+File.separator+name+".dependencies.ja2.xml");		
		gr = new GenericReport(KIND_DEPENDENCIES_SET,new FormattedText("<b>Dependencies</b>"),null);
	}

	public GenericReport getReport()
	{
		return gr;
	}
	
	public void close() {
//		pw.close();
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(r);
			gr.write(fos);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void push()
	{
		gr.push(KIND_DEPENDENCIES_SET, new FormattedText("<b>Dependencies</b>"), null);
	}
	
	public void pop()
	{
		gr.pop();
	}
	
	
	public void found(IRepositorySPIRef mr, String version) {
		gr.push(KIND_OK_MESSAGE,new FormattedText(mr.toLabel()), new FormattedText("<b>Found as:</b>"+version));
		gr.pop();
	}

	public void loaded(IRepositorySPIRef mr, String version) {
		gr.push(KIND_OK_MESSAGE,new FormattedText(mr.toLabel()), new FormattedText("<b>Loaded as:</b>"+version));
		gr.pop();
	}

	public void skippingBecauseNoComponent(File f) {
		gr.push(KIND_WARNING_MESSAGE,new FormattedText(f.getAbsolutePath()),new FormattedText("<b>Skipping dir<b> because no component found"));
		gr.pop();
	}

	public void couldNotFindVersion(File dir) {
		gr.push(KIND_KO_MESSAGE,new FormattedText(dir.getAbsolutePath()), new FormattedText("<b>Could not find version</b> in dir"));
		gr.pop();
	}

	public void couldNotFindSubDir(File dir) {
		gr.push(KIND_KO_MESSAGE,new FormattedText(dir.getAbsolutePath()), new FormattedText("<b>Could not find sub dir</b>"));
		gr.pop();
	}

	public void couldNotFindFile(String pomFileName) {
		gr.push(KIND_KO_MESSAGE,new FormattedText(pomFileName), new FormattedText("<b>Could not find file</b>"));
		gr.pop();
	}

	public void couldNotFindArchives(File versionFile) {
		gr.push(KIND_KO_MESSAGE,new FormattedText(versionFile.getAbsolutePath()), new FormattedText("<b>Could not find archive</b>"));
		gr.pop();
	}

	public void couldNotFindDirectory(File versionFile) {
		gr.push(KIND_KO_MESSAGE,new FormattedText(versionFile.getAbsolutePath()), new FormattedText("<b>Could not find directory</b>"));
		gr.pop();
	}

	public void couldNotResolveGroup(MavenRef mr) {
		gr.push(KIND_KO_MESSAGE,new FormattedText(mr.toLabel()), new FormattedText("<b>Could not resolve group</b>"));
		gr.pop();
	}

	public void noDependencyFoundIn(String name) {
		gr.push(KIND_WARNING_MESSAGE,new FormattedText(name), new FormattedText("<b>Could not find dependencies</b>"));
		gr.pop();
	}

	public void couldNotFindAnyVersionOfDependency(MavenRef mr,
			String pomFileName) {
//		System.err.println("PF23 Could not find any version for "+mr+" (dependency of "+pomFileName+")");
		gr.push(KIND_KO_MESSAGE, new FormattedText(mr.toLabel()), new FormattedText("Dependency of "+pomFileName));
		gr.pop();
	}

	@Override
	public void multipleVersionOfProject(IRepositorySPIRef mr,
			IRepositorySPIRef foundRef) {
		//					System.out.println("PF76 Multiple use of project "+mr+", used cached instead: "+foundRef);
		gr.push(KIND_WARNING_MESSAGE,
				new FormattedText("Multiple version of project "+mr.getCompositeId()), 
				new FormattedText("<b>New version:</b>"+mr.toLabel()+"<br/><b>Use instead (from cache):</b>"+foundRef));
		gr.pop();
	}
	
}
