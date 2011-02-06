package org.jarco.tags.external;

import org.jarco.code.external.INamed;
import org.jarco.swing.components.IExposableAsANode;
import org.jarco.xml.IPersistableAsXml;

public interface ITagAttributeType extends INamed, IPersistableAsXml, IExposableAsANode{
	public ITagType getType();
}
