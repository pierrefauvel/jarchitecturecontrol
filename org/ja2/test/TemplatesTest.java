package org.ja2.test;

import java.lang.reflect.Constructor;

public class TemplatesTest {
	public static void main(String[] args)
	{
		try
		{
		Class c = Class.forName("com.sqli.bankonet.integration.dao.impl.GenericDaoHibernate$1");
		System.out.println(c);
		for(Constructor cti:c.getConstructors())
		{
			System.out.println(cti);
			for(Class pt:cti.getParameterTypes())
				System.out.println("\t"+pt);
		}
		c=c.getSuperclass();
		System.out.println(c);
		for(Constructor cti:c.getConstructors())
		{
			System.out.println(cti);
			for(Class pt:cti.getParameterTypes())
				System.out.println("\t"+pt);
		}
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
	}
}
