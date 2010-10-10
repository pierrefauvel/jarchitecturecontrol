package org.jarco.tags.internal;

import org.jarco.tags.external.ITag;
import org.jarco.tags.external.ITagAssociation;
import org.jarco.tags.external.ITagAssociationType;
import org.jarco.tags.external.ITagRole;
import org.jarco.tags.external.ITagRoleType;

public class TagRoleTypeInternal implements ITagRoleType {

	private ITagAssociationType at;
	private String n;

	TagRoleTypeInternal(ITagAssociationType at, String n)
	{
		this.at=at;
		this.n=n;
	}
	
	public ITagAssociationType getParentAssociation() {
		return at;
	}

	public String getName() {
		return n;
	}

	public ITagRole newInstance(ITagAssociation association, ITag tag)
	{
		return new TagRoleInternal(this,association,tag);
	}
	
	public String toString()
	{
		return "Tole Type "+n;
	}
}
