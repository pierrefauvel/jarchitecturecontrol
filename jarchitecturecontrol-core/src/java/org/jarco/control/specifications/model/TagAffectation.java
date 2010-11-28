package org.jarco.control.specifications.model;

import java.util.List;

import org.jarco.code.external.ICodeElement;
import org.jarco.control.specifications.ContextStrategies;
import org.jarco.control.specifications.itf.IConsequence;
import org.jarco.control.specifications.model.FM.kind;
import org.jarco.persistence.FromXmlFactory;
import org.jarco.persistence.SpecificationFromXmlFactory;
import org.jarco.swing.tree.IExposableAsANode;
import org.jarco.tags.external.ITag;
import org.jarco.tags.external.ITagAssociation;
import org.jarco.tags.external.ITagAssociationType;
import org.jarco.tags.external.ITagAttributeType;
import org.jarco.tags.external.ITagRepository;
import org.jarco.tags.external.ITagRole;
import org.jarco.tags.external.ITagRoleType;
import org.jarco.tags.external.ITagType;
import org.jarco.tags.internal.TagRoleInternal;
import org.jarco.tags.internal.TagRoleTypeInternal;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TagAffectation<T extends ICodeElement> implements IConsequence<T>, IExposableAsANode{
	@FM(kind=kind.component)
	private String[][] name_values;
	@FM(kind=kind.component)
	private ITagType type;
	@FM(kind=kind.injected)
	private ITagRepository trepo;
	
	//pour l'éditeur graphique
	public TagAffectation()
	{
	}
	
	public TagAffectation(ITagRepository trepo, ITagType type, String[]...name_values)
	{
		this.name_values=name_values;
		this.type=type;
		loop:for(String[] ai : name_values)
		{
			ITagAttributeType at = type.getAttributes().get(ai[0]);
			if(ai[0].compareTo(ITagType.ASSOCIATION_NAME)==0) continue loop;
			if(ai[0].compareTo(ITagType.ASSOCIATION_TYPE_NAME)==0) continue loop;
			if(ai[0].compareTo(ITagType.ROLE_TYPE_NAME)==0) continue loop;
			if(at==null) throw new RuntimeException("PF57 Unexpected attribute "+ai[0]+" in type "+type);
		}
		this.trepo=trepo;
	}
	
	public void apply(T e, List stack)
	{
		ITag tag = type.newInstance(e);
		boolean associationFound=false;
		String roleTypeName=null;
		String associationName=null;
		String associationTypeName=null;
		for(String[] ai : name_values)
		{
			ITagAttributeType at = type.getAttributes().get(ai[0]);
			if(at!=null)
			{
				tag.setAttributeValue(at,ContextStrategies.resolve(ai[1],stack));
			}
			else if(ai[0].compareTo(ITagType.ASSOCIATION_NAME)==0)
			{
				associationFound=true;
				associationName = ContextStrategies.resolve(ai[1],stack);
			}
			else if(ai[0].compareTo(ITagType.ASSOCIATION_TYPE_NAME)==0)
			{
				associationFound=true;
				associationTypeName=ContextStrategies.resolve(ai[1],stack);
			}
			else if(ai[0].compareTo(ITagType.ROLE_TYPE_NAME)==0)
			{
				associationFound=true;
				roleTypeName = ContextStrategies.resolve(ai[1],stack);
			}
		}
		if(associationFound)
		{
			if(associationName==null) throw new RuntimeException("Found an association, missing association id in "+e);
			if(associationTypeName==null) throw new RuntimeException("Found an association, missing association type name in "+e);
			if(roleTypeName==null) throw new RuntimeException("Found an association, missing role type name in "+e);
			ITagAssociationType taty= trepo.getTagAssociationTypes().get(associationTypeName);
			if(taty==null) throw new RuntimeException("Could not resolve association "+associationTypeName);
			ITagAssociation tga = taty.getInstances().get(associationName);
			if(tga==null) {
				tga = taty.newInstance(associationName);
			};
			ITagRoleType trt = taty.getRoles().get(roleTypeName);
			if(trt==null) throw new RuntimeException("Could not resolve role type "+roleTypeName+" in association "+associationTypeName);
			ITagRole trti = trt.newInstance(tga,tag);
		}
		e.addTag(tag);
	}
	
	public String toString()
	{
		StringBuffer sb=new StringBuffer("Then Tag #"+(type==null ? "null" : type.getName())+" ");
		if(name_values!=null)
			for(String[] nv : name_values)
		{
			sb.append(nv[0]+"="+nv[1]+" ");
		};
		return sb.toString();
	}
	
	public String toLabel()
	{
		StringBuffer sb=new StringBuffer("<html><b>tag-affectation</b> "+(type==null ? "<i>no type</i>" : "<b>type</b>="+type.getName())+" ");
		if(name_values!=null)
			for(String[] nv : name_values)
		{
			sb.append("<b>{</b>"+nv[0]+"<b>=</b>"+nv[1]+"</b>}</b>");
		};
		return sb.toString();
	}

	public String toXml()
	{
		StringBuffer sb=new StringBuffer("<tag-affectation type=\""+type.getName()+"\">");
		for(String[] nv : name_values)
		{
			sb.append("<attribute name=\""+nv[0]+"\" value=\""+nv[1]+"\"/>");
		};
		sb.append("</tag-affectation>");
		return sb.toString();
	}
	
	public static TagAffectation fromXml(FromXmlFactory f, Element e)
	{
		String type = e.getAttribute("type");
		NodeList nl = e.getChildNodes();
		String[][] nv = new String[nl.getLength()][2];
		for (int i=0;i<nv.length;i++)
		{
			if(nl.item(i) instanceof Element)
			{
			Element ei = (Element)(nl.item(i));
			nv[i][0]=ei.getAttribute("name");
			nv[i][1]=ei.getAttribute("value");
			}
		}
		ITagRepository repo = ((SpecificationFromXmlFactory)f).getTagRepository();
		ITagType ttype = repo.getTagTypes().get(type);
		if(ttype==null)
			throw new RuntimeException("Could not find tag type "+type+ "in "+f.dump(e)+" "+repo.getTagTypes());
		return new TagAffectation(repo,ttype,nv);
	}

}
