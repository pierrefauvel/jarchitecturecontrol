package org.jarco.control.report.itf;

import org.jarco.control.Violation;
import org.jarco.control.report.genericreport.GenericReport;

public interface IViolationReport {

	public void writeViolation(Violation v);
	public void close();
	
	public abstract GenericReport getReport();

}
