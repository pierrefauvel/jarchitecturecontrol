package org.jarco.code.internal;

public class NameUtilities {
	public static String packageFromClassLongName(String cn)
	{
		if(cn.indexOf(".")==-1)
			return "";
		String pn = cn.substring(0,cn.lastIndexOf("."));
		return pn;
	}
	
	public static String classShortNameFromClassLongName(String cn)
	{
		if(cn.indexOf(".")==-1)
			return cn;
		String sn = cn.substring(cn.lastIndexOf(".")+1);
		return sn;
	}

	public static String resolveParent(
			String cn) {
		if(cn.indexOf("$")==-1)
			return null;
		cn=cn.substring(0,cn.lastIndexOf("$"));
		return cn;
	}
}
