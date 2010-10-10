package org.jarco.test.petclinic;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;


import org.jarco.code.internal.maven.MavenRef;
import org.jarco.control.specifications.SpecificationModelInterface;
import org.jarco.swing.Configuration;
import org.jarco.swing.JTreeEditor;
import org.jarco.swing.ModelInterface;
import org.xml.sax.SAXException;

public class MainTesPetClinic_IHM_FirstTime {
	public static void main(String[] args) throws IllegalArgumentException, SecurityException, XPathExpressionException, IOException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SAXException
	{
        Map<String,Object> map = new HashMap<String,Object>();
      PetClinicSpecification bs = new PetClinicSpecification();
      
      map.put(org.jarco.tags.external.ITagRepository.class.getName(), new PetClinicSpecification());
	    final ModelInterface mi = new SpecificationModelInterface(bs);
	    Configuration cfg=new Configuration();
	    cfg.jdkPath="C:\\Program Files\\Java\\jdk1.6.0_10";
	    cfg.prefix="ecites";
	    cfg.repopath="D:\\Mon maven2\\repository";
	    cfg.mr=new MavenRef[]{
				new MavenRef("D:\\Mon maven2\\repository",cfg.getDependenciesReport(),"org.springframework.javaconfig","petclinic","1.0.0.M4-SNAPSHOT","war")
		};
	    JTreeEditor.main(mi,map, IPetClinicTagConstants.tr,cfg);
		
	}
}
