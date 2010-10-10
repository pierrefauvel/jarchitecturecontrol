package org.jarco.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jarco.code.external.INamed;

public class ImmutableNamedSet<T extends INamed> implements Iterable<T> {
	private Map<String,T> m = new HashMap<String,T>();
	private Set<T> l;
	
	public ImmutableNamedSet()
	{
		this(new HashSet<T>());
	}
	public ImmutableNamedSet( Set<T> lst)
	{
		for(T n:lst)
		{
			m.put(n.getName(),n);
		};
		l=lst;
	}
	public Collection<String> getNames()
	{
		return m.keySet();
	}
	public T get(String name)
	{
		return m.get(name);
	}
	public int count()
	{
		return l.size();
	}
	public Iterator iterator() {
		final Iterator it = l.iterator();
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
		return l.toString();
	}
	public boolean contains(T t) {
		return l.contains(t);
	}
}
