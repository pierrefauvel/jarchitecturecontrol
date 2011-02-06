package org.jarco.tags.internal;

import java.util.ArrayList;
import java.util.List;

import org.jarco.swing.components.ModelInterface;
import org.jarco.tags.external.ITagAssociationType;
import org.jarco.tags.external.ITagAttributeType;
import org.jarco.tags.external.ITagRepository;
import org.jarco.tags.external.ITagType;
import org.jarco.tags.external.ITagRoleType;

public class TagRepositoryModel implements ModelInterface {

	/**
	 * TagAssociationTypeInternal
	 * TagAttributeTypeInternal
	 * TagRepositoryInternal
	 * TagRoleTypeInternal
	 * TagTypeInternal
	 * 
	 * Comment gérer les map ? noeud dans l'arbre mixte : type + valeur ? aller chercher la valeur dans le noeud au dessus ?
	 */

	private TagRepositoryInternal _tr;
	public TagRepositoryModel(TagRepositoryInternal tr)
	{
		_tr=tr;
	}
	
	@Override
	public boolean acceptChild(Object uoPere, Object uoFils) {
		if(uoPere instanceof TagRepositoryInternal)
		{
			return uoFils instanceof TagTypeInternal || uoFils instanceof TagAssociationInternal;
		}
		if(uoPere instanceof TagTypeInternal)
		{
			return uoFils instanceof TagAttributeTypeInternal;
		}
		if(uoPere instanceof TagAttributeTypeInternal)
		{
			return false;
		}
		if(uoPere instanceof TagAssociationTypeInternal)
		{
			return uoFils instanceof TagRoleTypeInternal;
		}
		if(uoPere instanceof TagRoleTypeInternal)
		{
			return false;
		};
		throw new RuntimeException("Unexpected classes "+uoPere.getClass()+" and "+uoFils.getClass());
	}

	@Override
	public boolean acceptChildType(Object uoPere, String typeFils) {
		System.out.println(uoPere.getClass()+"=>"+typeFils);
		if(uoPere instanceof TagRepositoryInternal)
		{
			return typeFils.compareTo("TagType")==0 || typeFils.compareTo("TagAssociationType")==0;
		}
		if(uoPere instanceof TagTypeInternal)
		{
			return typeFils.compareTo("TagAttributeType")==0;
		}
		if(uoPere instanceof TagAttributeTypeInternal)
		{
			return false;
		}
		if(uoPere instanceof TagAssociationTypeInternal)
		{
			return typeFils.compareTo("TagRoleType")==0;
		}
		if(uoPere instanceof TagRoleTypeInternal)
		{
			return false;
		};
		throw new RuntimeException("Unexpected classes "+uoPere.getClass()+" and "+typeFils);
	}

	@Override
	public void addChild(Object uoPere, Object uoFils) {
		if(uoPere instanceof TagRepositoryInternal && uoFils instanceof TagTypeInternal)
		{
			TagRepositoryInternal r = (TagRepositoryInternal)uoPere;
			TagTypeInternal t = (TagTypeInternal)uoFils;
			r.newTagType(t);
			return;
		}
		if(uoPere instanceof TagRepositoryInternal && uoFils instanceof TagAssociationInternal)
		{
			TagRepositoryInternal r = (TagRepositoryInternal)uoPere;
			TagAssociationTypeInternal a = (TagAssociationTypeInternal)uoFils;
			r.newTagAssociationType(a);
			return;
		}
		if(uoPere instanceof TagTypeInternal)
		{
			TagTypeInternal t = (TagTypeInternal)uoPere;
			TagAttributeTypeInternal at = (TagAttributeTypeInternal) uoFils;
			//TODO v1.1 renommer en newAttributeType
			t.newAttribute(at);
			return;
		}
		if(uoPere instanceof TagAssociationTypeInternal)
		{
			TagAssociationTypeInternal at = (TagAssociationTypeInternal)uoPere;
			TagRoleTypeInternal rt = (TagRoleTypeInternal)uoFils;
			at.newRoleType(rt);
			return;
		}
		throw new RuntimeException("Unexpected classes "+uoPere.getClass()+" and "+uoFils);
	}

	@Override
	public Object clone(Object original) {
		//TODO v1.1 support du clone
		throw new UnsupportedOperationException("Not yet supported");
	}

	@Override
	public Iterable getChildren(Object uoPere) {
		if(uoPere instanceof TagRepositoryInternal )
		{
			TagRepositoryInternal r = (TagRepositoryInternal)uoPere;
			List<Object> l = new ArrayList();
			for(ITagType tt : r.getTagTypes())
			{
				l.add(tt);
			}
			for(ITagAssociationType at : r.getTagAssociationTypes())
			{
				l.add(at);
			}
			return l;
		}
		if(uoPere instanceof TagTypeInternal)
		{
			TagTypeInternal t = (TagTypeInternal)uoPere;
			List<Object> l = new ArrayList();
			for(ITagAttributeType at : t.getAttributes())
			{
				l.add(at);
			}
			return l;
		}
		if(uoPere instanceof TagAssociationTypeInternal)
		{
			TagAssociationTypeInternal at = (TagAssociationTypeInternal)uoPere;
			List<Object> l = new ArrayList();
			for(ITagRoleType rt : at.getRoles())
			{
				l.add(rt);
			}
			return l;
		}
		if(uoPere instanceof TagAttributeTypeInternal)
			return new ArrayList();
		if(uoPere instanceof TagRoleTypeInternal)
			return new ArrayList();
		throw new RuntimeException("Unexpected class "+uoPere.getClass());
	}

	@Override
	public String getGroup(String type) {
		return "Tag";
	}

	@Override
	public void setRoot(Object root) {
		_tr = (TagRepositoryInternal) root;
	}

	@Override
	public Object getRoot() {
		return _tr;
	}

	@Override
	public String[] getTypes() {
		return new String[]{
//				"TagAssociation",
				"TagAssociationType",
				"TagAttributeType",
//				"Tag",
				"TagRepository",
//				"TagRole",
				"TagRoleType",
				"TagType"
		};
	}

	@Override
	public Object newObject(String tn) {
//		if(tn.compareTo("TagAssociation")==0)
//		{
//			return new TagAssociationInternal();
//		}
		if(tn.compareTo("TagAssociationType")==0)
		{
			return new TagAssociationTypeInternal();
		}
		else if(tn.compareTo("TagAttributeType")==0)
		{
			return new TagAttributeTypeInternal();
		}
//		else if(tn.compareTo("Tag")==0)
//		{
//			return new TagInternal();
//		}
//		else if(tn.compareTo("TagRole")==0)
//		{
//			return new TagRoleInternal();
//		}
		else if(tn.compareTo("TagRoleType")==0)
		{
			return new TagRoleTypeInternal();
		}
		else if(tn.compareTo("TagType")==0)
		{
			return new TagTypeInternal();
		}
		else
			throw new RuntimeException("Unsupported type "+tn+" for newObject");
	}

	@Override
	public void removeChild(Object uoParent, Object uoFils) {
		throw new UnsupportedOperationException("La suppression n'est pas encore implémentée");
	}

	public ITagRepository getTagRepository() {
		return _tr;
	}

}
