package org.jarco.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jarco.code.external.INamed;

public class ImmutableNamedCollection<T extends INamed> implements Iterable<T> {
	
	private Map<String,T> m = new HashMap<String,T>();
	
	public ImmutableNamedCollection()
	{
		this(new HashSet<T>());
	}	
	public ImmutableNamedCollection(Collection<T> lst)
	{
		for(T n:lst)
		{
			m.put(n.getName(),n);
		};
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
		final Iterator it = m.values().iterator();
		return new Iterator()
		{
			public boolean hasNext() {
				return it.hasNext();
			}
			public Object next() {
				return it.next();
			}
			public void remove() {
				throw new RuntimeException("FSet is readonly");
			}
		};
	}

	public String toString()
	{
		return m.toString();
	}

}
