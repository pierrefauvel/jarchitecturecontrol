package org.jarco.control.specifications.itf;

import java.util.List;

import org.jarco.code.external.ICodeElement;
import org.jarco.control.Violation;
import org.jarco.swing.components.IExposableAsANode;
import org.jarco.xml.IPersistableAsXml;

public interface IAssertion<T extends ICodeElement> extends IExposableAsANode, IPersistableAsXml{
  public Violation assertRule(T t, List stack);
  public String toXml();
}
