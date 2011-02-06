package org.jarco.control.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.transform.TransformerException;

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
import org.jarco.control.report.genericreport.FormattedText;
import org.jarco.control.report.genericreport.GenericReport;

public class AnalysisReport {
	
		
	public static final String INVOKES_METHOD = "invokes-method";
	public static final String INSTANTIATES_CLASS = "instantiates-class";
	public static final String WRITES_FIELD = "writes-field";
	public static final String READS_FIELD = "reads-field";
	public static final String EXCEPTION_THROWN = "exception-thrown";
	public static final String PARAMETER_TYPE = "parameter-type";
	public static final String RETURN_TYPE = "return-type";
	public static final String REFERENCED_CLASS = "referenced-class";
	public static final String FIELD_TYPE = "field-type";
	public static final String DECLARED_FIELD = "declared-field";
	public static final String INNER_CLASS = "inner-class";
	public static final String MODIFIER = "modifier";
	public static final String ANNOTATION = "annotation";
	public static final String INTERFACE = "interface";
	public static final String SUPER_CLASS = "super-class";
	public static final String PARENT_CLASS = "parent-class";
	public static final String DECLARED_METHOD = "method";
	public static final String DECLARED_CONSTRUCTOR = "constructor";

	public static void report(File dirpath,IProject project) throws IOException, TransformerException
	{

		File project_dir = new File(dirpath.getCanonicalPath()+File.separator+File.separator+project.getName());
		project_dir.mkdirs();
		for(IClass clazz : project.getClasses())
		{
			File package_dir = new File(project_dir.getCanonicalPath()+File.separator+clazz.getPackage().getName().replace(".", File.separator));
			package_dir.mkdirs();
			File class_file = new File(package_dir.getCanonicalPath()+File.separator+clazz.getName()+".analysis.class.ja2.xml");
			GenericReport pw = new GenericReport("class-analysis", new FormattedText("Class analysis"),new FormattedText(((ClassInternal)clazz).toString()));
			try
			{
			if(clazz.getParentClass()!=null)
			{
				pw.push(PARENT_CLASS, new FormattedText("<b>Parent:</b>"+clazz.getParentClass().getLongName()), null);
				pw.pop();
			}
			if(clazz.getSuperClass()!=null)
			{
				pw.push(SUPER_CLASS, new FormattedText("<b>Superclass:</b>"+clazz.getSuperClass().getLongName()), null);
				pw.pop();
			}
			for(IType itf:clazz.getInterfaces())
			{
				pw.push(INTERFACE, new FormattedText("<b>Implements:</b>"+itf.getLongName()), null);
				pw.pop();
			}
			//TODO V1.0 enrichir les annotation
			for(IAnnotation a:clazz.getAnnotations())
			{
				pw.push(ANNOTATION, new FormattedText("<b>Annotated:</b>"+a.toString()), null);
				pw.pop();
			}
			for(EModifier m:clazz.getModifiers())
			{
				pw.push(MODIFIER, new FormattedText("<b>Modifier:</b>"+m.toString()), null);
				pw.pop();
			}
			for(IClass c:clazz.getInnerClasses())
			{
				pw.push(INNER_CLASS, new FormattedText("<b>Contains:</b>"+c.getName()), null);
				pw.pop();
			}
			ImmutableNamedList<IField> f = clazz.getDeclaredFields();
			for(IField fi:f)
			{
				pw.push(DECLARED_FIELD, new FormattedText(fi.getName()), null);
				pw.push(FIELD_TYPE, new FormattedText(fi.getFieldType().getLongName()), null);
				pw.pop();
				for(IAnnotation a:fi.getAnnotations())
				{
					pw.push(ANNOTATION, new FormattedText("<b>Annotated:</b>"+a.toString()), null);
					pw.pop();
				}
				for(EModifier m:fi.getModifiers())
				{
					pw.push(MODIFIER, new FormattedText("<b>Modifier:</b>"+m.toString()), null);
					pw.pop();
				}
				pw.pop();
			}
			for(IClass itf:clazz.getReferencedClasses())
			{
				pw.push(REFERENCED_CLASS, new FormattedText("<b>References:</b>"+itf.getLongName()), null);
				pw.pop();
			}
			ImmutableList<IMethod> methods = clazz.getDeclaredMethods();
			reportMethods(DECLARED_METHOD,methods,pw);
			ImmutableList<IMethod> constructors = clazz.getConstructors();
			reportMethods(DECLARED_CONSTRUCTOR,constructors,pw);
			}
			catch(Throwable t)
			{
				t.printStackTrace();
				System.err.println("PF105 Error during AnalysisReport of "+clazz.getName()+":"+t.getClass().getSimpleName()+":"+t.getMessage());
			}
			finally
			{
				FileOutputStream fos = new FileOutputStream(class_file);
			pw.write(fos);
			fos.close();
			}
		}
		File documents_file = new File(project_dir.getCanonicalPath()+File.separator+"resources.analysis.ja2.xml");
		GenericReport pw=new GenericReport("resources", new FormattedText("Resources"),null);
		for(IXmlDocument xdi: project.getXmlDocuments())
		{
			pw.push("xml-document", new FormattedText(xdi.getName()), null);
			pw.pop();
		}
		for(IPropertiesDocument pdi: project.getPropertiesDocuments())
		{
			pw.push("properties-document", new FormattedText(pdi.getName()), null);
			pw.pop();
		}
		FileOutputStream fos = new FileOutputStream(documents_file);
		pw.write(fos);
		fos.close();
	}

