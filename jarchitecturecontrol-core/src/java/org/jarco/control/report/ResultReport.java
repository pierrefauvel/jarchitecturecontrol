package org.jarco.control.report;

import java.io.File;

import org.jarco.code.external.ICodeElement;
import org.jarco.control.specifications.ElementAndContext;
import org.jarco.control.specifications.Violation;
import org.jarco.control.specifications.model.Specification;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;

public class ResultReport {

	private PrintWriter pw;
	
	public ResultReport(File reports, String name) throws IOException {
		pw=new PrintWriter(new FileWriter(reports.getCanonicalPath()+File.separator+name+"."+"ja2control"));
	}

	public void writeElement(int level,ElementAndContext<ICodeElement> e) {
		pw.println(Indent.STR(level)+e);
	}

	public void writeSpec(int level,Specification s) {
		pw.println(Indent.STR(level)+s);
	}

	public void writeViolation(int level,Violation v) {
		pw.println(Indent.STR(level)+v);
	}
	public void close()
	{
		pw.close();
	}
}
