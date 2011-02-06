package org.jarco.test;

import org.jarco.tags.external.ITagType;
import org.jarco.tags.internal.TagInternal;
import org.jarco.tags.internal.TagRepositoryInternal;

public class TagRepositoryDeTest extends TagRepositoryInternal {
	public ITagType isAnnotation ;
	public ITagType isApplicativeCode;
	public TagRepositoryDeTest()
	{
		isAnnotation = this.newTagType("Annotation");
		isApplicativeCode = this.newTagType("ApplicativeCode");
	}
	
	
}
