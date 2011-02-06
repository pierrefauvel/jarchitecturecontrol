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
import org.jarco.swing.components.FM;
import org.jarco.swing.components.IExposableAsANode;
import org.jarco.swing.components.FM.kind;
import org.jarco.tags.external.ITagAssociation;
import org.jarco.tags.external.ITagAssociationType;
import org.jarco.tags.external.ITagRoleType;
import org.jarco.tags.external.ITagType;
import org.jarco.xml.FromXmlFactory;
import org.jarco.xml.TagRepositoryFromXmlFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TagAssociationTypeInternal implements ITagAssociationType, IExposableAsANode {

	@FM(kind=kind.component)
	private String name;
	@FM(kind=kind.treenode)
//	private Map<ITagRoleType,ITagType> roles=new HashMap<ITagRoleType,ITagType>();
	private Set<ITagRoleType> roles = new HashSet<ITagRoleType>();

	private transient Set<ITagAssociation> instances=new HashSet<ITagAssociation>();
	
	TagAssociationTypeInternal(String name)
	{
		this.name=name;
	}
	public TagAssociationTypeInternal() {
		// pour manipulation via swing
	}
	public ITagAssociationType newRoleType(String name, ITagType tagType)
	{
		TagRoleTypeInternal tyi = new TagRoleTypeInternal(this,name,tagType);
		newRoleType(tyi);
		return this;
	}
	public void newRoleType(TagRoleTypeInternal tyi) {
//		roles.put(tyi,tagType);
		roles.add(tyi);
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
//		return new ImmutableNamedSet<ITagRoleType>(roles.keySet());
		return new ImmutableNamedSet<ITagRoleType>(roles);
	}
//TO v1.1 � supprimer, rendue inutile par le d�placement du tag type DANS le role
	public ITagType getTypeOfRole(ITagRoleType role)
	{
//		return roles.get(role);
		return ((TagRoleTypeInternal)role).getType();
	}
	public String toString()
	{
		return "Tag Association Type "+name;
	}

	// on ne serialise pas les associations (sont le fruit d'une ex�cution et non la d�finition des tags)
	
	// V1.1 dissocier le r�f�rentiel de d�finition des tags et les tags et associations identifi�es (2 packages diff�rents)
	
//	public static TagAssociationTypeInternal fromXml(FromXmlFactory f,Element e)
	public void fromXml(FromXmlFactory f,Element e)
	{
		String an = e.getAttribute("name");
		this.name=an;
		
//		TagAssociationTypeInternal a = new TagAssociationTypeInternal(an);
//		
		f.pushInContext(this);
		NodeList nl_r = ((Element)(e.getElementsByTagName("tag-role-types").item(0))).getChildNodes();
		for(int i=0;i<nl_r.getLength();i++)
		{
			Element ei = (Element)(nl_r.item(i));
			TagRepositoryFromXmlFactory trxf=(TagRepositoryFromXmlFactory)f;
//			String ttn = ei.getAttribute("tag-type-name");
//			TagTypeInternal tt = (TagTypeInternal)(f.resolveInstanceByRef(TagTypeInternal.class,ttn));
			Element ej = (Element)(ei.getChildNodes());
			TagRoleTypeInternal r = (TagRoleTypeInternal)(f.fromXml(ej));
//			a.roles.put(r,tt);
			this.roles.add(r);
		}
		f.popFromContext(this.getClass());
//		return a;
	}
	@Override
	public String toLabel() {
		return "<html>TagAssociationType <b>"+name+"</b>";
	}
	@Override
	public String toXml() {
		StringBuffer sb=new StringBuffer();
		sb.append("<tag-association-type-internal name=\""+name+"\">");
//		sb.append("<tag-associations>");
//		for(ITagAssociation a : instances)
//		{
//			TagAssociationInternal ai = (TagAssociationInternal)a;
//			sb.append(ai.toXml());
//		}
//		sb.append("</tag-associations>");

//		sb.append("<tag-role-types-and-tag-types>");
		sb.append("<tag-role-types>");
		for(ITagRoleType r : roles)
		{
			//r est serialise 
			//tt est juste r�f�renc�
//			ITagType tt = getTypeOfRole(r);
//			sb.append("<tag-role-type tag-type-name=\""+tt.getName()+"\">");
//			sb.append(((TagRoleTypeInternal)r).toXml());
//			sb.append("</tag-role-type>");
			sb.append(((TagRoleTypeInternal)r).toXml());
		}
//		sb.append("</tag-role-types-and-tag-types>");
		sb.append("</tag-role-types>");
		sb.append("</tag-association-type-internal>");
		return sb.toString();
	}
}
