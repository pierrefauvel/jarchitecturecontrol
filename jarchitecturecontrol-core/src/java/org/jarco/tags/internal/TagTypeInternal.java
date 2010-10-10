package org.jarco.tags.internal;

import java.util.ArrayList;
import java.util.List;

import org.jarco.code.external.ICodeElement;
import org.jarco.collections.ImmutableList;
import org.jarco.collections.ImmutableNamedList;
import org.jarco.tags.external.ITag;
import org.jarco.tags.external.ITagAttribute;
import org.jarco.tags.external.ITagType;

public class TagTypeInternal implements ITagType {

	private String name;
	private List<ITag> ls_tags=new ArrayList<ITag>();
	private List<ITagAttribute> ls_attributes = new ArrayList<ITagAttribute>();
	
	 TagTypeInternal(String string) {
		name=string;
	}


	public ImmutableNamedList<ITagAttribute> getAttributes() {
		return new ImmutableNamedList<ITagAttribute>(ls_attributes);
	}
	
	public ITagType newAttribute(String an)
	{
		if(an.compareTo(ITagType.ASSOCIATION_NAME)==0)
			throw new RuntimeException("PF61 Attribute name "+an+ " is reserved (type="+name+")");
		if(an.compareTo(ITagType.ASSOCIATION_TYPE_NAME)==0)
			throw new RuntimeException("PF61 Attribute name "+an+ " is reserved (type="+name+")");
		if(an.compareTo(ITagType.ROLE_TYPE_NAME)==0)
			throw new RuntimeException("PF61 Attribute name "+an+ " is reserved (type="+name+")");
		ITagAttribute a = new TagAttributeInternal(this,an);
		ls_attributes.add(a);
		return this;
	}

	public String getName() {
		return name;
	}

	public ImmutableList<ITag> getTags() {
		return new ImmutableList<ITag>(ls_tags);
	}

	public ITag newInstance(ICodeElement element) {
		ITag tag = new TagInternal(this,element);
		ls_tags.add(tag);
		return tag;
	}
	public String toString()
	{
		return "Tag Type #"+name+" "+ls_attributes;
	}
}
