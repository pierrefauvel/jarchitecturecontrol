package org.jarco.control.specifications.itf;

import java.util.List;

import org.jarco.code.external.ICodeElement;
import org.jarco.control.specifications.Violation;

public interface IAssertion<T extends ICodeElement> {
  public Violation assertRule(T t, List stack);
  public String toXml();
}
