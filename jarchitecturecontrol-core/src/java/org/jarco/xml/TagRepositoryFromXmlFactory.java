package org.jarco.xml;

import org.jarco.code.internal.maven.MavenRef;
import org.jarco.configuration.Configuration;
import org.jarco.configuration.ConfigurationSet;
import org.jarco.tags.internal.TagAssociationInternal;
import org.jarco.tags.internal.TagAssociationTypeInternal;
import org.jarco.tags.internal.TagAttributeTypeInternal;
import org.jarco.tags.internal.TagInternal;
import org.jarco.tags.internal.TagRepositoryInternal;
import org.jarco.tags.internal.TagRoleInternal;
import org.jarco.tags.internal.TagRoleTypeInternal;
import org.jarco.tags.internal.TagTypeInternal;

public class TagRepositoryFromXmlFactory extends FromXmlFactory {

	public TagRepositoryFromXmlFactory ()
	{
//		register(TagAssociationInternal.class);
		register(TagAssociationTypeInternal.class);
		register(TagAttributeTypeInternal.class);
//		register(TagInternal.class);
		register(TagRepositoryInternal.class);
//		register(TagRoleInternal.class);
		register(TagRoleTypeInternal.class);
		register(TagTypeInternal.class);
	}
}
