package org.jarco.test.petclinic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.xml.xpath.XPathExpressionException;

import org.jarco.code.external.IClass;
import org.jarco.code.external.IMethod;
import org.jarco.code.external.IRepositorySPIRef;
import org.jarco.code.internal.CodeRepositoryInternal;
import org.jarco.code.internal.maven.MavenRef;
import org.jarco.code.internal.maven.MavenRepositorySPI;
import org.jarco.control.report.AnalysisReport;
import org.jarco.control.report.DependenciesReport;
import org.jarco.control.report.ResultReport;
import org.jarco.control.report.SpecificationReport;
import org.jarco.control.report.TagReport;
import org.jarco.control.report.filesystem.FileSystemViolationReport;
import org.jarco.control.specifications.ControlResult;
import org.xml.sax.SAXException;

public class MainTestPetClinic {
	public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, XPathExpressionException, SAXException
	{
		File reports = new File("reports");
		reports.mkdirs();

		DependenciesReport dr = new DependenciesReport(new FileWriter("reports/petclinic.ja2dependencies"));
		MavenRef[] mr = new MavenRef[]{
				new MavenRef("D:\\Mon maven2\\repository",dr,"org.springframework.javaconfig","petclinic","1.0.0.M4-SNAPSHOT","war")
		};
		MavenRepositorySPI mrspi = new MavenRepositorySPI(dr,"D:\\Mon maven2\\repository",mr);
		CodeRepositoryInternal cr=new CodeRepositoryInternal(
				dr,
				"C:\\Program Files\\Java\\jdk1.6.0_10",
				mrspi
		);
		cr.flush();
		
		System.out.println("Loading web project");
		AnalysisReport.report(reports,cr.findProject(mr[0],true));
		
		PetClinicSpecification bs = new PetClinicSpecification();
		System.out.println("[Specification report]");
		bs.dump(new SpecificationReport(reports,"petclinic"));
		
		FileWriter fw=new FileWriter(new File("reports/petclinic.specification.xml"));
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(bs.toXml());
		bw.close();
		
		System.out.println("[Control, result & violation reports]");
		ResultReport rr = new ResultReport(reports,"petclinic");
		FileSystemViolationReport vr = new FileSystemViolationReport(reports,"petclinic");
		ControlResult ctr = new ControlResult(rr,vr);
		bs.visit(cr, ctr);
		System.out.println("[Tag report]");
		TagReport tgr = new TagReport(reports,"petclinic");
		bs.tr.report(tgr);
		
		dr.close();
	}
}
