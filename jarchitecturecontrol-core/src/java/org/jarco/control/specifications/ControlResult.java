package org.jarco.control.specifications;

import java.util.ArrayList;
import java.util.List;

import org.jarco.code.external.ICodeElement;
import org.jarco.collections.ImmutableList;
import org.jarco.control.report.IViolationReport;
import org.jarco.control.report.ResultReport;
import org.jarco.control.report.filesystem.FileSystemViolationReport;
import org.jarco.control.specifications.model.Specification;

public class ControlResult {
  protected List stack = new ArrayList();
 
  private ResultReport report;
  private IViolationReport report2;
  
  public ControlResult(ResultReport report, IViolationReport vr)
  {
	  this.report=report;
	  this.report2=vr;
  }
  
  protected int getLevel()
  {
	  return stack.size();
  }
  
  public final void pushSpec(Specification spec) {
    report.writeSpec(getLevel(),spec);
    stack.add(spec);
  }

  public final void pushElement(ElementAndContext ec) {
    report.writeElement(getLevel(),ec);
    stack.add(ec);
  }

  public final void signalViolation(Violation v) {
    report.writeViolation(getLevel(),v);
    report2.writeViolation(v);
  }

  public final void popElement() {
    Object e = stack.remove(stack.size() - 1);
    if(!(e instanceof ElementAndContext))
    	throw new RuntimeException("Expected ICodeElement, found "+e.getClass());
  }

  public final void popSpec() {
    Object s=stack.remove(stack.size() - 1);
    if(!(s instanceof Specification))
    	throw new RuntimeException("Expected Specification, found "+s.getClass());
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
