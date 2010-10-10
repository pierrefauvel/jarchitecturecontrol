package org.jarco.code.internal;

import java.lang.reflect.Type;
import java.util.List;

import org.jarco.code.external.IClass;
import org.jarco.code.external.ICodeRepository;
import org.jarco.code.external.IType;
import org.jarco.collections.ImmutableList;

public class TypeInternal extends ACodeElementInternal implements IType {

	private IClass baseClass;
	private IType baseType;
	private boolean isArray=false;
	private int dimension=0;
	private boolean isGeneric=false;
	private boolean isPrimitive=false;
	private List<IType> actualTypes;
	
	public TypeInternal(ICodeRepository repo)
	{
		super(repo);
	}
	
	public int getArrayDimenstion() {
		return dimension;
	}

	public IClass getBaseClass() {
		if(baseClass==null && baseType!=null)
		{
			return baseType.getBaseClass();
		}
		return baseClass;
	}
	public IType getBaseType() {
		return baseType;
	}

	public boolean isArray() {
		return isArray;
	}

	public boolean isGeneric() {
		return isGeneric;
	}

	public boolean isPrimitive() {
		return isPrimitive;
	}

	public ImmutableList<IType> getActualTypes() {
		return new ImmutableList<IType>(actualTypes);
	}

	public static IType newPrimitive(ICodeRepository repo,IClass wrapBaseClass) {
		TypeInternal ti = new TypeInternal(repo);
		ti.isPrimitive=true;
		ti.baseClass=wrapBaseClass;
		ti.baseType=newInstance(repo,wrapBaseClass);
		return ti;
	}

	public static IType newInstance(ICodeRepository repo,IClass wrapBaseClass) {
		TypeInternal ti = new TypeInternal(repo);
		ti.baseClass=wrapBaseClass;
		ti.baseType=ti;
		return ti;
	}

	public static IType newArray(ICodeRepository repo,IClass wrapBaseClass, int dimension) {
		TypeInternal ti = new TypeInternal(repo);
		ti.baseClass=wrapBaseClass;
		ti.dimension=dimension;
		ti.baseType=newInstance(repo,wrapBaseClass);
		ti.isArray=true;
		return ti;
	}

	public static IType newParameterized(ICodeRepository repo,IClass rtc,IType iType, List<IType> ct) {
		TypeInternal ti = new TypeInternal(repo);
		ti.baseClass=rtc;
		ti.baseType=iType;
		ti.actualTypes=ct;
		return ti;
	}

	public static IType newArray(ICodeRepository repo,IClass rtc,IType wrap, int dimension) {
		TypeInternal ti = new TypeInternal(repo);
		ti.baseClass=rtc;
		ti.baseType=wrap;
		ti.isArray=true;
		ti.dimension=dimension;
		return ti;
	}
	
	public static IType newPrimitiveArray(ICodeRepository repo, IType type, int dimension){
		TypeInternal ti = new TypeInternal(repo);
		ti.baseClass=type.getBaseClass();
		ti.baseType=type;
		ti.isArray=true;
		ti.dimension=dimension;
		return ti;
	}

	public boolean equals(Object obj){
		return getLongName().compareTo(((TypeInternal)obj).getLongName())==0;
	}
	
	public int hashCode()
	{
		return getLongName().hashCode();
	}

	public String getName()
	{
		return getLongName();
	}
	
	private String _ln;
	public String getLongName() {
		if(_ln!=null)
			return _ln;
		StringBuffer sb=new StringBuffer();
		if(isPrimitive)
		{
			sb.append(getBaseClass().getName());
			sb.append(":primitive");
		}
		else if(isArray)
		{
			if(baseClass!=null)
			{
				sb.append(getBaseClass().getLongName());
				sb.append(":array[");
				sb.append(dimension);
				sb.append("]");
			}
			else
			{
				sb.append(getBaseType().getLongName());
				sb.append(":array[");
				sb.append(dimension);
				sb.append("]");
			}
		}
		else if(isGeneric)
		{
			sb.append(getBaseType());
			sb.append(":generic<");
			for(int i=0;i<actualTypes.size();i++)
			{
				if(i>=0) sb.append(",");
				sb.append(actualTypes.get(i));
			}
			sb.append(">");
		}
		else
		{
			sb.append(getBaseClass().getLongName());
		};
		_ln=sb.toString();
		return _ln;
	}

	public String toString(){
		return getLongName();
	}
}
