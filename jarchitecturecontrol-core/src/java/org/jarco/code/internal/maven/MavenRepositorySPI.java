package org.jarco.code.internal.maven;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.jarco.code.external.IRepositorySPI;
import org.jarco.code.external.IRepositorySPIRef;
import org.jarco.collections.ImmutableList;
import org.jarco.control.report.itf.IDependenciesReport;

public class MavenRepositorySPI implements IRepositorySPI {

	private String repoPath;
	private MavenRef[] mavenComponents;
	private IDependenciesReport pw;
	public MavenRepositorySPI( IDependenciesReport pw,String repoPath, MavenRef[] mavenComponents)
	{
		this.pw=pw;
		this.repoPath=repoPath;
		this.mavenComponents=mavenComponents;
	}
	
	public String getName()
	{
		return repoPath;
	}

	public ImmutableList<IRepositorySPIRef> getCanonicalComponents() {
		List<IRepositorySPIRef> lst = new ArrayList<IRepositorySPIRef>();
//		lst.add(new MavenRef(repoPath,pw,"bcel","bcel","5.1","jar"));
		lst.add(new MavenRef(repoPath,"bcel","bcel","5.1","jar"));
		return new ImmutableList(lst);
	}

	public ImmutableList<IRepositorySPIRef> getComponents() {
		List<IRepositorySPIRef> lst = new ArrayList<IRepositorySPIRef>();
		for(MavenRef mr : mavenComponents)
			lst.add(mr);
		return new ImmutableList(lst);
	}

}
