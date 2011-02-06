package org.jarco.tags.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jarco.code.external.ICodeElement;
import org.jarco.collections.ImmutableMapWithNamedKeys;
import org.jarco.swing.components.IExposableAsANode;
import org.jarco.tags.external.ITag;
import org.jarco.tags.external.ITagAttributeType;
import org.jarco.tags.external.ITagRole;
import org.jarco.tags.external.ITagType;
import org.jarco.xml.FromXmlFactory;
import org.w3c.dom.Element;

public class TagInternal implements ITag{

	private ITagType type;
	private ICodeElement element;
	private Map<ITagAttributeType,String> attributes=new HashMap<ITagAttributeType,String>();
	private ITagRole tagRole;
	
	 TagInternal(TagTypeInternal tagTypeImpl, ICodeElement element) {
		this.type=tagTypeImpl;
		this.element=element;
	}

	public TagInternal() {
		//pour manipulation via swing
	}

	public ImmutableMapWithNamedKeys<ITagAttributeType, String> getAttributeValues() {
		Set<ITagAttributeType> s=new HashSet<ITagAttributeType>();
		for(ITagAttributeType t:type.getAttributes())
			s.add(t);
		for(ITagAttributeType t:attributes.keySet())
			s.remove(t);
		if(s.size()>0)
			System.err.println("PF68 Some attributes are not affected in "+type+" on "+element+":"+s);
		return new ImmutableMapWithNamedKeys<ITagAttributeType,String>(attributes);
	}

	public ICodeElement getTaggedElement() {
		return element;
	}

	public ITagType getType() {
		return type;
	}

	public void setAttributeValue(ITagAttributeType at, String string) {
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
