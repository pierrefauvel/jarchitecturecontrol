package org.jarco.tags.external;

import org.jarco.code.external.INamed;
import org.jarco.collections.ImmutableSet;
import org.jarco.tags.internal.TagRoleInternal;

public interface ITagAssociation extends INamed{
	public ITagAssociationType getType();
	public ITagRole getRole(ITagRoleType roleType);
	public void setRole (ITagRoleType roleType, ITagRole role);
}
