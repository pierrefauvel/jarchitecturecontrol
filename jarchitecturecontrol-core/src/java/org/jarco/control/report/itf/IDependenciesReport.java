package org.jarco.control.report.itf;

import java.io.File;

import org.jarco.code.external.IRepositorySPIRef;
import org.jarco.code.internal.maven.MavenRef;
import org.jarco.control.report.genericreport.GenericReport;

public interface IDependenciesReport {

	public abstract void close();

	public abstract void push();

	public abstract void pop();

	public abstract void found(IRepositorySPIRef mr, String version);

	public abstract void loaded(IRepositorySPIRef mr, String version);

	public abstract void skippingBecauseNoComponent(File f);

	public abstract void couldNotFindVersion(File dir);

	public abstract void couldNotFindSubDir(File dir);

	public abstract void couldNotFindFile(String pomFileName);

	public abstract void couldNotFindArchives(File versionFile);

	public abstract void couldNotFindDirectory(File versionFile);

	public abstract void couldNotResolveGroup(MavenRef mr);

	public abstract void noDependencyFoundIn(String name);

	public abstract GenericReport getReport();

	public abstract void couldNotFindAnyVersionOfDependency(MavenRef mr,
			String pomFileName);

	public abstract void multipleVersionOfProject(IRepositorySPIRef mr,
			IRepositorySPIRef foundRef);

	
}
