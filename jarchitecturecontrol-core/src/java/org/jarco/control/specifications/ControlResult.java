package org.jarco.control.specifications;

import java.util.ArrayList;
import java.util.List;

import org.jarco.code.external.ICodeElement;
import org.jarco.collections.ImmutableList;
import org.jarco.control.Violation;
import org.jarco.control.report.IControlReport;
import org.jarco.control.report.itf.IViolationReport;
import org.jarco.control.specifications.model.Specification;

public class ControlResult {
  protected List stack = new ArrayList();
 
  private IControlReport report;
  private IViolationReport report2;
  
  public ControlResult(IControlReport report, IViolationReport vr)
  {
	  this.report=report;
	  this.report2=vr;
  }
  
  public final void pushSpec(Specification spec) {
    report.writeSpec(spec);
    stack.add(spec);
  }

  public final void pushElement(ElementAndContext ec) {
    report.writeElement(ec);
    stack.add(ec);
  }

  public final void signalViolation(Violation v) {
    report.writeViolation(v);
    report2.writeViolation(v);
  }

  public final void popElement() {
    Object e = stack.remove(stack.size() - 1);
    if(!(e instanceof ElementAndContext))
    	throw new RuntimeException("Expected ICodeElement, found "+e.getClass());
    report.popElement();
  }

  public final void popSpec() {
    Object s=stack.remove(stack.size() - 1);
    if(!(s instanceof Specification))
    	throw new RuntimeException("Expected Specification, found "+s.getClass());
    report.popSpec();
  }

  public final List getStack() {
    return stack;
  }
  
  public void close()
  {
	  report.close();
	  report2.close();
  }
}
