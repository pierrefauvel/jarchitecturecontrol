package org.jarco.code.external;

import org.jarco.collections.ImmutableList;

public interface IXmlElement extends INamed, ICodeElement {

	public String getValueForXPath(String strExpression);
	public ImmutableList<IXmlElement> getChildNodes();
	public ImmutableList<IXmlElement> getChildNodesForXPath(String xpath);

}
