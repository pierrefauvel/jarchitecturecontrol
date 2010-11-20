package org.jarco.code.internal;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.GETSTATIC;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.bcel.generic.PUTSTATIC;
import org.apache.bcel.generic.ReferenceType;
//import org.apache.bcel.generic.Type;
import org.jarco.code.external.EModifier;
import org.jarco.code.external.IAnnotation;
import org.jarco.code.external.IClass;
import org.jarco.code.external.ICodeRepository;
import org.jarco.code.external.IField;
import org.jarco.code.external.IMethod;
import org.jarco.code.external.IType;
import org.jarco.code.internal.bcel.BCELUtilities;
import org.jarco.collections.ImmutableList;
import org.jarco.collections.ImmutableNamedMap;
import org.jarco.collections.ImmutableSet;

//TODO V1.1 EXTERNALISER LE CODE BCEL pour MethodInternal

public class MethodInternal extends ACodeElementInternal implements IMethod {

	private final IClass parentClass;
	private final String name;
	private final IType returnType;
	private final List<IType> parameterTypes;
	private final Set<IType> exceptionTypes;
	private final Set<EModifier> mod;
	private Method bcelPeer;
	
	private java.lang.reflect.Method methodReflectPeer;
	private java.lang.reflect.Constructor constructorReflectPeer;

	//TODO V1.2 Mettre une SOFT REFERENCE (cf bcelPeer sur les Classes)
	private Instruction[] bcelInstructions;
	private ConstantPoolGen bcelConstantPool;
	
//	private MethodImpl(ICodeRepository repo, IClass parentClass,String name/*, IType returnType,
//			List<IType> parameterTypes, Set<IType> exceptionThrown, Set<EModifier> mod*/) 
//	{
//		super(repo);
//		this.parentClass=parentClass;
//		this.name=name;
//		System.out.println("\tPF68 "+name);
//		if(returnType==null)
//			throw new RuntimeException("PF13 null return type for method "+parentClass+" "+name);
//		this.returnType=returnType;
//		this.parameterTypes=parameterTypes;
//		this.exceptionThrown=exceptionThrown;
//		this.mod=mod;
//	}
	
	public MethodInternal(ICodeRepository repo, IClass parentClass, /*String name, IType returnType,
			List<IType> parameterTypes, Set<IType> exceptionThrown, Set<EModifier> mod,*/
			java.lang.reflect.Method peer) 
	{
		super(repo);
		this.parentClass=parentClass;
		this.name=peer.getName();
		returnType=wrap(peer.getGenericReturnType());
		parameterTypes=wrap(peer.getGenericParameterTypes());
		exceptionTypes=wrap2(peer.getGenericExceptionTypes());
		mod=ACodeElementInternal.wrapModifiers(peer.getModifiers());
		methodReflectPeer=peer;
	}
	
	public MethodInternal(ICodeRepository repo, IClass parentClass, /*String name, IType returnType,
			List<IType> parameterTypes, Set<IType> exceptionThrown, Set<EModifier> mod,*/
			java.lang.reflect.Constructor peer) 
	{
		super(repo);
		this.parentClass=parentClass;
		this.name="<init>";
		returnType=wrap(Void.class);
		parameterTypes=wrap(peer.getGenericParameterTypes());
		exceptionTypes=wrap2(peer.getGenericExceptionTypes());
		mod=ACodeElementInternal.wrapModifiers(peer.getModifiers());
		constructorReflectPeer=peer;
	}

