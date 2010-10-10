/**
 * 
 */
package org.jarco.code.internal;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jarco.code.external.IClass;
import org.jarco.code.external.IPackage;
import org.jarco.code.external.IProject;
import org.jarco.collections.ImmutableList;
import org.jarco.collections.ImmutableSet;

final class AuxilliaryClassLoader extends ClassLoader {
	/**
	 * 
	 */
	private final CodeRepositoryInternal repo;
	//TODO V1.1 Implémenter les caches en utilisant les références
	private Map<String,Class> map_className2Class=new HashMap<String,Class>();

	AuxilliaryClassLoader(CodeRepositoryInternal repo, ClassLoader arg0) {
		super(arg0);
		this.repo = repo;
	}

	public synchronized Class loadClass(String longName, boolean resolve)
      throws ClassNotFoundException {
		Class c=map_className2Class.get(longName);
		if(c!=null)
			return c;
		byte[] data;
		String pn = NameUtilities.packageFromClassLongName(longName);
		String shortName = NameUtilities.classShortNameFromClassLongName(longName);
		ImmutableSet<IProject> projects = repo.resolveProjectForPackage(pn);
		if(projects==null)
			throw new RuntimeException("PF104 No project found for package "+pn);
		loop: for(IProject project:projects)
		{
			if(project.equals(repo.getSystemProject()))
			{
//			System.out.println("PIF05 Calling parent class loader for class "+longName+" in package "+pn);
			IPackage pkg = repo.getSystemProject().getPackages().get(pn);
			
			//inutile puisqu'il y a un pré-chargement ?
//			IClass ic = new ClassImpl(SystemProject.instance(),pkg,longName,shortName,null,null);
//			((PackageImpl)pkg).addClass((ClassImpl)ic);

				c=getParent().loadClass(longName);
				map_className2Class.put(longName,c);
				return c;
			};
//		  System.out.println("PIF04 Loading class "+longName+" in project "+project);
			  try {
				data = project.loadClassByteCode(longName,pn);
			} catch (IOException ex) {
//				throw new ClassNotFoundException("IOException while loading class "+name+" from project "+project.getName()+":"+ex.getMessage(),ex);
				continue loop;
			}
			c= defineClass(longName,data, 0, data.length);
			map_className2Class.put(longName,c);
			return c;
		};
		throw new ClassNotFoundException("Could not find class "+longName+" in any project");
	  }
}