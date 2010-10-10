package org.jarco.control.specifications.editor.test;

import java.io.Serializable;
import java.util.List;

public class UserNote implements Serializable, Cloneable{

	private String t;
	
	public UserNote()
	{
		t=""+System.currentTimeMillis();
	}
	
    @Override
    public Object clone() {
    	return new UserNote();
    }
    
    @Override
    public String toString() {
    	return t;
    }
}
