package org.jarco.tags.external;

import org.jarco.code.external.INamed;
import org.jarco.swing.components.IExposableAsANode;
import org.jarco.xml.IPersistableAsXml;

public interface ITagRoleType extends INamed,IPersistableAsXml, IExposableAsANode{
	public ITagAssociationType getParentAssociation();
	public ITagRole newInstance(ITagAssociation association, ITag tag);
}
