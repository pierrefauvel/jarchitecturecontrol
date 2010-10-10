package org.jarco.control.specifications.itf;

import java.util.List;

import org.jarco.code.external.ICodeElement;
import org.jarco.control.specifications.ElementAndContext;

public interface IProductionRule<T extends ICodeElement> {
  public List<ElementAndContext<T>> produce(Object aContext, IPredicate<T> filter, List ctx);
  public String toXml();
}

