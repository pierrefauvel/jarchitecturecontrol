package org.jarco.code.internal;

import java.lang.annotation.Annotation;

import org.jarco.code.external.IAnnotation;

public class AnnotationInternal implements IAnnotation {

	private Annotation a;
	private Class type;
	public AnnotationInternal(Annotation a)
	{
		this.a=a;
		type = a.annotationType();
	}
	
	public String getName() {
		return type.getCanonicalName();
	}
	
	public String toString(){
		return "Annotation "+getName();
	}
	
	public String toLabel(){
		return "<b>@</b>"+type.getPackage().getName()+".<b>"+type.getSimpleName()+"</b>";
	}

}
