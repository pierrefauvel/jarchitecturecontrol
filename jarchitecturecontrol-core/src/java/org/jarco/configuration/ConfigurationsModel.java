package org.jarco.configuration;

import java.util.ArrayList;
import java.util.Arrays;

import org.jarco.code.internal.maven.MavenRef;
import org.jarco.swing.tree.ModelInterface;
import org.jarco.tags.external.ITagRepository;

public class ConfigurationsModel implements ModelInterface{

	private ConfigurationSet cs;
	private Configuration selectedConfiguration;
	private String repoPath;
	
	public ConfigurationsModel(ConfigurationSet cs, String repoPath)
	{
		this.cs=cs;
		this.repoPath=repoPath;
	}
	
	public void setRoot ( Object root)
	{
		cs = (ConfigurationSet)root;
	}
	
	@Override
	public boolean acceptChild(Object uoPere, Object uoFils) {
		if(uoPere instanceof ConfigurationSet)
			return uoFils instanceof Configuration;
		if(uoPere instanceof Configuration)
			return uoFils instanceof MavenRef;
		return false;
	}

	@Override
	public boolean acceptChildType(Object pere, String typeFils) {
		if(pere instanceof ConfigurationSet)
			return typeFils.compareTo("Configuration")==0;
		if(pere instanceof Configuration)
			return typeFils.compareTo("MavenRef")==0;
		return false;
	}

	@Override
	public void addChild(Object uoParent, Object uoFils) {
		if(uoParent instanceof ConfigurationSet)
		{
			((ConfigurationSet)uoParent).add((Configuration)uoFils);
		}
		else if(uoParent instanceof Configuration)
		{
			((Configuration)uoParent).add((MavenRef)uoFils);
		}
		else
			throw new RuntimeException("Could not add "+uoFils+" to "+uoParent);
	}

	@Override
	public Object clone(Object original) {
		throw new UnsupportedOperationException("Clone not yet supported");
	}

	@Override
	public Iterable getChildren(Object uo) {
		if(uo instanceof ConfigurationSet)
		{
			return ((ConfigurationSet)uo).getChildren();
		}
		else if(uo instanceof Configuration)
		{
			return Arrays.asList(((Configuration)uo).getMavenComponentReferences());
		}
		else if(uo instanceof MavenRef)
		{
			return new ArrayList();
		}
		else
			throw new RuntimeException("Unexpected child type "+uo);
	}

	@Override
	public String getGroup(String type) {
		return "Configuration Items";
	}

	@Override
	public Object getRoot() {
		return cs;
	}

	@Override
	public String[] getTypes() {
		return new String[]{ "Configuration", "MavenRef" };
	}

	@Override
	public Object newObject(String tn) {
		if(tn.compareTo("Configuration")==0)
			return new Configuration();
		if(tn.compareTo("MavenRef")==0)
			return new MavenRef(repoPath);
		throw new RuntimeException("Unexpected child type "+tn);
	}

	@Override
	public void removeChild(Object uoParent, Object uoFils) {
		if(uoParent instanceof ConfigurationSet)
		{
			((ConfigurationSet)uoParent).remove((Configuration)uoFils);
		}
		else if(uoParent instanceof Configuration)
		{
			((Configuration)uoParent).remove((MavenRef)uoFils);
		}
		
	}

	public Configuration getSelectedConfiguration() {
		return selectedConfiguration;
	}
	public void setSelectedConfiguration(Configuration configuration) {
		selectedConfiguration=configuration;
	}

}
