package org.jarco.tags.external;

import org.jarco.code.external.ICodeElement;
import org.jarco.collections.ImmutableNamedList;
import org.jarco.collections.ImmutableNamedSet;
import org.jarco.control.report.TagReport;

public interface ITagRepository {
	public ImmutableNamedSet<ITagType> getTagTypes();
	public ITagType newTagType(String name);
	public ITagAssociationType newTagAssociationType(String name);
	public ImmutableNamedSet<ITagAssociationType> getTagAssociationTypes();
	public void report(TagReport tgr);
}
