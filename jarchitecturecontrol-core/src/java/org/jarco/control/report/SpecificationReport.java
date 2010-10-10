package org.jarco.control.report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SpecificationReport {

	private int level;
	
	private PrintWriter pw;
	
	public SpecificationReport(File reports, String name) throws IOException {
		pw=new PrintWriter(new FileWriter(new File(reports.getCanonicalPath()+File.separator+name+".ja2specification")));
	}

	public void push() {
		level++;
	}

	public void writeSpec(String string) {
		pw.println(Indent.STR(level)+string);
	}

	public void writeConsequence(String string) {
		pw.println(Indent.STR(level)+string);
	}

	public void writeAssertion(String string) {
		pw.println(Indent.STR(level)+string);
	}

	public void pop() {
		level--;
	}
	
	public void close()
	{
		pw.close();
	}

}
