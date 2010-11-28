package org.jarco.control.specifications.model;

import java.util.HashMap;

import org.jarco.code.external.IClass;
import org.jarco.code.external.ICodeElement;
import org.jarco.collections.ImmutableMap;
import org.jarco.collections.ImmutableNamedMap;
import org.jarco.control.specifications.itf.IPredicate;
import org.jarco.control.specifications.model.FM.kind;
import org.jarco.persistence.FromXmlFactory;
import org.jarco.persistence.SpecificationFromXmlFactory;
import org.jarco.swing.tree.IExposableAsANode;
import org.jarco.tags.external.ITag;
import org.jarco.tags.external.ITagAttributeType;
import org.jarco.tags.external.ITagRepository;
import org.jarco.tags.external.ITagType;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TagPredicate<T extends ICodeElement> implements IPredicate<T>, IExposableAsANode {

	@FM(kind=kind.component)
	private String[][] tagAttributes;
	@FM(kind=kind.component)
	private ITagType ty;
	
	//pour l'éditeur graphique
	public TagPredicate()
	{
	}
	
	public TagPredicate(ITagType ty, String[]... at) {
		tagAttributes=at;
		this.ty=ty;
		for(String[] ai : at)
		{
			ITagAttributeType a = ty.getAttributes().get(ai[0]);
			if(a==null) throw new RuntimeException("PF57 Unexpected attribute "+ai[0]+" in type "+ty);
		};
	}

	public ImmutableMap<String, String> include(T t) {
		loop: for(ITag ta : t.getTags())
		{
			ITagType tt = ta.getType();
			
			if(ta.getName().compareTo(ty.getName())!=0)
				continue loop;
			for(String[] ai : tagAttributes)
			{
				String an = ai[0];
				String av = ai[1];
				ITagAttributeType at = tt.getAttributes().get(an);
				if(at==null)
					throw new RuntimeException("PF60 Unexpected attribute "+an+" in tag of type "+ty.getName());
				String v = ta.getAttributeValues().get(at);
				if(av.compareTo(v)!=0)
					continue loop;
			}
			return new ImmutableMap<String,String>(new HashMap<String,String>());
		};
		return null;
	}
	
	public String toString()
	{
		StringBuffer sb=new StringBuffer();
		sb.append("Tagged #"+(ty==null ? "null" : ty.getName()));
		if(ty!=null)
			for(String[] ai : tagAttributes)
		{
			sb.append(" "+ai[0]+"="+ai[1]);
		};
		return sb.toString();
	}
	
	public String toLabel()
	{
		StringBuffer sb=new StringBuffer();
		sb.append("<b>tagged</b> "+(ty==null ? "<i>no type</i>" : "<b>type</b>="+ty.getName()));
		if(ty!=null)
			for(String[] ai : tagAttributes)
		{
			sb.append("<b>{</b>"+ai[0]+"<b>=</b>"+ai[1]+"</b>}</b>");
		};
		return sb.toString();
	}

	public String toXml()
	{
		StringBuffer sb=new StringBuffer();
		sb.append("<tag-predicate name=\""+ty.getName()+"\">");
		for(String[] ai : tagAttributes)
		{
			sb.append("<attribute name=\""+ai[0]+"\" value=\""+ai[1]+"\" />");
		};
		sb.append("</tag-predicate>");
		return sb.toString();
	}

	public static TagPredicate fromXml(FromXmlFactory f, Element e)
	{
		String type = e.getAttribute("name");
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
//		System.out.println("PF71 "+type);
//		for(ITagType itt : repo.getTagTypes())
//		{
//			System.out.println("PF70 "+itt.getName());
//		}
		if(ttype==null)
			throw new RuntimeException("Could not find tag type "+type+ "in "+f.dump(e)+" "+repo.getTagTypes());
		return new TagPredicate(ttype,nv);
	}

}
