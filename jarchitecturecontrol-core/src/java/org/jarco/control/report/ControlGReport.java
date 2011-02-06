package org.jarco.control.report;

import java.io.File;

import org.jarco.code.external.ICodeElement;
import org.jarco.control.Violation;
import org.jarco.control.report.filesystem.Indent;
import org.jarco.control.report.genericreport.FormattedText;
import org.jarco.control.report.genericreport.GenericReport;
import org.jarco.control.specifications.ElementAndContext;
import org.jarco.control.specifications.itf.IConsequence;
import org.jarco.control.specifications.model.Specification;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;

public class ControlGReport implements IControlReport {

	private GenericReport gr;
	private File r;
	
	public ControlGReport(File reports, String name) throws IOException {
		gr=new GenericReport("control-flow",new FormattedText("Control flow"),null);
		r=new File(reports.getCanonicalPath()+File.separator+name+".control.ja2.xml");
	}

	public void writeElement(ElementAndContext<ICodeElement> e) {
		FormattedText description = new FormattedText("<b>Element: Type:</b>"+e.getElement().getClass().getSimpleName()+" <b>Name:</b>"+e.getElement().getName());
		StringBuffer sb=new StringBuffer();
		sb.append("<table border=1><tr><td>Context property</td><td>Value</td></tr>");
		for(String pn : e.getContextPropertyNames())
		{
			sb.append("<tr><td>"+pn+"</td><td>"+e.getContextProperty(pn)+"</td></tr>");
		}
		sb.append("</table>");
		gr.push("element", description, new FormattedText(sb.toString()));
	}

	public void popElement()
	{
		gr.pop();
	}
	
	public void writeSpec(Specification s) {
		gr.push("specification",new FormattedText("<b>Specification:</b>"+s.toLabel()),null);
	}
	
	public void popSpec()
	{
		gr.pop();
	}

	public void writeViolation(Violation v) {
		gr.push("violation",new FormattedText("<b>Violation:</b>"+v.getId()),new FormattedText(v.getMessage()));
		gr.pop();
	}
	public void writeConsequence(IConsequence c) {
		gr.push("violation",new FormattedText("<b>Consequence:</b>"+c.toLabel()),null);
		gr.pop();
	}
	public void close()
	{
		try
		{
			FileOutputStream os = new FileOutputStream(r);
			gr.write(os);
			os.close();
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
	}
	
	public GenericReport getReport()
	{
		return gr;
	}
}
