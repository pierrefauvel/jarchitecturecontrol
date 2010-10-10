package org.jarco.control.specifications.editor.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserObject implements Serializable, Cloneable {
    public String _label;
    public int _id;
    private List<Object> _children = new ArrayList<Object>();
    private static int id = 0;

    public UserObject() {
      _label = "noname" + id;
      _id = id;
      id++;
    }

    @Override
    public Object clone() {
      UserObject rc = new UserObject();
      rc._label = _label;
      rc._id = id;
      id++;
      return rc;
    }

    public List<Object> getChildren()
    {
    	return _children;
    }
    
    @Override
    public String toString() {
      return _id + ":" + _label +"("+_children.size()+")";
    }
  }
