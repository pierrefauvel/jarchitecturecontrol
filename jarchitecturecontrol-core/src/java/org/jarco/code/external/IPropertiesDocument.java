package org.jarco.code.external;

import org.jarco.collections.ImmutableSet;

public interface IPropertiesDocument extends ICodeElement{
	public IProject getProject();
	public String get(String key);
	public ImmutableSet<String> keys();
	public String getName();
}
