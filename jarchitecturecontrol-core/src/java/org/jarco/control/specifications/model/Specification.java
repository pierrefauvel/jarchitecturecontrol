package org.jarco.control.specifications.model;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jarco.code.external.ICodeElement;
import org.jarco.code.external.IProject;
import org.jarco.collections.ImmutableList;
import org.jarco.collections.ImmutableMap;
import org.jarco.control.report.SpecificationReport;
import org.jarco.control.specifications.ControlResult;
import org.jarco.control.specifications.ElementAndContext;
import org.jarco.control.specifications.FromXmlFactory;
import org.jarco.control.specifications.Violation;
import org.jarco.control.specifications.itf.IAssertion;
import org.jarco.control.specifications.itf.IConsequence;
import org.jarco.control.specifications.itf.IPredicate;
import org.jarco.control.specifications.itf.IProductionRule;
import org.jarco.control.specifications.model.FM.kind;
import org.jarco.swing.IExposableAsANode;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Specification<T extends ICodeElement> implements IExposableAsANode {
	@FM(kind=kind.treenode)
  private IPredicate<T> f;
	@FM(kind=kind.treenode)
  private IProductionRule<T> p;
	@FM(kind=kind.treenode)
  private List<Specification> ls;
	@FM(kind=kind.treenode)
  private List<IAssertion<T>> la;
	@FM(kind=kind.treenode)
  private List<IConsequence<T>> lc;

  public IPredicate<T> getFilter()
  {
	  return f;
  }
  public IProductionRule<T> getProductionRule()
  {
	  return p;
  }
  public ImmutableList<Specification> getChildSpecs()
  {
	  return new ImmutableList<Specification>(ls);
  }
  public ImmutableList<IAssertion<T>> getAssertions()
  {
	  return new ImmutableList<IAssertion<T>>(la);
  }
  public ImmutableList<IConsequence<T>> getConsequences()
  {
	  return new ImmutableList<IConsequence<T>>(lc);
  }
  
  
  public Specification(){
	    f = null;
	    p = null;
	    ls = new ArrayList<Specification>();
	    la = new ArrayList<IAssertion<T>>();
	    lc = new ArrayList<IConsequence<T>>();
  }
  
  public Specification(IProductionRule<T> p, IPredicate<T> f) {
	  this();
    this.p = p;
    this.f = f;
  }

  public Specification(IProductionRule<T> p) {
	  this();
	    this.p = p;
	  }

  public Specification(IPredicate<T> f) {
	  this();
	    this.f = f;
	  }

  public String toString()
  {
//	  String id = ""+System.identityHashCode(this);
	  if(p==null && f == null)
//		  return id + "# "+ "Spec";
		  return "specification";
	  else if(p==null)
//		  return id + "# "+ "Spec filter="+f;
		  return "specification filter="+f;
	  else if(f==null)
//		  return id + "# "+ "Spec productionRule="+p;
		  return "specification productionRule={"+p+"}";
	  else
		  return "specification productionRule={"+p+"},filter={"+f+"}";
  }
  
  public String toLabel()
  {
	  if(p==null)
	  {
		  if(f==null)
			  return "<i>no filter, no production-rule</i>";
		  return "<b>filter</b>="+f+"";
	  };
	  if(f==null)
		  return "<b>production-rule</b>="+p+"";
	  return "<b>filter</b>="+f+" <b>production-rule</b>="+p+"";
  }
  
  public String toXml()
  {
	  StringBuffer sb=new StringBuffer();
	  for(IAssertion a:la)
		  sb.append(a.toXml());
	  for(IConsequence c:lc)
		  sb.append(c.toXml());
	  for(Specification s:ls)
		  sb.append(s.toXml());
	  
	  
	  if(p==null && f == null)
		  return "<specification>"+sb+"</specification>";
	  else if(p==null)
		  return "<specification>"+f.toXml()+""+sb+"</specification>";
	  else if(f==null)
		  return "<specification>"+p.toXml()+""+sb+"</specification>";
	  else
		  return "<specification>"+p.toXml()+""+f.toXml()+""+sb+"</specification>";
  }
  
  public static Specification fromXml(FromXmlFactory f, Element e)
  {

	  NodeList nl = e.getChildNodes();
//	  Object[] o = new Object[nl.getLength()];
//	  for(int i=0;i<o.length;i++)
//	  {
//		  o[i]=f.fromXml((Element)(nl.item(i)));
//	  }
	  List<Object> l = new ArrayList<Object>();
	  for (int i=0;i<nl.getLength();i++)
	  {
		  if(nl.item(i) instanceof Element)
		  {
		  l.add(f.fromXml((Element)nl.item(i)));
		  }
	  }
	  Object[] o = l.toArray(new Object[]{});
	  int j;
	  loop: for(j=0;j<o.length;j++)
	  {
		  if(Specification.class.isAssignableFrom(o[j].getClass()))
			  break loop;
		  if(IAssertion.class.isAssignableFrom(o[j].getClass()))
			  break loop;
		  if(IConsequence.class.isAssignableFrom(o[j].getClass()))
			  break loop;
	  }

	  Specification rc = null;
	  if(j==0)
	  {
		  rc=new Specification();
	  }
	  else
	  {
		  Element e1 = (Element)(nl.item(0));
		  Object o1 = f.fromXml(e1);
		  
		  if(j==2)
		  {
			  IPredicate f2 = (IPredicate)(f.fromXml((Element)(nl.item(1))));
			  rc=new Specification ( (IProductionRule)o1, f2);
		  }
		  else if(o1 instanceof IProductionRule)
		  {
			  rc=new Specification((IProductionRule)o1);
		  }
		  else
		  {
			  rc=new Specification((IPredicate)o1);
		  }
	  }
	  
	  for(int i=j;i<o.length;i++)
	  {
		  if(Specification.class.isAssignableFrom(o[i].getClass()))
			  rc.addChildSpec((Specification)o[i]);
		  if(IAssertion.class.isAssignableFrom(o[i].getClass()))
			  rc.addAssertion((IAssertion)o[i]);
		  if(IConsequence.class.isAssignableFrom(o[i].getClass()))
			  rc.addConsequence((IConsequence)o[i]);
	  };
	  return rc;
  }
  
  public void addChildSpec(Specification s) {
    ls.add(s);
  }

  public void addAssertion(IAssertion<T> a) {
    la.add(a);
  }

	public void addConsequence(IConsequence<T> c) {
		lc.add(c);
	};

  public void visit(ICodeElement context, ControlResult r)
  {
	  _visit(context,r);
	  r.close();
  }
	
	
  private void _visit(ICodeElement context, ControlResult r) {
//    r.pushSpec(this);
    Iterable<ElementAndContext<T>> ps = null;
    ImmutableMap<String,String> filterResult;
    if(p!=null)
    {
    	ps= p.produce(context, f, r.getStack());
    }
    else if(f!=null && (filterResult=f.include((T)context))!=null)
    {
    	ps= (Iterable<ElementAndContext<T>>) Collections.singleton(new ElementAndContext<T>((T)context,filterResult));
    }
    else if(f==null)
    {
    	ps= (Iterable<ElementAndContext<T>>) Collections.singleton(new ElementAndContext<T>((T)context));
    }
    else
    {
//    	r.popSpec();
    	return;
    }

    boolean first=true;
    for (ElementAndContext ec : ps) {
    	T t = (T) ec.getElement();
      if(first)
        r.pushSpec(this);
      first=false;
      r.pushElement(ec);
      for (IConsequence<T> c:lc)
      {
    	  c.apply(t, r.getStack());
      }
      for (IAssertion<T> a : la) {
    	  try
    	  {
        Violation v = a.assertRule(t, r.getStack());
        if (v != null) {
          r.signalViolation(v);
        }
    	  }
    	  catch(Throwable th)
    	  {
    		  th.printStackTrace(System.out);
    	  }
      }
      for (Specification s : ls) {
        s._visit(t, r);
      }
      r.popElement();
    }
    if(!first)r.popSpec();
  }
  public void dump(SpecificationReport d)
  {
	  this._dump(d);
	  d.close();
  }
  private void _dump(SpecificationReport d){
	  d.push();
	  d.writeSpec(toString());
	  d.push();
	  for(IConsequence<T> c:lc)
	  {
		  d.writeConsequence(c.toString());  
	  }
	  for(IAssertion<T> a: la)
	  {
		  d.writeAssertion(a.toString());
	  }
	  d.pop();
	  for(Specification s : ls)
	  {
		  s._dump(d);
	  }
	  d.pop();
  }
public void setFilter(IPredicate uoFils) {
	this.f=uoFils;
}
public void setProductionRule(IProductionRule uoFils) {
	this.p=uoFils;
}
public void remove(IAssertion uoFils) {
	la.remove(uoFils);
}
public void remove(IConsequence uoFils) {
	lc.remove(uoFils);
}
public void remove(Specification uoFils) {
	ls.remove(uoFils);
}
  
}
