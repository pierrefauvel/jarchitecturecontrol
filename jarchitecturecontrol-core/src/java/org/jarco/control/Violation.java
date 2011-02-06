package org.jarco.control;

import java.util.ArrayList;
import java.util.List;

import org.jarco.code.external.ICodeElement;
import org.jarco.control.specifications.model.Specification;

public class Violation {
  private String id;
  private String message;
  private ICodeElement element;
  private List<Object> stack;

  //TODO V1.1 Ajouter une gravité à la violation
  
  public Violation(String id, String message, ICodeElement element, List<Object> stack)
  {
	  this.id=id;
	  this.message=message;
	  this.element=element;
	  this.stack=new ArrayList<Object>();
	  //the stack parameter must be cloned : it is transient
	  for(Object o: stack)
	  {
		  this.stack.add(o);
	  }
  }
  
  public String getId(){
	  return id;
  }
  
  public String getMessage() {
    return message;
  }

  public ICodeElement getElement() {
    return element;
  }

  public List<Object> getStack() {
    return stack;
  }
  @Override
  public String toString()
  {
	  return element+":"+message;
  }
}
