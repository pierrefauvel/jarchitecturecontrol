package org.jarco.tags.internal;

import org.jarco.control.specifications.model.FM;
import org.jarco.control.specifications.model.FM.kind;
import org.jarco.persistence.FromXmlFactory;
import org.jarco.swing.tree.IExposableAsANode;
import org.jarco.tags.external.ITag;
import org.jarco.tags.external.ITagAssociation;
import org.jarco.tags.external.ITagAssociationType;
import org.jarco.tags.external.ITagRole;
import org.jarco.tags.external.ITagRoleType;
import org.jarco.tags.external.ITagType;
import org.w3c.dom.Element;

public class TagRoleTypeInternal implements ITagRoleType, IExposableAsANode {

	@FM(kind=kind.ignore)
	private ITagAssociationType at;
	@FM(kind=kind.component)
	private String n;
	
	//TODO v1.1 mettre en place une gestion plus rigoureuse des références (que se passe-t-il en cas de suppression, ...)
	@FM(kind=kind.component)
	private ITagType tt;
	
	TagRoleTypeInternal(ITagAssociationType at, String n, ITagType tt)
	{
		this.at=at;
		this.n=n;
		this.tt=tt;
	}
	
	public TagRoleTypeInternal() {
		//pour manipulation via swing
	}

	public ITagAssociationType getParentAssociation() {
		return at;
	}

	public String getName() {
		return n;
	}

	public ITagType getType()
	{
		return tt;
	}
	
	public ITagRole newInstance(ITagAssociation association, ITag tag)
	{
		return new TagRoleInternal(this,association,tag);
	}
	
	public String toString()
	{
		return "Tole Type "+n;
	}
	
/*
	private ITagAssociationType at;
	private String n;
 */
	public static TagRoleTypeInternal fromXml(FromXmlFactory f,Element e)
	{
		TagRoleTypeInternal rti = new TagRoleTypeInternal();
		rti.n = e.getAttribute("name");
		TagAssociationTypeInternal at = (TagAssociationTypeInternal) f.peekInContext(TagAssociationTypeInternal.class);
		rti.at = at;
		rti.tt = (TagTypeInternal)(f.resolveInstanceByRef(TagTypeInternal.class, e.getAttribute("tag-type-name")));
		return rti;
	}

	@Override
	public String toLabel() {
		return "<html>Tag Role Type <b>"+n+"</b></html>";
	}

	//TODO v1.1 revoir la construction du xml : il faut encoder les attributs à la XML
	
	@Override
	public String toXml() {
		StringBuffer sb=new StringBuffer();
		sb.append("<tag-role-type-internal name=\""+n+"\" tag-type-name=\""+tt.getName()+"\"/>");
		return sb.toString();
	}
}
