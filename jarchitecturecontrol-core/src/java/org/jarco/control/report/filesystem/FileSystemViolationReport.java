package org.jarco.control.report.filesystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jarco.control.report.IViolationReport;
import org.jarco.control.report.Indent;
import org.jarco.control.specifications.Violation;
import org.jarco.control.specifications.model.Specification;

import java.io.PrintWriter;
import java.util.List;

public class FileSystemViolationReport implements IViolationReport{
	
	private PrintWriter pw;
	
	public FileSystemViolationReport(File dirpath, String name) throws IOException
	{
		pw=new PrintWriter(new FileWriter(dirpath.getCanonicalPath()+File.separator+name+".ja2violations"));
	}
	
	public void writeViolation(Violation v)
	{
		pw.println(Indent.STR(0)+v.getMessage()+" "+v.getElement());
//		List<Specification> lo = v.getStack();
//		for(int i=0;i<lo.size();i++)
//		{
//			pw.println(Indent.STR(1+i)+lo.get(lo.size()-1-i));
//		}
	}
	
	public void close()
	{
		pw.close();
	}
}
