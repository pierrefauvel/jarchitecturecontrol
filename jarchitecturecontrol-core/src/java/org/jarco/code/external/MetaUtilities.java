package org.jarco.code.external;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.jarco.collections.ImmutableSet;

public class MetaUtilities {

	public static String[] getFollows(Class c)
	{
		Set<String> s=new HashSet<String>();
		for(Method m : c.getDeclaredMethods())
		{
			boolean b=false;
			if((m.getModifiers() & Modifier.STATIC) == 0 && m.getName().startsWith("get") && m.getParameterTypes().length==0)
			{
				Class rt = m.getReturnType();
				if(Iterable.class.isAssignableFrom(rt))
				{
					Type grt = m.getGenericReturnType();

					if(grt instanceof ParameterizedType){
					    ParameterizedType type = (ParameterizedType) grt;
					    Type[] typeArguments = type.getActualTypeArguments();
					    if(typeArguments.length==1)
					    {
					    	Class t = (Class)(typeArguments[0]);
					    	if(ICodeElement.class.isAssignableFrom(t))
					    	{
								s.add(m.getName().substring("get".length()));
								b=true;
					    	}
					    }
					}	
				}
				else
				{
					s.add(m.getName().substring("get".length()));
					b=true;
				}
				if(!b)
				{
					System.out.println("Does not follow "+m.getClass().getSimpleName()+" "+m.getName());
				}
			}
		};
		return s.toArray(new String[]{});
	}
}
