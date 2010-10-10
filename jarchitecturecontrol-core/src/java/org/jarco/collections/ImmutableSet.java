package org.jarco.collections;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jarco.code.external.EModifier;

public class ImmutableSet<T> implements Iterable<T>{
	private Set<T> l;
	public ImmutableSet()
	{
		this(new HashSet<T>());
	}

	public ImmutableSet( Set<T> lst)
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
