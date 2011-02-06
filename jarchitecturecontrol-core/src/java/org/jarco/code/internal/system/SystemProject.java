package org.jarco.code.internal.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jarco.code.external.IClass;
import org.jarco.code.external.ICodeRepository;
import org.jarco.code.external.IPackage;
import org.jarco.code.external.IProject;
import org.jarco.code.external.IPropertiesDocument;
import org.jarco.code.external.IRepositorySPIRef;
import org.jarco.code.external.IXmlDocument;
import org.jarco.code.internal.ACodeElementInternal;
import org.jarco.code.internal.ClassInternal;
import org.jarco.code.internal.PackageInternal;
import org.jarco.collections.ImmutableList;
import org.jarco.collections.ImmutableNamedCollection;
import org.jarco.collections.ImmutableNamedList;
import org.jarco.collections.ImmutableNamedSet;
import org.jarco.collections.ImmutableSet;

public class SystemProject extends ACodeElementInternal implements IProject {

	private Set<IPackage> pkgs;
	private List<IClass> classes;
	
	public SystemProject(ICodeRepository repo)
	{
		super(repo);
		Package[] packages = Package.getPackages();
		pkgs = new HashSet<IPackage>();
		classes = new ArrayList<IClass>();
//		for(Package p:packages)
//		{
//			pkgs.add(new PackageImpl(SystemProject.instance(),p.getName()));
//		}
	}
	
	public ImmutableList<IClass> getClasses() {
		return new ImmutableList<IClass>(classes);
	}

	public String getName() {
		return "System project";
	}

	public ImmutableNamedSet<IPackage> getPackages() {
		return new ImmutableNamedSet<IPackage>(pkgs);
	}

	public byte[] loadClassByteCode(String name, String pn) throws IOException {
		throw new RuntimeException("System project implementation should not be asked for bytecode name="+name+", pn="+pn);
	}
	
	public String toString()
	{
		return "Project System";
	}
	
	public String toLabel()
	{
		return "<b>System</b>";
	}

	public void addPackage(PackageInternal pkg) {
		pkgs.add(pkg);
	}
	public void addClass(ClassInternal cls){
		classes.add(cls);
	}

	public ImmutableList<IPropertiesDocument> getPropertiesDocuments() {
		return null;
	}

	public ImmutableList<IXmlDocument> getXmlDocuments() {
		return null;
	}

	public IProject getParentProject() {
		return null;
	}

	public String resolveProperty(String propertyName) {
		return null;
	}

	public IRepositorySPIRef getRef() {
		return null;
	}


}
