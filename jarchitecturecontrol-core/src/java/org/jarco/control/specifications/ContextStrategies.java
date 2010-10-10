package org.jarco.control.specifications;

import java.util.List;

public class ContextStrategies {
	public static String resolve(String expression,List stack) {
		//TODO V1.2 Enrichir le langage d'expression
				
		String rc=null;
		if(expression.startsWith("${") && expression.endsWith("}"))
		{
			String pn = expression.substring(2,expression.length()-1);
			rc=ElementAndContext.getContextPropertyInStack(stack, pn);
			System.out.println("PF75 DUMP"+ElementAndContext.dumpContextPropertiesInStack(stack));
		}
		else
			rc=expression;
		System.out.println("PF73 resolved "+expression+" as "+rc);
		if(rc==null)
			throw new RuntimeException("PF68 null resolution of expression "+expression);
		return rc;
	}

}
