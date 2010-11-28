package org.jarco.tags.internal;

import org.jarco.control.specifications.model.FM;
import org.jarco.control.specifications.model.FM.kind;
import org.jarco.persistence.FromXmlFactory;
import org.jarco.swing.tree.IExposableAsANode;
import org.jarco.tags.external.ITagAttributeType;
import org.jarco.tags.external.ITagType;
import org.w3c.dom.Element;

public class TagAttributeTypeInternal implements ITagAttributeType, IExposableAsANode {

	@FM(kind=kind.ignore)
	private ITagType t;
	@FM(kind=kind.component)
	private String name;

	 TagAttributeTypeInternal(ITagType t, String name)
	{
		this.t=t;
		this.name=name;
	}
	
	public TagAttributeTypeInternal() {
		//pour manipulation via swing
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

	public static TagAttributeTypeInternal fromXml(FromXmlFactory f,Element e)
	{
		String n = e.getAttribute("name");
		TagTypeInternal tt = (TagTypeInternal)(f.peekInContext(TagTypeInternal.class));
		TagAttributeTypeInternal tai = new TagAttributeTypeInternal(tt,n);
		return tai;
	}

	@Override
	public String toLabel() {
		return "<html>Tag Attribute Type<b>"+name+"</b>";
	}

	@Override
	public String toXml() {
		StringBuffer sb=new StringBuffer();
		sb.append("<tag-attribute-type-internal name=\""+name+"\" />");
		return sb.toString();
	}
}
