package org.jarco.collections;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ImmutableCollection<T> implements Iterable<T>{
	private Collection<T> l;
	
	public ImmutableCollection()
	{
		this(new HashSet<T>());
	}
	
	public ImmutableCollection( Collection<T> lst)
	{
		l=lst;
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
				throw new RuntimeException("FCollection is readonly");
			}
		};
	}

	public String toString()
	{
		return l.toString();
	}
}
