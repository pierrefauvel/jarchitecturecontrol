package org.jarco.tags.internal;

import org.jarco.tags.external.ITag;
import org.jarco.tags.external.ITagAssociation;
import org.jarco.tags.external.ITagRole;
import org.jarco.tags.external.ITagRoleType;

public class TagRoleInternal implements ITagRole{

	private ITagRoleType type;
	private ITagAssociation association;
	private ITag tag;
	
	TagRoleInternal(ITagRoleType type,ITagAssociation association, ITag tag )
	{
		this.type=type;
		this.association=association;
		association.setRole(type,this);
		this.tag=tag;
		tag.setRolePlayedInAssociation(this);
	}
	
	public ITagAssociation getAssociation() {
		return association;
	}

	public ITagRoleType getType() {
		return type;
	}
	
	public ITag getTag(){
		return tag;
	}
	
	public String toString(){
		return "Role "+type.getName()+" played by "+tag+" in "+association;
	}
}
