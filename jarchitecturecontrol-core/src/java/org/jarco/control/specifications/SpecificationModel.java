package org.jarco.control.specifications;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jarco.control.specifications.*;
import org.jarco.control.specifications.itf.IAssertion;
import org.jarco.control.specifications.itf.IConsequence;
import org.jarco.control.specifications.itf.IPredicate;
import org.jarco.control.specifications.itf.IProductionRule;
import org.jarco.control.specifications.model.AnnotationPredicate;
import org.jarco.control.specifications.model.ChainedProductionRule;
import org.jarco.control.specifications.model.ContextAffectationFromProperties;
import org.jarco.control.specifications.model.ContextAffectationFromXPath;
import org.jarco.control.specifications.model.FilterFromXPath;
import org.jarco.control.specifications.model.FollowAssertion;
import org.jarco.control.specifications.model.FollowProductionRule;
import org.jarco.control.specifications.model.ModifierPredicate;
import org.jarco.control.specifications.model.NamePredicate;
import org.jarco.control.specifications.model.OrAssertion;
import org.jarco.control.specifications.model.PredicateAssertion;
import org.jarco.control.specifications.model.Specification;
import org.jarco.control.specifications.model.TagAffectation;
import org.jarco.control.specifications.model.TagPredicate;
import org.jarco.swing.components.ModelInterface;

public class SpecificationModel implements ModelInterface{

	private static Map<String,Class> hm_name2class=new HashMap<String,Class>();
	static
	{
		hm_name2class.put("AnnotationPredicate",AnnotationPredicate.class);
		hm_name2class.put("ChainedProductionRule",ChainedProductionRule.class);
		hm_name2class.put("ContextAffectationFromProperties",ContextAffectationFromProperties.class);
		hm_name2class.put("ContextAffectationFromXPath",ContextAffectationFromXPath.class);
		hm_name2class.put("FilterFromXPath",FilterFromXPath.class);
		hm_name2class.put("FollowAssertion",FollowAssertion.class);
		hm_name2class.put("FollowProductionRule",FollowProductionRule.class);
		hm_name2class.put("ModifierPredicate",ModifierPredicate.class);
		hm_name2class.put("NamePredicate",NamePredicate.class);
		hm_name2class.put("OrAssertion",OrAssertion.class);
		hm_name2class.put("PredicateAssertion",PredicateAssertion.class);
		hm_name2class.put("Specification",Specification.class);
		hm_name2class.put("TagAffectation",TagAffectation.class);
		hm_name2class.put("TagPredicate",TagPredicate.class);
	}
	
	private Specification _root;
	
	public SpecificationModel ( Specification spec)
	{
		_root=spec;
	}
	
	public void setRoot ( Object obj)
	{
		_root = (Specification)obj;
	}
	
	@Override
	public boolean acceptChild(Object uoPere, Object uoFils) {
		return acceptChildType(uoPere,uoFils.getClass());
	}

	@Override
	public boolean acceptChildType(Object pere, String typeFils) {
		return acceptChildType(pere,hm_name2class.get(typeFils));
	}

	private boolean acceptChildType(Object pere, Class fils)
	{
		if(pere instanceof IPredicate) return false;
		if(pere instanceof IConsequence) return false;
		if(pere instanceof ChainedProductionRule) return (IProductionRule.class.isAssignableFrom(fils));
		if(pere instanceof OrAssertion) return (IAssertion.class.isAssignableFrom(fils));
		if(pere instanceof FollowAssertion) return (IPredicate.class.isAssignableFrom(fils));
		if(pere instanceof PredicateAssertion) return (IPredicate.class.isAssignableFrom(fils));
		if(pere instanceof IAssertion) return false;
		if(pere instanceof Specification)
		{
			if(IAssertion.class.isAssignableFrom(fils)) return true;
			if(IConsequence.class.isAssignableFrom(fils)) return true;
			if(Specification.class.isAssignableFrom(fils)) return true;
			Specification spec = (Specification)pere;
			if(IPredicate.class.isAssignableFrom(fils)) return spec.getFilter()==null;
			if(IProductionRule.class.isAssignableFrom(fils)) return spec.getProductionRule()==null;
			return false;
		}
		return false;
	}

	public String getGroup(String typename)
	{
		Class c = hm_name2class.get(typename);
		if(IPredicate.class.isAssignableFrom(c)) return "Predicate";
		if(IConsequence.class.isAssignableFrom(c)) return "Consequence";
		if(IProductionRule.class.isAssignableFrom(c)) return "ProductionRule";
		if(IAssertion.class.isAssignableFrom(c)) return "Assertion";
		return null;
	}
	
