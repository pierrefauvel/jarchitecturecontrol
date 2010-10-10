package org.jarco.control.report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.jarco.code.external.EModifier;
import org.jarco.code.external.IAnnotation;
import org.jarco.code.external.IClass;
import org.jarco.code.external.IField;
import org.jarco.code.external.IMethod;
import org.jarco.code.external.IProject;
import org.jarco.code.external.IPropertiesDocument;
import org.jarco.code.external.IType;
import org.jarco.code.external.IXmlDocument;
import org.jarco.code.internal.ClassInternal;
import org.jarco.collections.ImmutableList;
import org.jarco.collections.ImmutableNamedList;
import org.jarco.collections.ImmutableSet;

public class AnalysisReport {
	public static void report(File dirpath,IProject project) throws IOException
	{
		File project_dir = new File(dirpath.getCanonicalPath()+File.separator+File.separator+project.getName());
		project_dir.mkdirs();
		for(IClass clazz : project.getClasses())
		{
			File package_dir = new File(project_dir.getCanonicalPath()+File.separator+clazz.getPackage().getName().replace(".", File.separator));
			package_dir.mkdirs();
			File class_file = new File(package_dir.getCanonicalPath()+File.separator+clazz.getName()+".ja2analysis");
			FileWriter fw=new FileWriter(class_file);
			PrintWriter pw=new PrintWriter(fw);
			pw.println(((ClassInternal)clazz).toLongString());
			try
			{
			if(clazz.getParentClass()!=null)
			{
				pw.println("\tParent "+clazz.getParentClass());
			}
//			for(IClass c : clazz.getActualTypeArgumentsInSuperclass())
//			{
//				pw.println("\tActualTypeArgumentsInSuperclass "+c);
//			}
//			((ClassImpl)clazz).analyzeViaReflection();
			if(clazz.getSuperClass()!=null)
				pw.println("\tExtends "+clazz.getSuperClass());
			for(IType itf:clazz.getInterfaces())
			{
				pw.println("\tImplements "+itf);
			}
			for(IAnnotation a:clazz.getAnnotations())
			{
				pw.println("\tAnnotated "+a);
			}
			for(EModifier m:clazz.getModifiers())
			{
				pw.println("\tModifier "+m);
			}
			for(IClass c:clazz.getInnerClasses())
			{
				pw.println("\tContains "+c);
			}
			ImmutableNamedList<IField> f = clazz.getDeclaredFields();
			for(IField fi:f)
			{
				pw.println("\t"+fi);
//				for(IClass c : fi.getActualTypeArguments())
//				{
//					pw.println("\t\tActualTypeArguments "+c);
//				}
				for(IAnnotation a:fi.getAnnotations())
				{
					pw.println("\t\tAnnotated "+a);
				}
				for(EModifier m:fi.getModifiers())
				{
					pw.println("\t\tModifier "+m);
				}
			}
//			((ClassImpl)clazz).analyzeViaBCEL();
			ImmutableSet<IClass> refs = clazz.getReferencedClasses();
			for(IClass ref:refs)
				pw.println("\tReferences "+ref);
			ImmutableList<IMethod> methods = clazz.getDeclaredMethods();
			for(IMethod mi:methods)
			{
				pw.println("\t"+mi);
				for(IAnnotation a:mi.getAnnotations())
				{
					pw.println("\t\tAnnotated "+a);
				}
				for(EModifier m:mi.getModifiers())
				{
					pw.println("\t\tModifier "+m);
				}
				for(IField fi:mi.getReadsFields())
				{
					pw.println("\t\tReads "+fi);
				}
				for(IField fi:mi.getWritesFields())
				{
					pw.println("\t\tWrites "+fi);
				}
				for(IClass ci:mi.getInstanciatesClasses())
				{
					pw.println("\t\tInstantiates "+ci);
				}
					for(IMethod mii:mi.getInvokesMethods())
					{
						pw.println("\t\tInvokes "+mii.getParentClass()+"."+mii);
					}
//				for(IClass c : mi.getActualTypeArgumentsInParameters())
//				{
//					pw.println("\t\tActualTypeArgumentsInParameters "+c);
//				}
//				for(IClass c : mi.getActualTypeArgumentsInReturn())
//				{
//					pw.println("\t\tActualTypeArgumentsInReturn "+c);
//				}
//				for(IClass c : mi.getActualTypeArgumentsInExceptions())
//				{
//					pw.println("\t\tActualTypeArgumentsInExceptions "+c);
//				}
				}
			}
			catch(Throwable t)
			{
				t.printStackTrace(pw);
				System.err.println("PF105 Error during AnalysisReport of "+clazz.getName()+":"+t.getClass().getSimpleName()+":"+t.getMessage());
			}
			finally
			{
			pw.close();
			}
		}
		File documents_file = new File(project_dir.getCanonicalPath()+File.separator+"_Documents.ja2");
		FileWriter fw=new FileWriter(documents_file);
		PrintWriter pw=new PrintWriter(fw);
		for(IXmlDocument xdi: project.getXmlDocuments())
		{
			pw.println("XmlDocument "+xdi.getName());
		}
		for(IPropertiesDocument pdi: project.getPropertiesDocuments())
		{
			pw.println("PropertiesDocument "+pdi.getName());
		}
		pw.close();
	}
}
