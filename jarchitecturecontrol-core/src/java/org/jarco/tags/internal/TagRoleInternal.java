package org.jarco.tags.internal;

import org.jarco.swing.tree.IExposableAsANode;
import org.jarco.tags.external.ITag;
import org.jarco.tags.external.ITagAssociation;
import org.jarco.tags.external.ITagRole;
import org.jarco.tags.external.ITagRoleType;
import org.jarco.xml.FromXmlFactory;
import org.w3c.dom.Element;

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
	
	public TagRoleInternal() {
		//pour manipulation via swing
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
