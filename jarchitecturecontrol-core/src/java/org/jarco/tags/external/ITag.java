package org.jarco.tags.external;

import org.jarco.code.external.ICodeElement;
import org.jarco.code.external.INamed;
import org.jarco.collections.ImmutableList;
import org.jarco.collections.ImmutableMapWithNamedKeys;
import org.jarco.collections.ImmutableNamedMap;
import org.jarco.tags.internal.TagRoleInternal;

public interface ITag extends INamed {
	
	public ITagType getType();
	public ICodeElement getTaggedElement();
	public ITagRole getRolePlayedInAssociation();
	public ImmutableMapWithNamedKeys<ITagAttributeType,String> getAttributeValues();
	public void setAttributeValue(ITagAttributeType at, String string);
	public void setRolePlayedInAssociation(ITagRole tagRoleImpl);
}
