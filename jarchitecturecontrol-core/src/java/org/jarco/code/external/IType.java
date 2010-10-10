package org.jarco.code.external;

import org.jarco.collections.ImmutableList;

public interface IType extends ICodeElement{
	public boolean isPrimitive();
	public boolean isArray();
	public int getArrayDimenstion();
	public boolean isGeneric();
	public ImmutableList<IType> getActualTypes();
	public IClass getBaseClass();
	public IType getBaseType();
	public String getLongName();
}
