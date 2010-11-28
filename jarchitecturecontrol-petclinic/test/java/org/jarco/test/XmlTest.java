package org.jarco.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jarco.control.specifications.model.Specification;
import org.jarco.persistence.FromXmlFactory;
import org.jarco.persistence.SpecificationFromXmlFactory;
import org.jarco.tags.external.ITagRepository;
import org.jarco.test.petclinic.IPetClinicTagConstants;
import org.jarco.test.petclinic.PetClinicSpecification;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XmlTest {
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException
	{
		ITagRepository tr ;
		{
			PetClinicSpecification bs = new PetClinicSpecification();
			tr=IPetClinicTagConstants.tr;
			FileWriter fw=new FileWriter(new File("reports/petclinic.specification.xml"));
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(bs.toXml());
			bw.close();
		}
		
		FromXmlFactory fxf = new SpecificationFromXmlFactory(tr);
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document d = db.parse(new File("reports/petclinic.specification.xml"));
		
		Specification s = (Specification) fxf.fromXml(d.getDocumentElement());
	}
}
