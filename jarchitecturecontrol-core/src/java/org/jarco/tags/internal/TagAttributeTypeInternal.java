package org.jarco.tags.internal;

import org.jarco.swing.components.FM;
import org.jarco.swing.components.IExposableAsANode;
import org.jarco.swing.components.FM.kind;
import org.jarco.tags.external.ITagAttributeType;
import org.jarco.tags.external.ITagType;
import org.jarco.xml.FromXmlFactory;
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

//	public static TagAttributeTypeInternal fromXml(FromXmlFactory f,Element e)
	public void fromXml(FromXmlFactory f,Element e)
	{
		name = e.getAttribute("name");
		t = (TagTypeInternal)(f.peekInContext(TagTypeInternal.class));
	}

	@Override
	public String toLabel() {
		return "<html>Tag Attribute Type<b>"+name+"</b></html>";
	}

	@Override
	public String toXml() {
		StringBuffer sb=new StringBuffer();
		sb.append("<tag-attribute-type-internal name=\""+name+"\" />");
		return sb.toString();
	}
}
