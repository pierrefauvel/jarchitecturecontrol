package org.jarco.collections;

import java.util.HashMap;
import java.util.Map;

import org.jarco.code.external.INamed;

public class ImmutableMap<T2, T> {

	private Map<T2,T> map;

	public ImmutableMap()
	{
		map=new HashMap<T2,T>();
	}

	public ImmutableMap(Map<T2,T> map)
	{
		this.map=map;
	}
	
	public ImmutableCollection<T> values(){
		return new ImmutableCollection<T>(map.values());
	}
	public ImmutableCollection<T2> keys(){
		return new ImmutableCollection<T2>(map.keySet());
	}
	
	public T get(T2 name)
	{
		return map.get(name);
	}

	public String toString()
	{
		StringBuffer sb=new StringBuffer();
		for(T2 key:map.keySet())
		{
			sb.append(key+"="+map.get(key)+" ");
		};
		return sb.toString();
	}

	public boolean containsKey(String name) {
		return map.containsKey(name);
	}
}
