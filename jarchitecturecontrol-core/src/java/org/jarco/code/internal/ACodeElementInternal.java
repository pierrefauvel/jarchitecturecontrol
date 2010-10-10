package org.jarco.code.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jarco.code.external.EModifier;
import org.jarco.code.external.IClass;
import org.jarco.code.external.ICodeElement;
import org.jarco.code.external.ICodeRepository;
import org.jarco.code.external.IMethod;
import org.jarco.code.external.IType;
import org.jarco.collections.ImmutableNamedSet;
import org.jarco.tags.external.ITag;

public abstract class ACodeElementInternal implements ICodeElement{

	private ICodeRepository repo;
	private Set<ITag> hs_tags = new HashSet<ITag>();
	
	protected ACodeElementInternal(ICodeRepository repo)
	{
		this.repo=repo;
	}
	
	public ICodeRepository getRepository()
	{
		return repo;
	}

	public void addTag(ITag tag)
	{
		if(hs_tags.contains(tag))
			throw new RuntimeException("PF62 Duplicated tag entry, adding "+tag);
		hs_tags.add(tag);
	}
	
	public ImmutableNamedSet<ITag> getTags() {
		return new ImmutableNamedSet<ITag>(hs_tags);
	}

	 IMethod wrap(IClass parentClass,Method m/*,Set<EModifier> mod*/) {
//		 System.out.println("\tPF67 "+m.getName());
		MethodInternal rc= new MethodInternal(
				getRepository(),
				parentClass,
//				m.getName(),
//				wrap(m.getReturnType()),
//				wrap(m.getParameterTypes()),
//				wrap2(m.getExceptionTypes()),
//				mod,
				m
		);
//		System.out.println("\tPF70 "+m.getName()+"/"+rc.getName());
		return rc;
	}
	//a reprendre
	//TODO V1 Attention au cinit de classe
	IMethod wrap(IClass parentClass,Constructor ctor/*,Set<EModifier> mod*/) {
		return new MethodInternal(
				getRepository(),
				parentClass,
//				"<init>",
//				wrap(Void.class),
//				wrap(ctor.getParameterTypes()),
//				wrap2(ctor.getExceptionTypes()),
//				mod,
				ctor
		);
	}

	List<IType> wrap(Class<?>[] classes) {
		List<IType> rc=new ArrayList<IType>();
		for(Class ci:classes)
		{
			rc.add(wrap(ci));
		};
		return rc;
	}
	Set<IType> wrap2(Class<?>[] classes) {
		Set<IType> rc=new HashSet<IType>();
		for(Class ci:classes)
		{
			rc.add(wrap(ci));
		};
		return rc;
	}
	static Set<EModifier> wrapModifiers(int modifiers) {
		Set<EModifier> rc = new HashSet<EModifier>();
		if(Modifier.isAbstract(modifiers)) rc.add(EModifier._abstract);
		if(Modifier.isFinal(modifiers)) rc.add(EModifier._final);
		if(Modifier.isInterface(modifiers)) rc.add(EModifier._interface);
		if(Modifier.isNative(modifiers)) rc.add(EModifier._native);
		if(Modifier.isPrivate(modifiers)) rc.add(EModifier._private);
		if(Modifier.isProtected(modifiers)) rc.add(EModifier._protected);
		if(Modifier.isPublic(modifiers)) rc.add(EModifier._public);
		if(Modifier.isStatic(modifiers)) rc.add(EModifier._static);
		if(Modifier.isStrict(modifiers)) rc.add(EModifier._strict);
		if(Modifier.isSynchronized(modifiers)) rc.add(EModifier._synchronized);
		if(Modifier.isTransient(modifiers)) rc.add(EModifier._transient);
		if(Modifier.isVolatile(modifiers)) rc.add(EModifier._volatile);
		return rc;
	}