	private static void reportMethods(String kind,ImmutableList<IMethod> methods, GenericReport pw) {
		for(IMethod mi:methods)
		{
			//System.out.println("PF206 "+signature(mi));
			pw.push(kind, new FormattedText(signature(mi)), null);
			for(IAnnotation a:mi.getAnnotations())
			{
				pw.push(ANNOTATION, new FormattedText("<b>Annotated:</b>"+a.toString()), null);
				pw.pop();
			}
			for(EModifier m:mi.getModifiers())
			{
				pw.push(MODIFIER, new FormattedText("<b>Modifier:</b>"+m.toString()), null);
				pw.pop();
			}
			pw.push(RETURN_TYPE, new FormattedText(mi.getReturnType().getLongName()), null);
			pw.pop();
			for(IType pi:mi.getParameterTypes())
			{
				pw.push(PARAMETER_TYPE, new FormattedText(pi.getLongName()), null);
				pw.pop();
			}
			for(IType ei:mi.getExceptionThrown())
			{
				pw.push(EXCEPTION_THROWN, new FormattedText("<b>Throws:</b>"+ei.getLongName()), null);
				pw.pop();
			}
			
			for(IField fi:mi.getReadsFields())
			{
				pw.push(READS_FIELD, new FormattedText("<b>Reads:</b>"+fi.getParentClass().getLongName()+"."+fi.getName()), null);
				pw.pop();
			}
			for(IField fi:mi.getWritesFields())
			{
				pw.push(WRITES_FIELD, new FormattedText("<b>Writes:</b>"+fi.getParentClass().getLongName()+"."+fi.getName()), null);
				pw.pop();
			}
			for(IClass ci:mi.getInstanciatesClasses())
			{
				pw.push(INSTANTIATES_CLASS, new FormattedText("<b>Instantiates:</b>"+ci.getLongName()), null);
				pw.pop();
			}
				for(IMethod mii:mi.getInvokesMethods())
				{
					pw.push(INVOKES_METHOD, new FormattedText("<b>Invokes:</b>"+mii.getParentClass().getLongName()+"."+signature(mii)), null);
					pw.pop();
				}
				pw.pop();
			}
	}

	public static String signature(IMethod m) {
		StringBuffer sb=new StringBuffer();
		sb.append(m.getName());
		sb.append("(");
		boolean first=true;
		for(IType pt:m.getParameterTypes())
		{
			if(!first)
				sb.append(",");
			sb.append(pt.getLongName());
			first=false;
		}
		sb.append(")");
		return sb.toString();
	}
}
