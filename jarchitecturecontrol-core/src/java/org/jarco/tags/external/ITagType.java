package org.jarco.tags.external;

import java.util.Collection;

import org.jarco.code.external.ICodeElement;
import org.jarco.code.external.INamed;
import org.jarco.collections.ImmutableList;
import org.jarco.collections.ImmutableNamedList;
import org.jarco.swing.components.IExposableAsANode;
import org.jarco.xml.IPersistableAsXml;

public interface ITagType extends INamed, IPersistableAsXml, IExposableAsANode{
	public static final String ASSOCIATION_NAME = "_ASSOCIATION_NAME_";
	public static final String ASSOCIATION_TYPE_NAME = "_ASSOCIATION_TYPE_NAME_";
	public static final String ROLE_TYPE_NAME = "_TYPE_NAME_";
	public String getName();
	public ImmutableList<ITag> getTags();
	public ImmutableNamedList<ITagAttributeType> getAttributes();
	public ITag newInstance(ICodeElement element);
	public ITagType newAttribute(String string);
}
