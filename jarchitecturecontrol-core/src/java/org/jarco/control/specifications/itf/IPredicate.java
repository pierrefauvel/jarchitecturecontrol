package org.jarco.control.specifications.itf;

import org.jarco.code.external.ICodeElement;
import org.jarco.collections.ImmutableMap;

public interface IPredicate<T extends ICodeElement> {
  public ImmutableMap<String, String> include(T t);
  public String toXml();
}
