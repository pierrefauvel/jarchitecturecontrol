package org.jarco.code.internal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jarco.code.external.IClass;
import org.jarco.code.external.ICodeRepository;
import org.jarco.code.external.IPackage;
import org.jarco.code.external.IProject;
import org.jarco.code.external.IPropertiesDocument;
import org.jarco.code.external.IXmlDocument;
import org.jarco.collections.ImmutableCollection;
import org.jarco.collections.ImmutableList;
import org.jarco.collections.ImmutableNamedMap;

public class PackageInternal extends ACodeElementInternal implements IPackage {

	private String ln;
	private List<IProject> projects;
	private Set<IClass> classes;
	
	public PackageInternal(ICodeRepository repo, IProject project,String ln)
	{
		super(repo);
		this.projects=new ArrayList<IProject>();
		projects.add(project);
		this.ln=ln;
		this.classes=new HashSet<IClass>();
	}
	
	public String getName() {
		return ln;
	}

	public ImmutableNamedMap<IClass> getClasses() {
		return new ImmutableNamedMap<IClass>(classes);
	}

	public ImmutableList<IProject> getProjects() {
		return new ImmutableList<IProject>(projects);
	}
	
	void addClass(IClass c) {
//		System.out.println("PIF03 Registering class "+c.getName()+" in pkg "+ln);
		classes.add(c);
	}
	
	public String toString()
	{
		return "Package "+ln;
	}

	public void addProject(ProjectInternal project2) {
		projects.add(project2);
	}
	
	public int hashCode()
	{
		return ln.hashCode();
	}
	public boolean equals(Object obj)
	{
		PackageInternal pi = (PackageInternal)obj;
		return ln.compareTo(pi.ln)==0;
	}

}
