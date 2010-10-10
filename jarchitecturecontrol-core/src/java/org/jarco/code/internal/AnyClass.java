package org.jarco.code.internal;

import org.jarco.code.external.EModifier;
import org.jarco.code.external.IAnnotation;
import org.jarco.code.external.IClass;
import org.jarco.code.external.ICodeRepository;
import org.jarco.code.external.IField;
import org.jarco.code.external.IMethod;
import org.jarco.code.external.IPackage;
import org.jarco.code.external.IType;
import org.jarco.collections.ImmutableList;
import org.jarco.collections.ImmutableNamedList;
import org.jarco.collections.ImmutableNamedMap;
import org.jarco.collections.ImmutableNamedSet;
import org.jarco.collections.ImmutableSet;
import org.jarco.tags.external.ITag;

//TODO V1.2 A supprimer quand on saura traiter les types complexes (génériques, types variables...)
public class AnyClass extends ACodeElementInternal implements IClass {

	private IPackage pkg;
	public AnyClass(ICodeRepository repo,IPackage pkg)
	{
		super(repo);
		this.pkg=pkg;
	}
	
	public ImmutableNamedMap<IAnnotation> getAnnotations() {
		return new ImmutableNamedMap<IAnnotation>();
	}

	public ImmutableNamedList<IField> getDeclaredFields() {
		return new ImmutableNamedList<IField>();
	}

	public ImmutableList<IMethod> getDeclaredMethods() {
		return new ImmutableList<IMethod>();
	}

	public IField getFieldByName(String fn) {
		throw new UnsupportedOperationException("getFieldByName is not supported");
	}

	public ImmutableSet<IClass> getInnerClasses() {
		return new ImmutableSet<IClass>();
	}

	public ImmutableSet<IType> getInterfaces() {
		return new ImmutableSet<IType>();
	}

	public String getLongName() {
		return "*Any*";
	}

	public ImmutableList<IMethod> getMethodByName(String mn) {
		return new ImmutableList<IMethod>();
	}

	public IMethod getMethodByNameAndTypes(String mn, String[] pt) {
		return null;
	}

	public ImmutableSet<EModifier> getModifiers() {
		return new ImmutableSet<EModifier>();
	}

	public String getName() {
		return "*Any*";
	}

	public IPackage getPackage() {
		return pkg;
	}

	public IClass getParentClass() {
		return null;
	}

	public ImmutableSet<IClass> getReferencedClasses() {
		return new ImmutableSet<IClass>();
	}

	public IType getSuperClass() {
		return null;
	}

}
