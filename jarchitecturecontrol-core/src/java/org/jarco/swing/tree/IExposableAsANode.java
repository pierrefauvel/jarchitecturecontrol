package org.jarco.swing.tree;

import org.jarco.persistence.IPersistableAsXml;

public interface IExposableAsANode extends IPersistableAsXml{

	String toLabel();
}