	IType wrap(Class t)
	{
		if(t.isPrimitive())
		{
			if(Integer.TYPE.isAssignableFrom(t))
				return TypeInternal.newPrimitive(repo,wrapBaseClass(Integer.class));
			if(Long.TYPE.isAssignableFrom(t))
				return TypeInternal.newPrimitive(repo,wrapBaseClass(Long.class));
			if(Double.TYPE.isAssignableFrom(t))
				return TypeInternal.newPrimitive(repo,wrapBaseClass(Double.class));
			if(Float.TYPE.isAssignableFrom(t))
				return TypeInternal.newPrimitive(repo,wrapBaseClass(Float.class));
			if(Boolean.TYPE.isAssignableFrom(t))
				return TypeInternal.newPrimitive(repo,wrapBaseClass(Boolean.class));
			if(Byte.TYPE.isAssignableFrom(t))
				return TypeInternal.newPrimitive(repo,wrapBaseClass(Byte.class));
			if(Short.TYPE.isAssignableFrom(t))
				return TypeInternal.newPrimitive(repo,wrapBaseClass(Short.class));
			if(Character.TYPE.isAssignableFrom(t))
				return TypeInternal.newPrimitive(repo,wrapBaseClass(Character.class));
			if(Void.TYPE.isAssignableFrom(t))
				return TypeInternal.newPrimitive(repo,wrapBaseClass(Void.class));
			throw new RuntimeException("PF14 Unexpected primitive type "+t);
		}
		else if(t.isArray())
		{
			//TODO V1.2: Dimension des tableaux
			int dimension = 1;
			
			IType tc = wrap(t.getComponentType());
			
			return TypeInternal.newArray(repo,tc.getBaseClass(),tc,dimension);
		}
		else 
		{
			return TypeInternal.newInstance(repo,wrapBaseClass(t));
		}
	}

	private IClass wrapBaseClass(Class t)
	{
		IClass ft = getRepository().getClassByName(t.getName());
		if(ft!=null)
			return ft;
		throw new RuntimeException("PF15 can't wrap "+t.getName());
	}
		protected IType wrap(Type genericType) {
			
		    if(genericType instanceof GenericArrayType)
		    {
		    	Type gt = ((GenericArrayType)genericType).getGenericComponentType();
		    	if(gt instanceof Class)
		    	{
		    		if(((Class)gt).isPrimitive())
		    		{
		    			return TypeInternal.newPrimitiveArray(repo, wrap((Class)gt), 1);
		    		}
		    		else
		    		{
		    			return TypeInternal.newArray(repo,(gt instanceof Class ? wrapBaseClass((Class)gt) : null),wrap(gt),1);
		    		}
		    	}
		    	else
		    		return TypeInternal.newArray(repo,null,wrap(gt),1);
		    }
		    else if( genericType instanceof ParameterizedType)
		    {
				ParameterizedType pt = (ParameterizedType)genericType;
				Type[] ata = pt.getActualTypeArguments();
				List<IType> ct = new ArrayList<IType>();
				for(Type ti:ata)
				{
					ct.add(wrap(ti));
				}
				Type rt = pt.getRawType();
				IClass rtc= (rt instanceof Class ? wrapBaseClass((Class)rt) : null);
		    	return TypeInternal.newParameterized(repo,rtc,wrap(pt.getRawType()),ct);
		    }
		    else if( genericType instanceof TypeVariable)
		    {
		    	return TypeInternal.newInstance(repo,((CodeRepositoryInternal)repo).getAnyClass()); //TODO V1.2 Gerer les TypeVariable
		    }
		    else if (genericType instanceof WildcardType)
		    {
		    	return TypeInternal.newInstance(repo,((CodeRepositoryInternal)repo).getAnyClass()); //TODO V1.2 Gérer les wildcards
		    }
		    else if (genericType instanceof Class)
		    {
		    	return wrap((Class)genericType);
		    }
		    throw new RuntimeException("Could not wrap "+genericType+ "instanceof "+genericType.getClass());
		}
		 List<IType> wrap(Type[] genericTypes) {
			 List<IType> rc = new ArrayList<IType>();
			 for(Type t:genericTypes)
				 rc.add(wrap(t));
			 return rc;
			}
		 Set<IType> wrap2(Type[] genericTypes) {
			 Set<IType> rc = new HashSet<IType>();
			 for(Type t:genericTypes)
				 rc.add(wrap(t));
			 return rc;
			}


}
