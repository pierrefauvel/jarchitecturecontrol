package org.jarco.tags.internal;

import java.util.HashSet;
import java.util.Set;

import org.jarco.collections.ImmutableNamedList;
import org.jarco.collections.ImmutableNamedSet;
import org.jarco.control.report.Indent;
import org.jarco.control.report.TagReport;
import org.jarco.control.specifications.model.FM;
import org.jarco.control.specifications.model.FM.kind;
import org.jarco.swing.tree.IExposableAsANode;
import org.jarco.tags.external.ITag;
import org.jarco.tags.external.ITagAssociation;
import org.jarco.tags.external.ITagAssociationType;
import org.jarco.tags.external.ITagRepository;
import org.jarco.tags.external.ITagRoleType;
import org.jarco.tags.external.ITagType;
import org.jarco.xml.FromXmlFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TagRepositoryInternal implements ITagRepository, IExposableAsANode{

	@FM(kind=kind.treenode)
	private Set<ITagType> hs_types = new HashSet<ITagType>();
	@FM(kind=kind.treenode)
	private Set<ITagAssociationType> hs_associations = new HashSet<ITagAssociationType>();
		
	public ImmutableNamedSet<ITagType> getTagTypes() {
		return new ImmutableNamedSet<ITagType>(hs_types);
	}

	public ITagType newTagType(String string) {
		ITagType tag = new TagTypeInternal(string);
		return newTagType(tag);
	}
	public ITagType newTagType(ITagType tag) {
		hs_types.add(tag);
		return tag;
	}

	public void report(TagReport tgr)
	{
		tgr.writeTagSection();
		for(ITagType tty : getTagTypes())
		{
			tgr.writeType(tty);
			for(ITag t : tty.getTags())
			{
				tgr.writeTag(t);
			}
		}
		tgr.writeAssociationSection();
		for(ITagAssociationType tat:getTagAssociationTypes())
		{
			tgr.writeAssociationType(tat);
			for(ITagAssociation as:tat.getInstances())
			{
				tgr.writeAssociation(as);
				for(ITagRoleType trt : tat.getRoles())
				{
					tgr.writeRole(as.getRole(trt));
				}
			}
		}
		tgr.close();
	}
	
	public ITagAssociationType newTagAssociationType(String name)
	{
		ITagAssociationType association = new TagAssociationTypeInternal(name);
		return newTagAssociationType(association);
	}
	public ITagAssociationType newTagAssociationType(ITagAssociationType association) {
		// TODO Auto-generated method stub
		hs_associations.add(association);
		return association;
	}
	
	public ImmutableNamedSet<ITagAssociationType> getTagAssociationTypes()
	{
		return new ImmutableNamedSet<ITagAssociationType>(hs_associations);
	}
	
/**
	private Set<ITagType> hs_types = new HashSet<ITagType>();
	private Set<ITagAssociationType> hs_associations = new HashSet<ITagAssociationType>();
 */

	public static TagRepositoryInternal fromXml(FromXmlFactory f,Element e)
	{
		TagRepositoryInternal r= new TagRepositoryInternal();
		NodeList nl_t = e.getElementsByTagName("tag-types").item(0).getChildNodes();
		for(int i=0;i<nl_t.getLength();i++)
		{
			Element ei = (Element)(nl_t.item(i));
			r.hs_types.add((ITagType)f.fromXml(ei));
		}
		NodeList nl_a = e.getElementsByTagName("tag-association-types").item(0).getChildNodes();
		for(int i=0;i<nl_a.getLength();i++)
		{
			Element ei = (Element)(nl_a.item(i));
			r.hs_associations.add((ITagAssociationType)f.fromXml(ei));
		}
		return r;
	}

	@Override
	public String toLabel() {
		return toString();
	}

	@Override
	public String toXml() {
		StringBuffer sb=new StringBuffer();
		sb.append("<tag-repository-internal>");
		sb.append("<tag-types>");
		for(ITagType tt : hs_types)
		{
			sb.append( ((TagTypeInternal) tt).toXml());
		}
		sb.append("</tag-types>");
		sb.append("<tag-association-types>");
		for(ITagAssociationType at : hs_associations)
		{
			sb.append( ((TagAssociationTypeInternal) at).toXml());
		}
		sb.append("</tag-association-types>");
		sb.append("</tag-repository-internal>");
		return sb.toString();
	}


}
