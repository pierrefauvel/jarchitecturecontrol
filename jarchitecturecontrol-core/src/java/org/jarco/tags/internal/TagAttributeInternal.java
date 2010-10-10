package org.jarco.tags.internal;

import org.jarco.tags.external.ITagAttribute;
import org.jarco.tags.external.ITagType;

public class TagAttributeInternal implements ITagAttribute {

	private ITagType t;
	private String name;

	 TagAttributeInternal(ITagType t, String name)
	{
		this.t=t;
		this.name=name;
	}
	
	public ITagType getType() {
		return t;
	}

	public String getName() {
		return name;
	}
	
	public String toString()
	{
		return "Tag Attribute "+getName();
	}

}
