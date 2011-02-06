package org.jarco.test.analysis.packageA;

import java.text.SimpleDateFormat;
import java.util.Date;

@Agent(name = "agent classeA")
public class ClasseA implements InterfaceD {
	public static final String CONSTANT_FIELD = "ConstantFieldValue";
	@Property(name="Id")
	private long id;
	
	public static String NON_FINAL_PUBLIC_FIELD = "NonFinalPublicField";

	@Property(name="Comment")
	public String comment;
	
	public ClasseA()
	{
		setId(-1);
	}
	
	@Property(name="Id")
	protected void setId(long id) throws RuntimeException
	{
		this.id=id;
		System.out.println(new Log1(new Date(),"id set to "+id));
	}
	@Property(name="Id")
	public long getId()
	{
		return id;
	}
	
	private static final class Log1
	{
		@Property(name = "date")
		private Date date;
		
		@Property(name = "message")
		private String message;
		
		public Log1(Date date, String message)
		{
			this.date=date;
			this.message=message;
		}
		
		public String toString()
		{
			return date+":"+message;
		}
		
		public String[] toArray()
		{
			return new String[]{new SimpleDateFormat().format(date),message};
		}
	}
}
