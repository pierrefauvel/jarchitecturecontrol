package org.jarco.code.internal;

import java.io.IOException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.jarco.code.external.ICodeRepository;
import org.jarco.code.external.IPackage;
import org.jarco.code.external.IProject;
import org.jarco.code.external.IPropertiesDocument;
import org.jarco.collections.ImmutableNamedSet;
import org.jarco.collections.ImmutableSet;
import org.jarco.tags.external.ITag;

public class PropertiesDocumentInternal extends ACodeElementInternal implements IPropertiesDocument {

	private IProject project;
	private String archiveFileName;
	private String name;
	private Map<String,String> peer;
	
	public PropertiesDocumentInternal(ICodeRepository repo, ProjectInternal projectImpl,
			String archiveFileName, String jen) {
		super(repo);
		project=projectImpl;
		this.archiveFileName=archiveFileName;
		name=jen;
	}

	public String getName()
	{
		return name;
	}
	
	public String toLabel()
	{
		return name;
	}
	
	public String get(String key) {
		try
		{
		analyzeProperties();
		return peer.get(key);
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	public IProject getProject() {
		return project;
	}

	public ImmutableSet<String> keys() {
		try
		{
		analyzeProperties();
		return new ImmutableSet<String>(peer.keySet());
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	private void analyzeProperties() throws IOException {
		if(peer==null)
		{
			JarFile jf = new JarFile(archiveFileName);
			JarEntry je = jf.getJarEntry(name);
			peer = new HashMap<String,String>();
			Properties props = new Properties();
			props.load(jf.getInputStream(je));
			for(Object k : props.keySet())
			{
				peer.put((String)k,(String)(props.getProperty((String)k)));
			}
		};
	}


}
