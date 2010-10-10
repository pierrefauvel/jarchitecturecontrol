package org.jarco.control.specifications.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.jarco.code.external.IClass;
import org.jarco.code.external.ICodeElement;
import org.jarco.code.external.IField;
import org.jarco.code.external.IMethod;
import org.jarco.collections.ImmutableMap;
import org.jarco.control.specifications.FromXmlFactory;
import org.jarco.control.specifications.itf.IPredicate;
import org.jarco.control.specifications.model.FM.kind;
import org.jarco.swing.IExposableAsANode;
import org.w3c.dom.Element;

//TODO V2 Enrichir les annotations avec une expression calculée
public class AnnotationPredicate<T extends ICodeElement> implements IPredicate<T>, IExposableAsANode {

	@FM(kind=kind.component)
	private String annotationClassName;
	
	//pour l'éditeur graphique
	public AnnotationPredicate()
	{
	}
	
	
	public AnnotationPredicate(String annotationClassName)
	{
		this.annotationClassName = annotationClassName;
	}
	
	public ImmutableMap<String, String> include(T t) {
		if(t instanceof IClass)
		{
			IClass c = (IClass)t;
			if(c.getAnnotations().getByName(annotationClassName)!=null)
			{
				return new ImmutableMap<String,String>(new HashMap<String,String>());
			}
			return null;
		}
		else if(t instanceof IField)
		{
			IField f = (IField)t;
			if(f.getAnnotations().getByName(annotationClassName)!=null)
			{
				return new ImmutableMap<String,String>(new HashMap<String,String>());
			}
			return null;
		}
		else if(t instanceof IMethod)
		{
			IMethod m = (IMethod)t;
			if(m.getAnnotations().getByName(annotationClassName)!=null)
			{
				return new ImmutableMap<String,String>(new HashMap<String,String>());
			}
			return null;
		}
		else
			throw new RuntimeException("Unexpected type "+t.getClass()+" in AnnotationFilter");
	}

	public String toString()
	{
		return "annotated @"+annotationClassName;
	}
	
	public String toLabel()
	{
		return "<b>Annotation</b>="+annotationClassName+"";
	}
	
	public String toXml()
	{
		return "<annotation-filter annotation-class-name=\""+annotationClassName+"\" />";
	}
	
	public static AnnotationPredicate fromXml(FromXmlFactory f,Element e)
	{
		String acn = e.getAttribute("annotation-class-name");
		return new AnnotationPredicate(acn);
	}
}
