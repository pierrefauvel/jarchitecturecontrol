package org.jarco.control.report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.jarco.tags.external.ITag;
import org.jarco.tags.external.ITagAssociation;
import org.jarco.tags.external.ITagAssociationType;
import org.jarco.tags.external.ITagAttributeType;
import org.jarco.tags.external.ITagRole;
import org.jarco.tags.external.ITagType;

public class TagReport {
	
	private PrintWriter pw;

	public TagReport(File reports, String name) throws FileNotFoundException, IOException {
		pw=new PrintWriter(reports.getCanonicalPath()+File.separator+name+".ja2tags");
	}

	public void writeTagSection()
	{
		pw.println(Indent.STR(0)+"Tags");
	}
	
	public void writeType(ITagType tty) {
		pw.println(Indent.STR(1)+tty.getName());
	}

	public void writeTag(ITag t) {
		pw.print(Indent.STR(2));
		for(ITagAttributeType a : t.getType().getAttributes())
		{
			pw.print(" "+a+"="+t.getAttributeValues().get(a));
		}
		pw.print(" "+t.getTaggedElement());
		pw.println();
	}

	public void close() {
		pw.close();
	}

	public void writeAssociationSection()
	{
		pw.println(Indent.STR(0)+"Tags Associations");
	}
	
	public void writeAssociationType(ITagAssociationType tat) {
		pw.println(Indent.STR(1)+tat);
	}

	public void writeAssociation(ITagAssociation as) {
		pw.println(Indent.STR(2)+as);
	}

	public void writeRole(ITagRole role) {
		pw.println(Indent.STR(3)+role);
	}

}

