package org.jarco.swing.tree;

import java.util.List;
import java.util.Set;

public interface ModelInterface {

	void addChild(Object uoParent, Object uoFils);
	boolean acceptChild(Object uoPere, Object uoFils);
	void removeChild(Object uoParent, Object uoFils);
	public Object getRoot();
	public Object newObject(String tn);
	public Object clone(Object original);
	public String[] getTypes();
	public String getGroup(String type);
	public boolean acceptChildType(Object pere, String typeFils);
	public Iterable getChildren(Object uo);
	public void setRoot(Object root);
}
