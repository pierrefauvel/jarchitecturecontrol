package org.jarco.tags.internal;

import java.util.HashSet;
import java.util.Set;

import org.jarco.collections.ImmutableNamedList;
import org.jarco.collections.ImmutableNamedSet;
import org.jarco.control.report.ITagReport;
import org.jarco.control.report.filesystem.Indent;
import org.jarco.swing.components.FM;
import org.jarco.swing.components.IExposableAsANode;
import org.jarco.swing.components.FM.kind;
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

	public void report(ITagReport tgr)
	{
		tgr.writeTagSection();
		for(ITagType tty : getTagTypes())
		{
			tgr.writeType(tty);
			for(ITag t : tty.getTags())
			{
				tgr.writeTag(t);
			}
			tgr.popType();
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
				tgr.popAssociation();
			}
			tgr.popAssociationType();
		}
		tgr.close();
	}
	
	public ITagAssociationType newTagAssociationType(String name)
	{
		ITagAssociationType association = new TagAssociationTypeInternal(name);
		return newTagAssociationType(association);
	}
	public ITagAssociationType newTagAssociationType(ITagAssociationType association) {
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

//	public static TagRepositoryInternal fromXml(FromXmlFactory f,Element e)
	public void fromXml(FromXmlFactory f,Element e)
	{
		NodeList nl_t = e.getElementsByTagName("tag-types").item(0).getChildNodes();
		for(int i=0;i<nl_t.getLength();i++)
		{
			Element ei = (Element)(nl_t.item(i));
			this.hs_types.add((ITagType)f.fromXml(ei));
		}
		NodeList nl_a = e.getElementsByTagName("tag-association-types").item(0).getChildNodes();
		for(int i=0;i<nl_a.getLength();i++)
		{
			Element ei = (Element)(nl_a.item(i));
			this.hs_associations.add((ITagAssociationType)f.fromXml(ei));
		}
	}

	@Override
	public String toLabel() {
		return "<html><b>Tag repository:</b>"+hs_types.size()+" tags types & "+hs_associations.size()+" associations</html>";
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
