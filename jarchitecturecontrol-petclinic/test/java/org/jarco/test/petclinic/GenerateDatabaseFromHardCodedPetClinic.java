package org.jarco.test.petclinic;

import java.util.Arrays;

import org.jarco.code.internal.maven.MavenRef;
import org.jarco.configuration.Configuration;
import org.jarco.configuration.ConfigurationSet;
import org.jarco.control.specifications.model.Specification;
import org.jarco.swing.tree.JTreeEditor;
import org.jarco.tags.external.ITagRepository;
import org.jarco.tags.internal.TagRepositoryInternal;

public class GenerateDatabaseFromHardCodedPetClinic {
	public static void main(String[] args)
	{
	    Configuration cfg=new Configuration();
	    cfg.jdkPath="C:\\Program Files\\Java\\jdk1.6.0_10";
	    cfg.prefix="ecites";
	    cfg.repopath="D:\\Mon maven2\\repository";
	    cfg.mr=Arrays.asList(new MavenRef[]{
				new MavenRef("D:\\Mon maven2\\repository","org.springframework.javaconfig","petclinic","1.0.0.M4-SNAPSHOT","war")
		});
	    ConfigurationSet cfgs = new ConfigurationSet();
	    cfgs.add(cfg);
	    //TODO v1.0 adopter comme directory & name prefix le nom de l'entité principale
	    //TODO v1.0 sortir le code de sauvegarde et restore du jtree editor
	    JTreeEditor.saveAllToFile("configuration", "configuration", cfgs);
	    
	    //supprimer la distinction external/internal pour les tags
	    TagRepositoryInternal tr = (TagRepositoryInternal)(IPetClinicTagConstants.tr);
	    JTreeEditor.saveAllToFile("tagrepository", "tagrepository", tr);
	    
	    Specification s = new PetClinicSpecification();
	    JTreeEditor.saveAllToFile("specification", "specification", s);
	    
	    
	}
}
