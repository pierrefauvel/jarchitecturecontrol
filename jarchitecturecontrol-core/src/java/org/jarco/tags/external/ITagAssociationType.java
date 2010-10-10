package org.jarco.tags.external;

import org.jarco.code.external.INamed;
import org.jarco.collections.ImmutableList;
import org.jarco.collections.ImmutableNamedSet;
import org.jarco.collections.ImmutableSet;

public interface ITagAssociationType extends INamed{
	public String getName();
	public ImmutableNamedSet<ITagRoleType> getRoles();
	public ITagType getTypeOfRole(ITagRoleType role);
	public ImmutableNamedSet<ITagAssociation> getInstances();
	public ITagAssociation newInstance(String name);
	public ITagAssociationType newRoleType(String name, ITagType tagType);

}