	private void analyzeBcel() 
	{
		try
		{
			if(bcelPeer==null)
			{
				JavaClass jc = ((ClassInternal)parentClass).getPeer();
				if(methodReflectPeer!=null)
					bcelPeer = jc.getMethod(methodReflectPeer);
				else
				{
					System.err.println("PF65 analyzeBcel called on a constructor, access to constructor byte code not yet implemented");
					return;
				}
				if((methodReflectPeer.getModifiers() & Modifier.ABSTRACT)!=0)
				{
					bcelInstructions = new Instruction[]{};
				}
				else
				{
					if(bcelPeer==null)
						throw new RuntimeException("PF12 could not find bcelPeer for "+methodReflectPeer);
					if(bcelPeer.getCode()==null)
						throw new RuntimeException("PF12 could not find code for "+methodReflectPeer);
		            InstructionList il = new InstructionList(bcelPeer.getCode().getCode());
		            bcelInstructions=il.getInstructions();
		            bcelConstantPool = new ConstantPoolGen(bcelPeer.getConstantPool());
				};
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
	
	public ImmutableSet<IType> getExceptionThrown() {
		return new ImmutableSet<IType>(exceptionTypes);
	}

	public ImmutableSet<EModifier> getModifiers() {
		return new ImmutableSet<EModifier>(mod);
	}

	public String getName() {
		return name;
	}

	public ImmutableList<IType> getParameterTypes() {
		return new ImmutableList<IType>(parameterTypes);
	}

	public IClass getParentClass() {
		return parentClass;
	}

	public IType getReturnType() {
		return returnType;
	}

	private String _toString;
	
	public String toString(){
		if(_toString!=null) return _toString;
		StringBuffer sb=new StringBuffer();
		sb.append("Method ");
		sb.append(name);
		sb.append(" returns ");
		sb.append(getReturnType());
		sb.append(" parameters ");
		sb.append(getParameterTypes());
		sb.append(" throws ");
		sb.append(getExceptionThrown());
		sb.append(" modifiers ");
		sb.append(mod);
		_toString = sb.toString();
		return _toString;
	}

	public int hashCode()
	{
		return toString().hashCode();
	}
	
	public boolean equals(Object obj)
	{
		MethodInternal mi = (MethodInternal)obj;
		return mi.toString().equals(toString());
	}
	
	public ImmutableSet<IClass> getInstanciatesClasses() {
		analyzeBcel();
		Set<IClass> s = new HashSet<IClass>();
		for(Instruction ii : bcelInstructions)
		{
            if(ii instanceof NEW)
            {
            NEW x = (NEW)ii;
            ObjectType ot = x.getLoadClassType(bcelConstantPool);
            String cn = ot.getClassName();
            ClassInternal ci = (ClassInternal)(this.getParentClass());
            IClass c = ci.getProject().getRepository().getClassByName(cn);
            s.add(c);
            }
		}
		return new ImmutableSet<IClass>(s);
	}
	

	private void failToResolveMethod(String cn, String mn, String[] pt, CouldNotResolveMethodException ex)
	{
    	System.err.print("PF32 Could not resolve method "+cn+"."+mn+"(");
    	boolean first=true;
    	for(String pti:pt)
    	{
    		System.err.print((first ? "" : ", ") + pti);
    		first=false;
    	};
    	System.err.println(") called by "+this.getParentClass().getLongName()+"."+this.getName());
    	ex.printStackTrace(System.err);
	}
	
	public ImmutableSet<IMethod> getInvokesMethods() {
		analyzeBcel();
		Set<IMethod> s = new HashSet<IMethod>();
        ICodeRepository repository = getRepository();
		loop_instruction : for(Instruction ii : bcelInstructions)
		{
			try
			{
	            if(ii instanceof INVOKEINTERFACE)
	            {
	                INVOKEINTERFACE x = (INVOKEINTERFACE)ii;
	                String mn=x.getMethodName(bcelConstantPool);
	                String cn = ((ObjectType)x.getReferenceType(bcelConstantPool)).getClassName();
	                String[] pt = BCELUtilities.parseSignatureExcludingReturnType(x.getSignature(bcelConstantPool));
	                IClass targetClass = repository.getClassByName(cn);
	                try
	                {
	                IMethod targetMethod = targetClass.getMethodByNameAndTypes(mn,pt);
	                s.add(targetMethod);
	                }
	                catch(CouldNotResolveMethodException ex)
	                {
	                	if(targetClass.getParentClass()!=null && mn.compareTo("<init>")==0)
	                	{
	                		//Cas où le compilateur rajoute des arguments au constructeur
	                		ImmutableList<IMethod> m= targetClass.getMethodByName(mn);
	                		if(m.count()==1)
	                		{
	                			s.add(m.get(0));
	                		}
	                		else
	                		{
	                			//TODO V2 Gérer les paramètres synthétiques des constructeurs pour les inner-classes
	                			System.err.println("PF65 Could not resolve inner-class constructor in "+targetClass);
	                		}
	                	}
	                	else
	                	{
	                		ImmutableList<IMethod> m= targetClass.getMethodByName(mn);
	                		if(m.count()==1)
	                		{
	                			s.add(m.get(0));
	                		}
	                		else
	                		{
		                		failToResolveMethod(cn,mn,pt,ex);
	                		}
	                		
	                	}
	                }
	            }
	            else if(ii instanceof INVOKESPECIAL)
	            {
	                INVOKESPECIAL x = (INVOKESPECIAL)ii;
	                String mn=x.getMethodName(bcelConstantPool);
	                String cn = ((ObjectType)x.getReferenceType(bcelConstantPool)).getClassName();
	                String[] pt = BCELUtilities.parseSignatureExcludingReturnType(x.getSignature(bcelConstantPool));
	                IClass targetClass = repository.getClassByName(cn);
	                try
	                {
	                IMethod targetMethod = targetClass.getMethodByNameAndTypes(mn,pt);
	                s.add(targetMethod);
	                }
	                catch(CouldNotResolveMethodException ex)
	                {
	                	if(targetClass.getParentClass()!=null && mn.compareTo("<init>")==0)
	                	{
		                		//Cas où le compilateur rajoute des arguments au constructeur
		                		ImmutableList<IMethod> m= targetClass.getMethodByName(mn);
		                		if(m.count()==1)
		                		{
		                			s.add(m.get(0));
		                		}
		                		else
		                		{
		                			//TODO V2 Gérer les paramètres synthétiques des constructeurs pour les inner-classes
		                			System.err.println("PF65 Could not resolve inner-class constructor in "+targetClass);
		                		}
	                	}
	                	else
	                	{
	                		ImmutableList<IMethod> m= targetClass.getMethodByName(mn);
	                		if(m.count()==1)
	                		{
	                			s.add(m.get(0));
	                		}
	                		else
	                		{
		                		failToResolveMethod(cn,mn,pt,ex);
	                		}
	                		
	                	}
	                }
	            }
	            else if(ii instanceof INVOKESTATIC)
	            {
	                INVOKESTATIC x = (INVOKESTATIC)ii;
	                String mn=x.getMethodName(bcelConstantPool);
	                String cn = ((ObjectType)x.getReferenceType(bcelConstantPool)).getClassName();
	                String[] pt = BCELUtilities.parseSignatureExcludingReturnType(x.getSignature(bcelConstantPool));
	                IClass targetClass = repository.getClassByName(cn);
	                try
	                {
	                IMethod targetMethod = targetClass.getMethodByNameAndTypes(mn,pt);
	                s.add(targetMethod);
	                }
	                catch(CouldNotResolveMethodException ex)
	                {
	                	if(targetClass.getParentClass()!=null && mn.compareTo("<init>")==0)
	                	{
	                		//Cas où le compilateur rajoute des arguments au constructeur
	                		ImmutableList<IMethod> m= targetClass.getMethodByName(mn);
	                		if(m.count()==1)
	                		{
	                			s.add(m.get(0));
	                		}
	                		else
	                		{
	                			//TODO V2 Gérer les paramètres synthétiques des constructeurs pour les inner-classes
	                			System.err.println("PF65 Could not resolve inner-class constructor in "+targetClass);
	                		}
	                	}
	                	else
	                	{
	                		ImmutableList<IMethod> m= targetClass.getMethodByName(mn);
	                		if(m.count()==1)
	                		{
	                			s.add(m.get(0));
	                		}
	                		else
	                		{
		                		failToResolveMethod(cn,mn,pt,ex);
	                		}
	                		
	                	}
	                }
	            }
	            else if(ii instanceof INVOKEVIRTUAL)
	            {
	                INVOKEVIRTUAL x = (INVOKEVIRTUAL)ii;
	                String mn=x.getMethodName(bcelConstantPool);
	                ReferenceType rt = x.getReferenceType(bcelConstantPool);
	                if(!(rt instanceof ObjectType))
	                {
	                	if(rt instanceof ArrayType)
	                	{
	                		//TODO V1.1 Quelle est la sémantique exacte de invoke virtual sur un array ?
		                	continue loop_instruction;
	                	}
	                	else
	                	{
	                		//TODO V1.1 Quelle est la sémantique exacte de invoke virtual si la reference n'est ni un ObjectType ni un ArrayType ?
		                	System.err.println("PF31 Found "+rt.getClass()+" (invoke virtual)");
		                	continue loop_instruction;
	                	}
	                }
	                String cn = ((ObjectType)rt).getClassName();
	                String[] pt = BCELUtilities.parseSignatureExcludingReturnType(x.getSignature(bcelConstantPool));
	                IClass targetClass = repository.getClassByName(cn);
	                try
	                {
	                IMethod targetMethod = targetClass.getMethodByNameAndTypes(mn,pt);
	                s.add(targetMethod);
	                }
	                catch(CouldNotResolveMethodException ex)
	                {
	                	if(targetClass.getParentClass()!=null && mn.compareTo("<init>")==0)
	                	{
	                		//Cas où le compilateur rajoute des arguments au constructeur
	                		ImmutableList<IMethod> m= targetClass.getMethodByName(mn);
	                		if(m.count()==1)
	                		{
	                			s.add(m.get(0));
	                		}
	                		else
	                		{
	                			//TODO V2 Gérer les paramètres synthétiques des constructeurs pour les inner-classes
	                			System.err.println("PF65 Could not resolve inner-class constructor in "+targetClass);
	                		}
	                	}
	                	else
	                	{
	                		ImmutableList<IMethod> m= targetClass.getMethodByName(mn);
	                		if(m.count()==1)
	                		{
	                			s.add(m.get(0));
	                		}
	                		else
	                		{
		                		failToResolveMethod(cn,mn,pt,ex);
	                		}
	                		
	                	}
	                }
	            }
			}
			catch(CouldNotResolveMethodException ex)
			{
				ex.printStackTrace();
				continue loop_instruction;
			}
		}
		return new ImmutableSet<IMethod>(s);
	}

	public ImmutableSet<IField> getReadsFields() {
		analyzeBcel();
		Set<IField> s = new HashSet<IField>();
		for(Instruction ii : bcelInstructions)
		{
            if(ii instanceof GETFIELD)
            {
                GETFIELD x = (GETFIELD)ii;
                String fn = x.getName(bcelConstantPool);
                String cn = ((ObjectType)x.getReferenceType(bcelConstantPool)).getClassName();
                ClassInternal ci = (ClassInternal)(this.getParentClass());
                IClass c = ci.getRepository().getClassByName(cn);
                IField f = c.getFieldByName(fn);
                if(f==null)
                	System.err.println("PF15 could not find field "+fn+" in "+cn);
                s.add(f);
            }
            else if(ii instanceof GETSTATIC)
            {
                GETSTATIC x = (GETSTATIC)ii;
                String fn = x.getName(bcelConstantPool);
                String cn = ((ObjectType)x.getReferenceType(bcelConstantPool)).getClassName();
                ClassInternal ci = (ClassInternal)(this.getParentClass());
                IClass c = ci.getRepository().getClassByName(cn);
                IField f = c.getFieldByName(fn);
                if(f==null)
                	System.err.println("PF15 could not find field "+fn+" in "+cn);
                s.add(f);
            }
		}
		return new ImmutableSet<IField>(s);
	}

	public ImmutableSet<IField> getWritesFields() {
		analyzeBcel();
		Set<IField> s = new HashSet<IField>();
		for(Instruction ii : bcelInstructions)
		{
            if(ii instanceof PUTFIELD)
            {
            	PUTFIELD x = (PUTFIELD)ii;
                String fn = x.getName(bcelConstantPool);
                String cn = ((ObjectType)x.getReferenceType(bcelConstantPool)).getClassName();
                ClassInternal ci = (ClassInternal)(this.getParentClass());
                IClass c = ci.getRepository().getClassByName(cn);
                IField f = c.getFieldByName(fn);
                if(f==null)
                	System.err.println("PF15 could not find field "+fn+" in "+cn);
                s.add(f);
            }
            else if(ii instanceof PUTSTATIC)
            {
                PUTSTATIC x = (PUTSTATIC)ii;
                String fn = x.getName(bcelConstantPool);
                String cn = ((ObjectType)x.getReferenceType(bcelConstantPool)).getClassName();
                ClassInternal ci = (ClassInternal)(this.getParentClass());
                IClass c = ci.getRepository().getClassByName(cn);
                IField f = c.getFieldByName(fn);
                if(f==null)
                	System.err.println("PF15 could not find field "+fn+" in "+cn);
                s.add(f);
            }
		}
		return new ImmutableSet<IField>(s);
	}

	public ImmutableNamedMap<IAnnotation> getAnnotations() {
		try
		{
			List<IAnnotation> rc=new ArrayList<IAnnotation>();
			if(methodReflectPeer!=null)
			for(Annotation a:methodReflectPeer.getDeclaredAnnotations())
			{
				rc.add(new AnnotationInternal(a));
			}
			if(constructorReflectPeer!=null)
				for(Annotation a:constructorReflectPeer.getDeclaredAnnotations())
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
/*
	public FList<IClass> getActualTypeArgumentsInParameters() {
		List<IClass> ct = new ArrayList<IClass>();
		Type[] pts = reflectPeer.getGenericParameterTypes();
		for(Type pti: pts)
		{
			if(pti instanceof ParameterizedType)
			{
				ParameterizedType pt = (ParameterizedType)pti;
				Type[] ata = pt.getActualTypeArguments();
				for(Type ti:ata)
				{
					if(ti instanceof Class)
					{
					ct.add(wrap((Class)ti));
					}
				}
			}
		}
		return new FList<IClass>(ct);
	}

	public FList<IClass> getActualTypeArgumentsInReturn() {
		List<IClass> ct = new ArrayList<IClass>();
		Type rt = reflectPeer.getGenericReturnType();
		if(rt instanceof ParameterizedType)
		{
			ParameterizedType pt = (ParameterizedType)rt;
			Type[] ata = pt.getActualTypeArguments();
			for(Type ti:ata)
			{
				if(ti instanceof Class)
				{
				ct.add(wrap((Class)ti));
				}
			}
		}
		return new FList<IClass>(ct);
	}

	public FList<IClass> getActualTypeArgumentsInExceptions() {
		List<IClass> ct = new ArrayList<IClass>();
		Type[] ets = reflectPeer.getGenericExceptionTypes();
		for(Type et: ets)
		{
			if(et instanceof ParameterizedType)
			{
				ParameterizedType pt = (ParameterizedType)et;
				Type[] ata = pt.getActualTypeArguments();
				for(Type ti:ata)
				{
					if(ti instanceof Class)
					{
					ct.add(wrap((Class)ti));
					}
				}
			}
		}
		return new FList<IClass>(ct);
	}
*/	
}
