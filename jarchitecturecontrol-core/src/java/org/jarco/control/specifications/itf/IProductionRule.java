package org.jarco.control.specifications.itf;

import java.util.List;

import org.jarco.code.external.ICodeElement;
import org.jarco.control.specifications.ElementAndContext;
import org.jarco.swing.components.IExposableAsANode;
import org.jarco.xml.IPersistableAsXml;

public interface IProductionRule<T extends ICodeElement> extends IExposableAsANode, IPersistableAsXml{
  public List<ElementAndContext<T>> produce(Object aContext, IPredicate<T> filter, List ctx);
  public String toXml();
}

