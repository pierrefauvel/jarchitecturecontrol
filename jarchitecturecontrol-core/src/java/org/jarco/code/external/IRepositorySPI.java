package org.jarco.code.external;

import org.jarco.collections.ImmutableList;

public interface IRepositorySPI {
	
	public String getName();
	public ImmutableList<IRepositorySPIRef> getCanonicalComponents();
	public ImmutableList<IRepositorySPIRef> getComponents();
	
}
