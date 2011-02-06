package org.jarco.code.external;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jarco.collections.ImmutableList;
import org.jarco.collections.ImmutableNamedList;
import org.jarco.collections.ImmutableNamedMap;
import org.jarco.collections.ImmutableSet;

public interface IClass extends ICodeElement, INamed{
	public String getName();
	public String getLongName();
	public ImmutableSet<EModifier> getModifiers();
	public IPackage getPackage();
	public ImmutableSet<IClass> getReferencedClasses();
//	public IClass getSuperClass();
//	public FList<IClass> getActualTypeArgumentsInSuperclass();
	public IType getSuperClass();
//	public FSet<IClass> getInterfaces();
	public ImmutableSet<IType> getInterfaces();
	public ImmutableNamedList<IField> getDeclaredFields();
	public ImmutableSet<IClass> getInnerClasses();
	public ImmutableList<IMethod> getDeclaredMethods();
	public ImmutableList<IMethod> getConstructors();
	public IClass getParentClass();
	public IMethod getMethodByNameAndTypes(String mn, String[] pt);
	public ImmutableNamedMap<IAnnotation> getAnnotations();
	public IField getFieldByName(String fn);
	public ImmutableList<IMethod> getMethodByName(String mn);
}
