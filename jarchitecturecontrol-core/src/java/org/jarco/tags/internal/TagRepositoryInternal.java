package org.jarco.tags.internal;

import java.util.HashSet;
import java.util.Set;

import org.jarco.collections.ImmutableNamedList;
import org.jarco.collections.ImmutableNamedSet;
import org.jarco.control.report.Indent;
import org.jarco.control.report.TagReport;
import org.jarco.tags.external.ITag;
import org.jarco.tags.external.ITagAssociation;
import org.jarco.tags.external.ITagAssociationType;
import org.jarco.tags.external.ITagRepository;
import org.jarco.tags.external.ITagRoleType;
import org.jarco.tags.external.ITagType;

public class TagRepositoryInternal implements ITagRepository{

	private Set<ITagType> hs_types = new HashSet<ITagType>();
	private Set<ITagAssociationType> hs_associations = new HashSet<ITagAssociationType>();
		
	public ImmutableNamedSet<ITagType> getTagTypes() {
		return new ImmutableNamedSet<ITagType>(hs_types);
	}

	public ITagType newTagType(String string) {
		ITagType tag = new TagTypeInternal(string);
		hs_types.add(tag);
		return tag;
	}

	public void report(TagReport tgr)
	{
		tgr.writeTagSection();
		for(ITagType tty : getTagTypes())
		{
			tgr.writeType(tty);
			for(ITag t : tty.getTags())
			{
				tgr.writeTag(t);
			}
		}
		tgr.writeAssociationSection();
		for(ITagAssociationType tat:getTagAssociationTypes())
		{
			tgr.writeAssociationType(tat);
			for(ITagAssociation as:tat.getInstances())
			{
				tgr.writeAssociation(as);
				for(ITagRoleType trt : tat.getRoles())
				{
					tgr.writeRole(as.getRole(trt));
				}
			}
		}
		tgr.close();
	}
	
	public ITagAssociationType newTagAssociationType(String name)
	{
		ITagAssociationType association = new TagAssociationTypeInternal(name);
		hs_associations.add(association);
		return association;
	}
	
	public ImmutableNamedSet<ITagAssociationType> getTagAssociationTypes()
	{
		return new ImmutableNamedSet<ITagAssociationType>(hs_associations);
	}
}
