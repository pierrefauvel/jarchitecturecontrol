package org.jarco.code.external;

import org.jarco.collections.ImmutableCollection;
import org.jarco.collections.ImmutableList;
import org.jarco.collections.ImmutableNamedMap;


public interface IPackage extends ICodeElement, INamed{
	public String getName();
//	public String getLongName();
	public ImmutableNamedMap<IClass> getClasses();
	public ImmutableList<IProject> getProjects();
}
