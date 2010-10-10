package org.jarco.tags.external;

import org.jarco.code.external.INamed;

public interface ITagRoleType extends INamed{
	public ITagAssociationType getParentAssociation();
	public ITagRole newInstance(ITagAssociation association, ITag tag);
}
