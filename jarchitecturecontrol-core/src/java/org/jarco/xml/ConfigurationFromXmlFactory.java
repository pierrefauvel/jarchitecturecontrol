package org.jarco.xml;

import org.jarco.code.internal.maven.MavenRef;
import org.jarco.configuration.Configuration;
import org.jarco.configuration.ConfigurationSet;

public class ConfigurationFromXmlFactory extends FromXmlFactory {

	public ConfigurationFromXmlFactory ()
	{
		register(ConfigurationSet.class);
		register(Configuration.class);
		register(MavenRef.class);
	}
}
