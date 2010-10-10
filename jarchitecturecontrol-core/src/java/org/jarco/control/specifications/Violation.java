package org.jarco.control.specifications;

import java.util.List;

import org.jarco.code.external.ICodeElement;
import org.jarco.control.specifications.model.Specification;

public class Violation {
  private String id;
  private String message;
  private ICodeElement element;
  private List stack;

  //TODO V1.1 Ajouter un commentaire en texte libre expliquant la violation ? un ID "fonctionnel" ?
  //TODO V1.1 Ajouter une gravité à la violation
  
  public Violation(String id, String message, ICodeElement element, List stack)
  {
	  this.id=id;
	  this.message=message;
	  this.element=element;
	  this.stack=stack;
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

  public List<Specification> getStack() {
    return stack;
  }
  
  public String toString()
  {
	  return element+":"+message;
  }
}
