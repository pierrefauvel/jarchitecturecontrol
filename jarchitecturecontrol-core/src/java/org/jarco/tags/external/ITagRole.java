package org.jarco.tags.external;

import org.jarco.xml.IPersistableAsXml;

public interface ITagRole  {
	public ITagRoleType getType();
	public ITagAssociation getAssociation();
	public ITag getTag();
}
