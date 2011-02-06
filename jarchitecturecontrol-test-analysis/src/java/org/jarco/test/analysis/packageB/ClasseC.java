package org.jarco.test.analysis.packageB;

import org.jarco.test.analysis.packageA.ClasseA;

public class ClasseC<T extends ClasseA> {
	public String wrap(T t){
		return t.toString();
	}
	public T unwrap(String s)
	{
		return (T) new ClasseA();
	}
}