	@Override
	public void addChild(Object uoParent, Object uoFils) {
		if(uoParent instanceof ChainedProductionRule)
		{
			IProductionRule pr_fils = (IProductionRule) uoFils;
			ChainedProductionRule pr_parent = (ChainedProductionRule)uoParent;
			pr_parent.chain(pr_fils);
		}
		else if(uoParent instanceof OrAssertion)
		{
			OrAssertion ass_parent = (OrAssertion)uoParent;
			IAssertion ass_fils = (IAssertion)uoFils;
			ass_parent.addAssertion(ass_fils);
		}
		else if(uoParent instanceof FollowAssertion)
		{
			FollowAssertion ass_parent = (FollowAssertion)uoParent;
			IPredicate ass_fils = (IPredicate)uoFils;
			ass_parent.setFilter(ass_fils);
		}
		else if(uoParent instanceof PredicateAssertion)
		{
			PredicateAssertion ass_parent = (PredicateAssertion)uoParent;
			IPredicate ass_fils = (IPredicate)uoFils;
			ass_parent.setPredicate(ass_fils);
		}
		else if(uoParent instanceof Specification)
		{
			Specification spec_pere = (Specification)uoParent;
			if(uoFils instanceof IAssertion)spec_pere.addAssertion((IAssertion)uoFils);
			else if(uoFils instanceof IConsequence)spec_pere.addConsequence((IConsequence)uoFils);
			else if(uoFils instanceof Specification)spec_pere.addChildSpec((Specification)uoFils);
			else if(uoFils instanceof IPredicate)spec_pere.setFilter((IPredicate)uoFils);
			else if(uoFils instanceof IProductionRule)spec_pere.setProductionRule((IProductionRule)uoFils);
			else throw new RuntimeException("Unexpected child type "+uoFils+" for Spec "+uoParent);
		}
	}

	private static final Iterable EMPTY_LIST = new ArrayList(0);
	
	@Override
	public Iterable getChildren(Object uo) {
		if(uo==null)
			return EMPTY_LIST;
		if(uo instanceof IPredicate) return EMPTY_LIST;
		if(uo instanceof IConsequence) return EMPTY_LIST;
		if(uo instanceof ChainedProductionRule) return ((ChainedProductionRule)uo).rules();
		if(uo instanceof OrAssertion) return ((OrAssertion)uo).assertions();
		if(uo instanceof PredicateAssertion) 
			{
			List<Object> rc=new ArrayList<Object>();
			rc.add(((PredicateAssertion)uo).predicate());
			return rc;
			}
		if(uo instanceof FollowAssertion) 
		{
		List<Object> rc=new ArrayList<Object>();
		FollowAssertion fa = (FollowAssertion)uo;
		if(fa.getFilter()!=null)
			rc.add(fa.getFilter());
		return rc;
		}
		if(uo instanceof IAssertion) return EMPTY_LIST;
		if(uo instanceof IProductionRule) return EMPTY_LIST;
		List<Object> rc=new ArrayList<Object>();
		Specification sp = (Specification)uo;
		if(sp.getProductionRule()!=null)
		{
			rc.add(sp.getProductionRule());
		}
		if(sp.getFilter()!=null)
		{
			rc.add(sp.getFilter());
		}
		for(Object i: sp.getChildSpecs())
		{
			rc.add(i);
		}
		for(Object i: sp.getAssertions())
		{
			rc.add(i);
		}
		for(Object i: sp.getConsequences())
		{
			rc.add(i);
		}
		return rc;
	}

	@Override
	public Object getRoot() {
		return _root;
	}

	@Override
	public String[] getTypes() {
		return hm_name2class.keySet().toArray(new String[]{});
	}

	@Override
	public Object newObject(String tn) {
		try
		{
			return hm_name2class.get(tn).newInstance();
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			return null;
		}
	}

	@Override
	public Object clone(Object original) {
		//TODO V1.0 clone a implémenter
		throw new UnsupportedOperationException("Clone not yet supported");
	}

	@Override
	public void removeChild(Object uoParent, Object uoFils) {
		if(uoParent instanceof ChainedProductionRule)
		{
			IProductionRule pr_fils = (IProductionRule) uoFils;
			ChainedProductionRule pr_parent = (ChainedProductionRule)uoParent;
			pr_parent.remove(pr_fils);
		}
		else if(uoParent instanceof OrAssertion)
		{
			OrAssertion ass_parent = (OrAssertion)uoParent;
			IAssertion ass_fils = (IAssertion)uoFils;
			ass_parent.remove(ass_fils);
		}
		else if(uoParent instanceof FollowAssertion)
		{
			FollowAssertion ass_parent = (FollowAssertion)uoParent;
			IPredicate ass_fils = (IPredicate)uoFils;
			ass_parent.setFilter(null);
		}
		else if(uoParent instanceof PredicateAssertion)
		{
			PredicateAssertion ass_parent = (PredicateAssertion)uoParent;
			IPredicate ass_fils = (IPredicate)uoFils;
			ass_parent.setPredicate(null);
		}
		else if(uoParent instanceof Specification)
		{
			Specification spec_pere = (Specification)uoParent;
			if(uoFils instanceof IAssertion)spec_pere.remove((IAssertion)uoFils);
			else if(uoFils instanceof IConsequence)spec_pere.remove((IConsequence)uoFils);
			else if(uoFils instanceof Specification)spec_pere.remove((Specification)uoFils);
			else if(uoFils instanceof IPredicate)spec_pere.setFilter(null);
			else if(uoFils instanceof IProductionRule)spec_pere.setProductionRule(null);
			else throw new RuntimeException("Unexpected child type "+uoFils+" for Spec "+uoParent);
		}
	}

}
