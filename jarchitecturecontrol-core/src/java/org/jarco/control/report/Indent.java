package org.jarco.control.report;

/*extraire une couche de reporting bas niveau : mots avec style + indentation
pondre les rapports :
- en xml (incluant ces styles)
- en html colorié
*/

public class Indent {
	private static String STR = "";
	private static String INC = "  ";
	
	public static String STR(int count)
	{
		int length = count * INC.length();
		if(STR.length()>length)
		{
			return STR.substring(0,length);
		}
		STR=STR+INC;
		return STR(count);
	}

}
