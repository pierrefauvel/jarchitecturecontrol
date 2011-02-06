package org.jarco.code.external;

import org.jarco.collections.ImmutableNamedSet;
import org.jarco.swing.components.IExposableAsANode;
import org.jarco.tags.external.ITag;

public interface ICodeElement extends INamed, IExposableAsANode{
	public ImmutableNamedSet<ITag> getTags();
	public void addTag(ITag t);
	public ICodeRepository getRepository();
}
