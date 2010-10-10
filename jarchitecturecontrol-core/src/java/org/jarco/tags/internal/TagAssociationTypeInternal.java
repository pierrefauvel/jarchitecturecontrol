package org.jarco.tags.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jarco.collections.ImmutableList;
import org.jarco.collections.ImmutableNamedSet;
import org.jarco.collections.ImmutableSet;
import org.jarco.tags.external.ITagAssociation;
import org.jarco.tags.external.ITagAssociationType;
import org.jarco.tags.external.ITagRoleType;
import org.jarco.tags.external.ITagType;

public class TagAssociationTypeInternal implements ITagAssociationType {

	private String name;
	private Map<ITagRoleType,ITagType> roles=new HashMap<ITagRoleType,ITagType>();
	private Set<ITagAssociation> instances=new HashSet<ITagAssociation>();
	
	TagAssociationTypeInternal(String name)
	{
		this.name=name;
	}
	public ITagAssociationType newRoleType(String name, ITagType tagType)
	{
		TagRoleTypeInternal tyi = new TagRoleTypeInternal(this,name);
		roles.put(tyi,tagType);
		return this;
	}
	
	public ITagAssociation newInstance(String name)
	{
		TagAssociationInternal tai = new TagAssociationInternal(this,name);
		instances.add(tai);
		return tai;
	}
	
	public ImmutableNamedSet<ITagAssociation> getInstances()
	{
		return new ImmutableNamedSet(instances);
	}
	
	public String getName() {
		return name;
	}

	public ImmutableNamedSet<ITagRoleType> getRoles() {
		return new ImmutableNamedSet<ITagRoleType>(roles.keySet());
	}

	public ITagType getTypeOfRole(ITagRoleType role)
	{
		return roles.get(role);
	}
	public String toString()
	{
		return "Tag Association Type "+name;
	}
}
