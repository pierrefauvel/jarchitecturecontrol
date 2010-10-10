package org.jarco.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jarco.code.external.INamed;

public class ImmutableNamedMap<T extends INamed> implements Iterable<T> {

	private Map<String,T> map;

	public ImmutableNamedMap()
	{
		map=new HashMap<String,T>();
	}

	public ImmutableNamedMap(Map<String,T> map)
	{
		this.map=map;
	}
	public ImmutableNamedMap(Collection<T> col)
	{
		map = new HashMap<String,T>();
		for(T t:col)
		{
			map.put(t.getName(), t);
		}
	}
	
	
	public ImmutableCollection<T> values(){
		return new ImmutableCollection<T>(map.values());
	}
	
//	public void add(T named)
//	{
//		map.put(named.getName(), named);
//	}
	
	public T getByName(String name)
	{
		return map.get(name);
	}

	public String toString()
	{
		return map.toString();
	}

	public Iterator<T> iterator() {
		final Iterator<T> it = map.values().iterator();
		return new Iterator<T>()
		{
			public boolean hasNext() {
				return it.hasNext();
			}
			public T next() {
				return it.next();
			}
			public void remove() {
				throw new RuntimeException("FCollection is readonly");
			}
		};
	}

}
