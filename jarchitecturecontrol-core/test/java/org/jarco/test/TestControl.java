package org.jarco.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Properties;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.jarco.code.external.IProject;
import org.jarco.code.internal.CodeRepositoryInternal;
import org.jarco.code.internal.maven.MavenRef;
import org.jarco.code.internal.maven.MavenRepositorySPI;
import org.jarco.configuration.Configuration;
import org.jarco.configuration.ConfigurationSet;
import org.jarco.control.report.ControlGReport;
import org.jarco.control.report.DependenciesGReport;
import org.jarco.control.report.TagGReport;
import org.jarco.control.report.ViolationGReport;
import org.jarco.control.report.genericreport.GenericReport;
import org.jarco.control.report.itf.IDependenciesReport;
import org.jarco.control.specifications.ControlResult;
import org.jarco.control.specifications.model.Specification;
import org.jarco.swing.JMainFrame;
import org.jarco.swing.JReportViewer;
import org.jarco.swing.components.JTreeEditor;
import org.jarco.tags.internal.TagRepositoryInternal;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

public class TestControl {

	static File tempdir = new File("target/temp");
	

	static ControlGReport cfr ;
	static ViolationGReport vr ;
	static TagGReport tr ;
	static IDependenciesReport dr;
	
	@Test
	public void test() throws IOException, IllegalArgumentException, SecurityException, XPathExpressionException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SAXException, TransformerException, InterruptedException
	{
		dr = new DependenciesGReport(tempdir,"test-analysis");
		
		Properties p = new Properties();
		p.load(new FileInputStream("TestAnalysis.conf.properties"));
		String repoPath = p.getProperty("repoPath");
		String jdkPath = p.getProperty("jdkPath");
		
		MavenRef[] mr = new MavenRef[]{
				new MavenRef(repoPath,"org.jarco","test-analysis", "0.1-SNAPSHOT","jar")
		};
		MavenRepositorySPI mrspi = new MavenRepositorySPI(dr,repoPath,mr);
		final CodeRepositoryInternal cr=new CodeRepositoryInternal(
				dr,
				jdkPath,
				mrspi
		);
		cr.flush();
		
		TagRepositoryDeTest trepo = new TagRepositoryDeTest();
		SpecficationDeTest es = new SpecficationDeTest(trepo);
		
		for(MavenRef mri : mr)
		{
			IProject prj = cr.findProject(mri,true);
		}
		
		cfr = new ControlGReport(tempdir,"test-analysis");
		vr = new ViolationGReport(tempdir,"test-analysis");
		ControlResult ctr = new ControlResult(cfr,vr);
		es.visit(cr, ctr);
		tr = new TagGReport(tempdir,"test-analysis");
		trepo.report(tr);
		dr.close();
		GenericReport gr = vr.getReport();

	    Configuration cfg=new Configuration();
	    cfg.jdkPath="C:\\Program Files\\Java\\jdk1.6.0_10";
	    cfg.prefix="test-analysis";
	    cfg.repopath="D:\\Mon maven2\\repository";
	    cfg.mr=Arrays.asList(new MavenRef[]{
				new MavenRef(repoPath,"org.jarco","test-analysis", "0.1-SNAPSHOT","jar")
		});
	    ConfigurationSet cfgs = new ConfigurationSet();
	    cfgs.add(cfg);
	    JTreeEditor.saveAllToFile("configuration", "configuration", cfgs);
	    JTreeEditor.saveAllToFile("tagrepository", "tagrepository", trepo);
	    JTreeEditor.saveAllToFile("specification", "specification", es);
    	
		Assert.assertEquals(5,trepo.getTagTypes().get("ApplicativeCode").getTags().count()); // ClasseA, ClasseB, ClasseC, Interface D, ClasseA$Log1
		Assert.assertEquals(2,trepo.getTagTypes().get("Annotation").getTags().count()); // Property, Agent
		Assert.assertEquals(3,gr.getRoot().getSubNodes().size()); // le champ public static pas final, le champ propriété public, l'appel à System.out.println()

	/**
Restent à tester
		ChainedProductionRule.java
		ContextAffectationFromProperties.java
		ContextAffectationFromXPath.java
		FilterFromXPath.java
		FollowAssertion.java
		TagPredicate.java
	 */
	
		//TODO 0.1 ajouter une page dans le site disant : vous avez ce code, vous voudriez atteindre ces conclusions, comment faire
	
	}
	
}
