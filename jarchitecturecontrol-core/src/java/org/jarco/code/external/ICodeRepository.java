package org.jarco.code.external;

import org.jarco.code.internal.ClassInternal;
import org.jarco.code.internal.system.SystemProject;
import org.jarco.collections.ImmutableList;
import org.jarco.collections.ImmutableNamedMap;

public interface ICodeRepository extends ICodeElement{
	public ImmutableList<IProject> getProjects();
	public ImmutableNamedMap<IPackage> getPackages();
	public IClass getClassByName(String name);
	public ImmutableList<IXmlDocument> getXmlDocuments();
	public ImmutableList<IPropertiesDocument> getPropertiesDocument();
	public SystemProject getSystemProject();
	public void flush();

}
