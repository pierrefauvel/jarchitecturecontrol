package org.jarco.test.petclinic;

import org.jarco.tags.external.ITagAssociationType;
import org.jarco.tags.external.ITagRepository;
import org.jarco.tags.external.ITagType;
import org.jarco.tags.internal.TagRepositoryInternal;

public interface IPetClinicTagConstants {

	static final ITagRepository tr= new TagRepositoryInternal();
	static final ITagType tg_tier = tr.newTagType("Tier").newAttribute("name");
//TODO V1.1 Reg-exp pour extraire les 2 attributs suivants
//	tg_tier.newAttribute("version");
//	tg_tier.newAttribute("extension");
	static final ITagType tg_layer = tr.newTagType("Layer")
		.newAttribute("name");
	static final ITagType tg_pattern = tr.newTagType("Pattern")
		.newAttribute("name");
	static final ITagAssociationType tgat_dao = tr.newTagAssociationType("dao")
		.newRoleType("interface", tg_pattern)
		.newRoleType("implementation", tg_pattern);
	static final ITagAssociationType tgat_service = tr.newTagAssociationType("service")
		.newRoleType("interface", tg_pattern)
		.newRoleType("implementation", tg_pattern);
	static final ITagType tg_basicJdk = tr.newTagType("BasicJdk");
	static final ITagType tg_basicApache = tr.newTagType("BasicApache");
	
	static final ITagType tg_referenced_by_spring_as_dao = tr.newTagType("Referenced_by_spring_as_dao");
	static final ITagType tg_referenced_by_spring_as_entity = tr.newTagType("Referenced_by_spring_as_entity");
	static final ITagType tg_referenced_by_spring_as_service = tr.newTagType("Referenced_by_spring_as_service");
	static final ITagType tg_referenced_by_spring_as_action = tr.newTagType("Referenced_by_spring_as_action");
	static final ITagType tg_referenced_by_hibernate_as_dialect = tr.newTagType("Referenced_by_hibernate_as_dialect");

}
