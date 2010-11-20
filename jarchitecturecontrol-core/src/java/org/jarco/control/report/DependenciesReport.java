package org.jarco.control.report;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.jarco.code.external.IRepositorySPIRef;
import org.jarco.code.internal.maven.MavenRef;

public class DependenciesReport {

	private PrintWriter pw;
	
	public DependenciesReport(FileWriter fileWriter) {
		pw=new PrintWriter(fileWriter);
		
		MavenRef.registerDependenciesReport(this);
	}

	public void close() {
		pw.close();
	}

	public void found(String indent, IRepositorySPIRef mr, String version) {
		pw.println(indent+mr+":Found as "+version);
	}

	public void loaded(String indent, IRepositorySPIRef mr, String version) {
		pw.println(indent+mr+":Loaded as "+mr.getVersion());
	}

	public void skippingBecauseNoComponent(File f) {
		pw.println("Skipping dir because no component found in "+f.getAbsolutePath());
	}

	public void couldNotFindVersion(File dir) {
		pw.println("Could not find version in "+dir.getAbsolutePath());
	}

	public void couldNotFindSubDir(File dir) {
		pw.println("Could not find subdir "+dir.getAbsolutePath());
	}

	public void couldNotFindFile(String pomFileName) {
		pw.println("Could not find file "+pomFileName);
	}

	public void couldNotFindArchives(File versionFile) {
		pw.println("Could not find archives "+versionFile.getAbsolutePath());
	}

	public void couldNotFindDirectory(File versionFile) {
		pw.println("Could not find directory "+versionFile.getAbsolutePath());
	}

	public void couldNotResolveGroup(MavenRef mr) {
		pw.println("Could not resolve group "+mr);
	}

	public void noDependencyFoundIn(String name) {
		pw.println("No dependency found in "+name);
	}

}
