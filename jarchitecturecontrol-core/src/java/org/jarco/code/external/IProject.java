package org.jarco.code.external;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.jarco.code.internal.CodeRepositoryInternal;
import org.jarco.collections.ImmutableList;
import org.jarco.collections.ImmutableNamedCollection;
import org.jarco.collections.ImmutableNamedList;
import org.jarco.collections.ImmutableNamedSet;

public interface IProject extends INamed, ICodeElement{
	
	public static FileFilter ARCHIVE_FILE_FILTER = new FileFilter(){
		public boolean accept(File pathname) {
			if(pathname.getName().endsWith("jar"))
				return true;
			if(pathname.getName().endsWith("war"))
				return true;
			if(pathname.getName().endsWith("ear"))
				return true;
			return false;
		}
	};

	public String getName();
	public ImmutableList<IClass> getClasses();
	public ImmutableNamedSet<IPackage> getPackages();
	public ICodeRepository getRepository();
	public byte[] loadClassByteCode(String name, String pn) throws IOException;
	public ImmutableList<IXmlDocument> getXmlDocuments();
	public ImmutableList<IPropertiesDocument> getPropertiesDocuments();
	public IProject getParentProject();
	public IRepositorySPIRef getRef();
}
