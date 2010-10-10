package org.jarco.control.specifications.itf;

import java.util.List;

public interface IConsequence<T> {
	public void apply(T e, List list);
	public String toXml();
}
