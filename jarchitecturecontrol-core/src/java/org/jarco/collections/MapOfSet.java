package org.jarco.collections;
import java.util.*;

public class MapOfSet<T1,T2> {
	
	private Map<T1,Set<T2>> m = new HashMap<T1,Set<T2>>();

	public void putAll(T1 key, Collection<T2> values)
	{
		for(T2 t2:values)
		{
			put(key,t2);
		}
	}
	public void put(T1 key,T2 value) {
		Set<T2> s = m.get(key);
		if(s==null)
		{
			s=new HashSet<T2>();
			m.put(key,s);
		}
		s.add(value);
	}
	public Set<T2> get(T1 key){
		Set<T2> s = m.get(key);
		if(s==null)
		{
			return new HashSet<T2>();
		};
		return s;
	}
	public Set<T1> keySet()
	{
		return m.keySet();
	}
	public boolean containsKey(String key)
	{
		return m.containsKey(key);
	}
}
