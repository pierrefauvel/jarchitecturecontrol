package org.jarco.tags.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jarco.code.external.ICodeElement;
import org.jarco.collections.ImmutableMapWithNamedKeys;
import org.jarco.tags.external.ITag;
import org.jarco.tags.external.ITagAttribute;
import org.jarco.tags.external.ITagRole;
import org.jarco.tags.external.ITagType;

public class TagInternal implements ITag {

	private ITagType type;
	private ICodeElement element;
	private Map<ITagAttribute,String> attributes=new HashMap<ITagAttribute,String>();
	private ITagRole tagRole;
	
	 TagInternal(TagTypeInternal tagTypeImpl, ICodeElement element) {
		this.type=tagTypeImpl;
		this.element=element;
	}

	public ImmutableMapWithNamedKeys<ITagAttribute, String> getAttributeValues() {
		Set<ITagAttribute> s=new HashSet<ITagAttribute>();
		for(ITagAttribute t:type.getAttributes())
			s.add(t);
		for(ITagAttribute t:attributes.keySet())
			s.remove(t);
		if(s.size()>0)
			System.err.println("PF68 Some attributes are not affected in "+type+" on "+element+":"+s);
		return new ImmutableMapWithNamedKeys<ITagAttribute,String>(attributes);
	}

	public ICodeElement getTaggedElement() {
		return element;
	}

	public ITagType getType() {
		return type;
	}

	public void setAttributeValue(ITagAttribute at, String string) {
		if(string==null)
			throw new RuntimeException("PF67 Null value set on attribute "+at+" of tag "+type+" of element "+element);
		attributes.put(at,string);
	}

	public String getName() {
		return type.getName();
	}

	public void setRolePlayedInAssociation(ITagRole tr)
	{
		tagRole=tr;
	}
	public ITagRole getRolePlayedInAssociation() {
		return tagRole;
	}
	
	public String toString()
	{
		return "Tag "+type.getName()+" on "+element+" "+attributes+ "role "+tagRole.getType().getName()+" in "+tagRole.getType().getParentAssociation().getName();
	}

}
