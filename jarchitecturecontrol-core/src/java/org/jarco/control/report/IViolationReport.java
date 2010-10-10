package org.jarco.control.report;

import org.jarco.control.specifications.Violation;

public interface IViolationReport {

	public void writeViolation(Violation v);
	public void close();
}
