package org.jarco.control.report;

import org.jarco.control.report.genericreport.GenericReport;
import org.jarco.tags.external.ITag;
import org.jarco.tags.external.ITagAssociation;
import org.jarco.tags.external.ITagAssociationType;
import org.jarco.tags.external.ITagRole;
import org.jarco.tags.external.ITagType;

public interface ITagReport {

	public abstract void writeTagSection();

	public abstract void writeType(ITagType tty);

	public abstract void writeTag(ITag t);

	public abstract void close();

	public abstract void writeAssociationSection();

	public abstract void writeAssociationType(ITagAssociationType tat);

	public abstract void writeAssociation(ITagAssociation as);

	public abstract void writeRole(ITagRole role);
	
	public abstract GenericReport getReport();

	public abstract void popType();

	public abstract void popAssociation();

	public abstract void popAssociationType();

}