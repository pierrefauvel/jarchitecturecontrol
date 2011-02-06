package org.jarco.control.report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.jarco.code.external.ICodeElement;
import org.jarco.control.Violation;
import org.jarco.control.report.genericreport.FormattedText;
import org.jarco.control.report.genericreport.GenericReport;
import org.jarco.control.report.itf.IViolationReport;
import org.jarco.control.specifications.ElementAndContext;
import org.jarco.control.specifications.model.Specification;

public class ViolationGReport implements IViolationReport{

	private static final String KIND_VIOLATION = "violation";
	private static final String KIND_VIOLATION_SET = "violation-set";
	private File r;
	private GenericReport gr;
	
	public ViolationGReport(File dirpath, String name) throws IOException
	{
		r = new File(dirpath.getCanonicalPath()+File.separator+name+".violations.ja2.xml");		
		gr = new GenericReport(KIND_VIOLATION_SET,new FormattedText("<b>Violations</b>"),null);
	}
	
	public GenericReport getReport()
	{
		return gr;
	}
	
	public void close() {
		try {
			FileOutputStream fos = new FileOutputStream(r);
			gr.write(fos);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeViolation(Violation v) {
		FormattedText description = writeDescriptionForReport(v);
		FormattedText detail = writeDetailForReport(v);
		
		gr.push(KIND_VIOLATION, description, detail);
		gr.pop();
	}
	public static FormattedText writeDescriptionForReport(Violation v)
	{
		FormattedText description = new FormattedText("<b>Violation:</b>"+v.getId());
		return description;
	}
	public static FormattedText writeDetailForReport(Violation v)
	{
		StringBuffer sb=new StringBuffer();
		sb.append("<b>Id:</b>"+v.getId()+"<br/>");
		sb.append("<b>Message:</b>"+v.getMessage()+"<br/>");
		sb.append("<table border=1>");
		sb.append("<tr><th>Type</th><th>Description</th></tr>");
		List<Object> stack = v.getStack(); 
		for(int i=0;i<stack.size();i++)
		{
			Object obj = stack.get(i);
			if(obj instanceof Specification)
			{
				Specification s=(Specification)(stack.get(i));
				sb.append("<tr><td>"+s.getClass().getSimpleName()+"</td><td>"+s.toLabel()+"</td>");
			}
			else
			{
				ElementAndContext e = (ElementAndContext)(stack.get(i));
				ICodeElement ce = e.getElement();
				//TODO v1.0 cloner le code element lors de l'ajout à la Violation, afin d'avoir la valeur des propriétés du context au moment de la violation
				sb.append("<tr><td>"+ce.getClass().getSimpleName()+"</td><td>"+ce.getName()+"</td></tr>");
			}
		};
		sb.append("</table>");
		FormattedText detail = new FormattedText(sb.toString());
		return detail;
	}
}
