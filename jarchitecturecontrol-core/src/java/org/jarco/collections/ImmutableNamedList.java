package org.jarco.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jarco.code.external.INamed;

public class ImmutableNamedList<T extends INamed> implements Iterable<T> {
	
	private Map<String,T> m = new HashMap<String,T>();
	private List<T> lst;
	
	public ImmutableNamedList()
	{
		this(new ArrayList<T>());
	}
	
	public ImmutableNamedList(List<T> lst)
	{
		for(T n:lst)
		{
			m.put(n.getName(),n);
		};
		this.lst=lst;
	}
	public Collection<String> getNames()
	{
		return m.keySet();
	}
	public T get(String name)
	{
		return m.get(name);
	}
	public Iterator iterator() {
		return new Iterator()
		{
			int i=0;
			public boolean hasNext() {
				return i<lst.size();
			}
			public Object next() {
				Object rc = lst.get(i);
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
		return lst.toString();
	}

}
