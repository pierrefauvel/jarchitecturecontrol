package org.jarco.control.specifications.itf;

import org.jarco.code.external.ICodeElement;
import org.jarco.collections.ImmutableMap;
import org.jarco.swing.components.IExposableAsANode;
import org.jarco.xml.IPersistableAsXml;

public interface IPredicate<T extends ICodeElement> extends IExposableAsANode, IPersistableAsXml{
  public ImmutableMap<String, String> include(T t);
  public String toXml();
}
