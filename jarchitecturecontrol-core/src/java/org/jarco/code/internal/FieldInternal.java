package org.jarco.code.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jarco.code.external.EModifier;
import org.jarco.code.external.IAnnotation;
import org.jarco.code.external.IClass;
import org.jarco.code.external.ICodeRepository;
import org.jarco.code.external.IField;
import org.jarco.code.external.IType;
import org.jarco.collections.ImmutableList;
import org.jarco.collections.ImmutableNamedMap;
import org.jarco.collections.ImmutableSet;

public class FieldInternal extends ACodeElementInternal implements IField {
	
	private IClass parentClass;
	private String name;
	private IType type;
	private Set<EModifier> mod;
	private Field reflectPeer;
//	private List<IClass> ct;
	
	FieldInternal(ICodeRepository repo,IClass parentClass, /*String name, IClass type, Set<EModifier> mod, */Field reflectPeer/*, List<IClass> concreteTypes*/)
	{
		super(repo);
		this.parentClass=parentClass;
		this.name=reflectPeer.getName();
		this.type=wrap(reflectPeer.getGenericType());
		this.mod=wrapModifiers(reflectPeer.getModifiers());
		this.reflectPeer=reflectPeer;
	}
	
	public IType getFieldType() {
		return type;
	}

	public ImmutableSet<EModifier> getModifiers() {
		return new ImmutableSet<EModifier>(mod);
	}

	public String getName() {
		return name;
	}

	public IClass getParentClass() {
		return parentClass;
	}

	public String toString()
	{
		return "Field "+name+" of type "+type+" modifiers "+mod;
	}
	
	public boolean equals(Object o){
		IField other = (IField)o;
		return getParentClass().equals(other.getParentClass()) && name.compareTo(other.getName())==0;
	}
	public int hashCode(){
		return name.hashCode();
	}
	public ImmutableNamedMap<IAnnotation> getAnnotations() {
		try
		{
			List<IAnnotation> rc=new ArrayList<IAnnotation>();
			for(Annotation a:reflectPeer.getDeclaredAnnotations())
			{
				rc.add(new AnnotationInternal(a));
			}
			return new ImmutableNamedMap<IAnnotation>(rc);
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			return null;
		}
	}

//	public FList<IClass> getActualTypeArguments() {
//		return new FList<IClass>(ct);
//	}

}
