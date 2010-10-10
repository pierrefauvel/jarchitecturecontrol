package org.jarco.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImmutableList<T> implements Iterable<T>{
	private List<T> l;
	
	public ImmutableList()
	{
		this(new ArrayList<T>());
	}
	
	public ImmutableList( List<T> lst)
	{
		if(lst==null)
		{
			new RuntimeException("La liste est nulle").printStackTrace();
		}
		l=lst;
	}
	public int count()
	{
		return l.size();
	}
	public T get(int i)
	{
		return l.get(i);
	}
	public Iterator iterator() {
		return new Iterator()
		{
			int i=0;
			public boolean hasNext() {
				return i<l.size();
			}
			public Object next() {
				Object rc = l.get(i);
				i++;
				return rc;
			}
			public void remove() {
				throw new RuntimeException("FList is readonly");
			}
		};
	}

	public String toString()
	{
		return l.toString();
	}
	
}
