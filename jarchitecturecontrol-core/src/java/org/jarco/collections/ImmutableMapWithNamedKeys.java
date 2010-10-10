package org.jarco.collections;

import java.util.HashMap;
import java.util.Map;

import org.jarco.code.external.INamed;

public class ImmutableMapWithNamedKeys<T1 extends INamed, T2> {
	
	private Map<T1,T2> hm;
	
	public ImmutableMapWithNamedKeys()
	{
		this(new HashMap<T1,T2>());
	}
	
	public ImmutableMapWithNamedKeys(Map<T1,T2> s)
	{
		hm=s;
	}
	
	public ImmutableNamedCollection<T1> keys(){
		return new ImmutableNamedCollection<T1>(hm.keySet());
	}
	public T2 get(T1 key)
	{
		return hm.get(key);
	}
}
