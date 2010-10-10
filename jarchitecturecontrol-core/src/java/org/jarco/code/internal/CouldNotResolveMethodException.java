package org.jarco.code.internal;

public class CouldNotResolveMethodException extends RuntimeException {

	private static String makeupMessage(String cn, String mn, String[] pt, String log)
	{
		StringBuffer sb=new StringBuffer("PF43 Could not resolve method ");
		sb.append(cn);
		sb.append("."+mn+"(");
		boolean first=true;
		if(pt!=null)
		{
			for(String pti:pt)
			{
				sb.append((first?"":" ,")+pti);
				first=false;
			};
		}
		else
			sb.append("*");
		sb.append(")\n");
		sb.append(log);
		return sb.toString();
	}
	
	public CouldNotResolveMethodException(String cn, String mn, String[] pt, String log)
	{
		super(makeupMessage(cn,mn,pt,log));
	}
	public CouldNotResolveMethodException(String cn, String mn, String[] pt, String log, CouldNotResolveMethodException ex)
	{
		super(makeupMessage(cn,mn,pt,log),ex);
	}

}
