package org.jarco.code.external;

import org.jarco.collections.ImmutableList;
import org.w3c.dom.Element;

public interface IXmlDocument extends ICodeElement{
	public IProject getProject();
	public IXmlElement getDocumentElement();
	public String getName();
	public ImmutableList<IXmlElement> getChildNodesForXPath(String xpath);
}
