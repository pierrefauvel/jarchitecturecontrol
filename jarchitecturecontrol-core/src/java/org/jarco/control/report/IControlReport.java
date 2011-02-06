package org.jarco.control.report;

import org.jarco.code.external.ICodeElement;
import org.jarco.control.Violation;
import org.jarco.control.report.genericreport.GenericReport;
import org.jarco.control.specifications.ElementAndContext;
import org.jarco.control.specifications.model.Specification;

public interface IControlReport {

	public abstract void writeElement(ElementAndContext<ICodeElement> e);

	public abstract void popElement();

	public abstract void writeSpec(Specification s);

	public abstract void popSpec();

	public abstract void writeViolation(Violation v);

	public abstract void close();

	public abstract GenericReport getReport();

}