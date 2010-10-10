package org.jarco.control.specifications.editor.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jarco.swing.ModelInterface;

public class UserObjectModelInterface  implements ModelInterface{

		private UserObject root;
		
		@Override
		public boolean acceptChild(Object uoPere, Object uoFils) {
			if(uoPere instanceof UserNote)
				return false;
			return true;
		}

		public void addChild(Object uoParent, Object uoFils) {
			((UserObject)uoParent).getChildren().add(uoFils);
		}

		@Override
		public void removeChild(Object uoParent, Object uoFils) {
			((UserObject)uoParent).getChildren().remove(uoFils);
		}

		public Object getRoot()
		{
			if(root==null)
			{
				root = new UserObject();
				root.getChildren().add(new UserNote());
			};
			return root;
		}
		
		public Object newObject(String tn)
		{
			if(tn.compareTo("UserObject")==0)
			{
	        UserObject uo_fils = new UserObject();
	        return uo_fils;
			}
			if(tn.compareTo("UserNote")==0)
			{
				UserNote uo_note = new UserNote();
				return uo_note;
			}
			throw new UnsupportedOperationException("Unexpected type name "+tn);
		}
		
		public Object clone(Object original)
		{
			Object clone = ((UserObject)original).clone();
			return clone;
		}
		
		public String[] getTypes()
		{
			return new String[]{ "UserObject", "UserNote" };
		}

		@Override
		public boolean acceptChildType(Object pere, String typeFils) {
			System.out.println(pere.getClass()+"/"+typeFils+"/");
			if(pere instanceof UserObject)
				return true;
			if(pere instanceof UserNote)
				return false;
			return false;
		}

		private static final List EMPTY_LIST = new ArrayList();
		
		@Override
		public List getChildren(Object uo) {
			if(uo instanceof UserNote)
				return EMPTY_LIST;
			if(uo instanceof UserObject)
			{
				UserObject uoi = (UserObject)uo;
				return uoi.getChildren();
			};
			return EMPTY_LIST;
		}
		
		public String getGroup(String t)
		{
			return null;
		}
}
