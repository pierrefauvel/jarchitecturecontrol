package org.jarco.code.external;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jarco.collections.ImmutableList;
import org.jarco.collections.ImmutableNamedMap;
import org.jarco.collections.ImmutableSet;

public interface IMethod extends ICodeElement, INamed {
	public String getName();
	public ImmutableSet<EModifier> getModifiers();
//	public IClass getReturnType();
//	public FList<IClass> getParameterTypes();
//	public FSet<IClass> getExceptionThrown();
	public IType getReturnType();
	public ImmutableList<IType> getParameterTypes();
	public ImmutableSet<IType> getExceptionThrown();
	public ImmutableSet<IClass> getInstanciatesClasses();
	public ImmutableSet<IMethod> getInvokesMethods();
	public IClass getParentClass();
	public ImmutableSet<IField> getReadsFields();
	public ImmutableSet<IField> getWritesFields();
	public ImmutableNamedMap<IAnnotation> getAnnotations();
//	public FList<IClass> getActualTypeArgumentsInReturn();
//	public FList<IClass> getActualTypeArgumentsInParameters();
//	public FList<IClass> getActualTypeArgumentsInExceptions();
}
