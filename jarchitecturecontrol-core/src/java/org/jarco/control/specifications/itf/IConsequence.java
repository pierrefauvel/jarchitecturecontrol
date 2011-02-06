package org.jarco.control.specifications.itf;

import java.util.List;

import org.jarco.swing.components.IExposableAsANode;
import org.jarco.xml.IPersistableAsXml;

public interface IConsequence<T> extends IExposableAsANode,IPersistableAsXml{
	public void apply(T e, List list);
	public String toXml();
	public String toLabel();
}
