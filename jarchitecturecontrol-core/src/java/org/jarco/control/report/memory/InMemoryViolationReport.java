package org.jarco.control.report.memory;

import java.util.ArrayList;
import java.util.List;

import org.jarco.control.report.IViolationReport;
import org.jarco.control.specifications.Violation;

public class InMemoryViolationReport implements IViolationReport {

	private List<Violation> lst=new ArrayList<Violation>();
	
	public void close() {
	}

	public void writeViolation(Violation v) {
		lst.add(v);
	}

	public Violation[] getViolations()
	{
		return lst.toArray(new Violation[]{});
	}
}
