package org.jarco.tags.internal;

import java.util.ArrayList;
import java.util.List;

import org.jarco.code.external.ICodeElement;
import org.jarco.collections.ImmutableList;
import org.jarco.collections.ImmutableNamedList;
import org.jarco.control.specifications.model.FM;
import org.jarco.control.specifications.model.FM.kind;
import org.jarco.swing.tree.IExposableAsANode;
import org.jarco.tags.external.ITag;
import org.jarco.tags.external.ITagAttributeType;
import org.jarco.tags.external.ITagType;
import org.jarco.xml.FromXmlFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TagTypeInternal implements ITagType, IExposableAsANode {

	@FM(kind=kind.component)
	private String name;
	@FM(kind=kind.treenode)
	private List<ITagAttributeType> ls_attributes = new ArrayList<ITagAttributeType>();

	
	private transient List<ITag> instances=new ArrayList<ITag>();
	
	 TagTypeInternal(String string) {
		name=string;
	}


	public TagTypeInternal() {
		//pour manipulation via swing
	}


	public ImmutableNamedList<ITagAttributeType> getAttributes() {
		return new ImmutableNamedList<ITagAttributeType>(ls_attributes);
	}
	
	public ITagType newAttribute(String an)
	{
		if(an.compareTo(ITagType.ASSOCIATION_NAME)==0)
			throw new RuntimeException("PF61 Attribute name "+an+ " is reserved (type="+name+")");
		if(an.compareTo(ITagType.ASSOCIATION_TYPE_NAME)==0)
			throw new RuntimeException("PF61 Attribute name "+an+ " is reserved (type="+name+")");
		if(an.compareTo(ITagType.ROLE_TYPE_NAME)==0)
			throw new RuntimeException("PF61 Attribute name "+an+ " is reserved (type="+name+")");
		ITagAttributeType a = new TagAttributeTypeInternal(this,an);
		newAttribute(a);
		return this;
	}
	public void newAttribute(ITagAttributeType at) {
		ls_attributes.add(at);
	}

	public String getName() {
		return name;
	}

	public ImmutableList<ITag> getTags() {
		return new ImmutableList<ITag>(instances);
	}

	public ITag newInstance(ICodeElement element) {
		ITag tag = new TagInternal(this,element);
		instances.add(tag);
		return tag;
	}
	public String toString()
	{
		return "Tag Type #"+name+" "+ls_attributes;
	}


	@Override
	public String toLabel() {
		return "<html>Tag Type <b>"+name+"</b></html>";
	}

//TODO v1.0 indenter le xml ? 
//TODO v2.0 passer à un format non-xml ??

//NB : on ne serialise pas les TagInternal
	@Override
	public String toXml() {
		StringBuffer sb=new StringBuffer();
		sb.append("<tag-type-internal name=\""+name+"\">");
		sb.append("<tag-attribute-types>");
		for(ITagAttributeType at : ls_attributes)
		{
			sb.append(((TagAttributeTypeInternal)at).toXml());
		}
		sb.append("</tag-attribute-types>");
		sb.append("</tag-type-internal>");
		return sb.toString();
	}
	public static TagTypeInternal fromXml(FromXmlFactory f,Element e)
	{
		TagTypeInternal tai = new TagTypeInternal();
		String tn = e.getAttribute("name");
		tai.name=tn;
		f.pushInContext(tai);
		NodeList nl = e.getElementsByTagName("tag-attribute-types").item(0).getChildNodes();
		for(int i=0;i<nl.getLength();i++)
		{
			Element ei = (Element)(nl.item(i));
			TagAttributeTypeInternal ati = (TagAttributeTypeInternal)(f.fromXml(ei));
			tai.ls_attributes.add(ati);
		}
		f.popFromContext(TagTypeInternal.class);
		f.registerInstanceByRef(tai, tai.name);
		return tai;
	}


}
