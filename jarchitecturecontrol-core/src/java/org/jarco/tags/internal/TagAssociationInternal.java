package org.jarco.tags.internal;

import java.util.HashMap;
import java.util.Map;

import org.jarco.tags.external.ITag;
import org.jarco.tags.external.ITagAssociation;
import org.jarco.tags.external.ITagAssociationType;
import org.jarco.tags.external.ITagRole;
import org.jarco.tags.external.ITagRoleType;

public class TagAssociationInternal implements ITagAssociation {

	private Map<ITagRoleType,ITagRole> map=new HashMap<ITagRoleType,ITagRole>();
	private ITagAssociationType type;
	private String name;

	TagAssociationInternal(ITagAssociationType type, String name)
	{
		this.type=type;
		this.name=name;
	}
	public void setRole (ITagRoleType roleType, ITagRole role)
	{
		map.put(roleType,role);
	}
	
	public ITagRole getRole(ITagRoleType roleType) {
		return map.get(roleType);
	}

	public ITagAssociationType getType() {
		return type;
	}
	public String getName() {
		return name;
	}
	
	public String toString()
	{
		return "Tag Association "+name;
	}

}
