package org.jarco.code.external;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jarco.control.specifications.ElementAndContext;
import org.jarco.control.specifications.model.FollowProductionRule;
import org.jarco.swing.components.IExposableAsANode;
import org.jarco.swing.components.ModelInterface;
import org.jarco.xml.FromXmlFactory;
import org.w3c.dom.Element;

public class CodeRepositoryModel implements ModelInterface {

	private ICodeRepository repo;
	
	public CodeRepositoryModel(ICodeRepository repo)
	{
		this.repo=repo;
	}
	
	@Override
	public boolean acceptChild(Object uoPere, Object uoFils) {
		return false;
	}

	@Override
	public boolean acceptChildType(Object pere, String typeFils) {
		return false;
	}

	@Override
	public void addChild(Object uoParent, Object uoFils) {
		throw new UnsupportedOperationException("CodeRepositoryModel does not support addChild");
	}

	@Override
	public Object clone(Object original) {
		throw new UnsupportedOperationException("CodeRepositoryModel does not support clone");
	}

	public static class CodeElementFacet implements IExposableAsANode
	{
		ICodeElement parent;
		FollowProductionRule rule;
				
		public ICodeElement getParent()
		{
			return parent;
		}
		
		public String getRule()
		{
			return rule.getName();
		}

		public String toLabel() {
			return rule.toLabel();
		}
	}
	
	public static class CodeElementDecorated implements IExposableAsANode
	{
		ICodeElement parent;
		CodeElementFacet[] c;
		CodeElementFacet[] children(){
			if(c!=null)
				return c;
			return (c=childrenFor(parent));
		}
		
		public ICodeElement getParent()
		{
			return parent;
		}

		public String toLabel() {
			return parent.toLabel();
		}
	}
	
	//TODO 0.1 L'arbre doit être découvert A LA DEMANDE (cycles, taille trop importante)
	
	@Override
	public Iterable getChildren(Object uo) {
		if(uo instanceof CodeElementDecorated)
		{
			return check(Arrays.asList(((CodeElementDecorated)uo).children())); // on affiche le parent
		}
		else if(uo instanceof CodeElementFacet)
		{
			CodeElementFacet sn = (CodeElementFacet)uo;

			List<ICodeElement> lst =new ArrayList();
			List rc = sn.rule.produce(sn.parent, null, lst);
			
			CodeElementDecorated[] n = new CodeElementDecorated[rc.size()];
			for(int i=0;i<rc.size();i++)
			{
				n[i] = new CodeElementDecorated();
				n[i].parent = ((ElementAndContext)rc.get(i)).getElement();
				
			}
			
			// on affiche la règle de production
			return check(Arrays.asList(n));
		}
		else if(uo==null)
		{
			throw new RuntimeException("getChildren called with NULL argument");
		}
		else 
		{
			throw new RuntimeException("Unexpected type "+uo.getClass());
		}
	}
	
	private static List check(List lst)
	{
		for(Object o : lst)
		{
			if(o==null)
				throw new RuntimeException("Unexpteced NULL in list "+lst);
		};
		return lst;
	}
	
	private static CodeElementFacet[] childrenFor(ICodeElement e)
	{
		String[] n = null;
		
		if(e instanceof ICodeRepository)
		{
			n=MetaUtilities.getFollows(ICodeRepository.class);
		}
		else if(e instanceof IProject)
		{
			n=MetaUtilities.getFollows(IProject.class);
		}
		else if(e instanceof IPackage)
		{
			n=MetaUtilities.getFollows(IPackage.class);
		}
		else if(e instanceof IClass)
		{
			n=MetaUtilities.getFollows(IClass.class);
		}
		else if(e instanceof IAnnotation)
		{
			n=MetaUtilities.getFollows(IAnnotation.class);
		}
		else if(e instanceof IField)
		{
			n=MetaUtilities.getFollows(IField.class);
		}
		else if(e instanceof IMethod)
		{
			//TODO 0.1 calculer automatiquement la liste des follow possible pour ici ET pour l'éditeur de spec
			//n=new String[]{"ReturnType","ParameterTypes","ThrowsException","ReadsFields","WritesFields","InvokesMethods","InstantiatesClasses"};
			n=MetaUtilities.getFollows(IMethod.class);
			}
		else if(e instanceof IType)
		{
			n=MetaUtilities.getFollows(IType.class);
		}	
		else if(e instanceof IPropertiesDocument)
		{
			n=MetaUtilities.getFollows(IPropertiesDocument.class);
		}
		else if(e instanceof IXmlDocument)
		{
			n=MetaUtilities.getFollows(IXmlDocument.class);
		}
		else if(e instanceof IXmlElement)
		{
			n=MetaUtilities.getFollows(IXmlElement.class);
		}
		else if(e == null)
		{
			throw new RuntimeException("childrenFor called with NULL");
		}
		else
			throw new RuntimeException("Unexpected "+e.getClass()+" "+e);
		
		CodeElementFacet[] rc = new CodeElementFacet[n.length];
		for(int i=0;i<rc.length;i++)
		{
			rc[i]=new CodeElementFacet();
			rc[i].parent = e;
			rc[i].rule = new FollowProductionRule(n[i]);
		}
		return rc;
	}

	@Override
	public String getGroup(String type) {
		return "-";
	}

	@Override
	public Object getRoot() {
		CodeElementDecorated n = new CodeElementDecorated();
		n.parent=repo;
		return n;
	}

	@Override
	public String[] getTypes() {
		return new String[]{};
	}

	@Override
	public Object newObject(String tn) {
		throw new UnsupportedOperationException("CodeRepositoryModel does not support newObject");
	}

	@Override
	public void removeChild(Object uoParent, Object uoFils) {
		throw new UnsupportedOperationException("CodeRepositoryModel does not support removeChild");
	}

	@Override
	public void setRoot(Object root) {
		this.repo = (ICodeRepository)root;
	}

}
