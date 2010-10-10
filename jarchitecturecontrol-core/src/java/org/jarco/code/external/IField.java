package org.jarco.code.external;

import java.util.Set;

import org.jarco.collections.ImmutableList;
import org.jarco.collections.ImmutableNamedMap;
import org.jarco.collections.ImmutableSet;

public interface IField extends ICodeElement, INamed{
	public String getName();
	public ImmutableSet<EModifier> getModifiers();
	public IType getFieldType();
//	public FList<IClass> getActualTypeArguments();
	public IClass getParentClass();
	public ImmutableNamedMap<IAnnotation> getAnnotations();
}
