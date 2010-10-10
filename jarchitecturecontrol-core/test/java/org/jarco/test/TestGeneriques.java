package org.jarco.test;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class TestGeneriques {
	public static class Container<T extends Serializable>
	{
		public String string()
		{
	        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
	        return "param="+parameterizedType.getActualTypeArguments()[0].toString();
		}
	}
	public static class Test1 implements Serializable
	{
		
	}
	public static class Test2 implements Serializable
	{
		
	}
	public static abstract class Test
	{
		abstract Container<Test1> methode(Container<Test2> param);
	}
	
	public static String dump(Type c) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, InstantiationException
	{
		return ((ParameterizedType)c).getActualTypeArguments()[0].toString();
	}
	
	public static void main(String[] args) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException
	{
		Class c= Test.class;
		for(Method m : c.getDeclaredMethods())
		{
			System.out.println(m.getName());
			Type rt = m.getGenericReturnType();
			System.out.println("\trt="+dump(rt));
			if(m.getGenericParameterTypes().length>0)
			{
				Type param = m.getGenericParameterTypes()[0];
				System.out.println("\tparam[0]="+dump(param));
			}
		}
		
	}
}
