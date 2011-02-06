package org.jarco.control.report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.transform.TransformerException;

import org.jarco.control.report.filesystem.Indent;
import org.jarco.control.report.genericreport.FormattedText;
import org.jarco.control.report.genericreport.GenericReport;
import org.jarco.tags.external.ITag;
import org.jarco.tags.external.ITagAssociation;
import org.jarco.tags.external.ITagAssociationType;
import org.jarco.tags.external.ITagAttributeType;
import org.jarco.tags.external.ITagRole;
import org.jarco.tags.external.ITagType;

public class TagGReport implements ITagReport {
	
//	private PrintWriter pw;
	private File r;
	private GenericReport gr;

	public TagGReport(File reports, String name) throws FileNotFoundException, IOException {
//		pw=new PrintWriter(reports.getCanonicalPath()+File.separator+name+".ja2tags");
		r=new File(reports.getCanonicalPath()+File.separator+name+".tags.ja2.xml");
		gr=new GenericReport("tag-repository",new FormattedText("Tags and associations"),null);
	}

	public void writeTagSection()
	{
//		pw.println(Indent.STR(0)+"Tags");
		gr.push("tag-set",new FormattedText("Tags"),null);
	}
	
	public void writeType(ITagType tty) {
//		pw.println(Indent.STR(1)+tty.getName());
		gr.push("tag-type",new FormattedText("<b>Tag Type:</b>"+tty.getName()),null);
	}

	public void writeTag(ITag t) {
//		pw.print(Indent.STR(2));
//		for(ITagAttributeType a : t.getType().getAttributes())
//		{
//			pw.print(" "+a+"="+t.getAttributeValues().get(a));
//		}
//		pw.print(" "+t.getTaggedElement());
//		pw.println();
		StringBuffer sb=new StringBuffer();
		sb.append("<table border=1><tr><th>Attribute name</th><th>Attribute value</th></tr>");
		for(ITagAttributeType a : t.getType().getAttributes())
		{
			sb.append("<tr><td>"+a.getName()+"</td><td>"+t.getAttributeValues().get(a)+"</td></tr>");
		}
		sb.append("</table>");
		gr.push("tag",new FormattedText("<b>Element:</b>"+t.getTaggedElement()),new FormattedText(sb.toString()));
		gr.pop();
	}

	public void popType()
	{
		gr.pop();
	}
	
	public void close() {
//		pw.close();
		gr.pop();
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(r);
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

	public void writeAssociationSection()
	{
		gr.pop();
		gr.push("associtation-set", new FormattedText("<b>Associations</b>"), null);
	}
	
	public void writeAssociationType(ITagAssociationType tat) {
		gr.push("association-type",new FormattedText(tat.toLabel()),null);
	}

	public void writeAssociation(ITagAssociation as) {
		gr.push("association",new FormattedText("<b>Association:</b>"+as.getName()),null);
	}

	public void writeRole(ITagRole role) {
		gr.push("association",new FormattedText("<b>Role:</b>"+role.getType().getName()+" played by "+role.getTag().getTaggedElement().getName()),null);
		gr.pop();
	}

	public GenericReport getReport() {
		return gr;
	}

	public void popAssociation() {
		gr.pop();
	}

	public void popAssociationType() {
		gr.pop();
	}

}

