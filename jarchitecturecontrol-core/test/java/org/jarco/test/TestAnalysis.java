package org.jarco.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.jarco.code.external.IProject;
import org.jarco.code.internal.CodeRepositoryInternal;
import org.jarco.code.internal.maven.MavenRef;
import org.jarco.code.internal.maven.MavenRepositorySPI;
import org.jarco.control.report.AnalysisReport;
import org.jarco.control.report.DependenciesGReport;
import org.jarco.control.report.genericreport.GenericReport;
import org.jarco.control.report.genericreport.ReportNode;
import org.jarco.control.report.itf.IDependenciesReport;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

public class TestAnalysis {

	static File tempdir = new File("target/temp");
	
	static
	{
		try
		{
		init();
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
	}

	@BeforeClass
	public static void init() throws IOException, IllegalArgumentException, SecurityException, XPathExpressionException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SAXException, TransformerException
	{
		IDependenciesReport dr = new DependenciesGReport(tempdir,"test-analysis");
		
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
		for(IProject prj : cr.getProjects())
		{
			if(prj.getName().compareTo("test-analysis-0.1-SNAPSHOT.jar")==0)
			{
				//System.out.println("PF207 reporting "+prj.getName());
				new AnalysisReport().report(tempdir, prj);
				//System.out.println("PF207 reported "+prj.getName());
			}
		}
	}
	
	@Test
	public void testFile() throws FileNotFoundException, SAXException, IOException, XPathExpressionException
	{
		GenericReport resources_report = new GenericReport(new FileInputStream(tempdir+"/test-analysis-0.1-SNAPSHOT.jar/resources.analysis.ja2.xml"));
		ReportNode[] n = resources_report.find("./node/node[@kind='xml-document']");
		Assert.assertEquals("Nb de documents xml",2,n.length);
		Assert.assertEquals("Nom du fichier","XmlF.xml",n[0].getDescription().getHtml());
		n = resources_report.find("./node/node[@kind='properties-document']");
		Assert.assertEquals("Nb de documents properties",2,n.length);
		Assert.assertEquals("Nom du fichier","PropertiesE.properties",n[0].getDescription().getHtml());
	}

	private static void checkClassName(GenericReport report, String pakkage, String clazz) throws XPathExpressionException
	{
		ReportNode[] rn_root = report.find("./node");
		Assert.assertEquals(rn_root.length,1);
		Assert.assertEquals("Class "+pakkage+"."+clazz,rn_root[0].getDetail().getHtml());
	}
	private static void checkSuperClassName(GenericReport report, String pakkage, String clazz) throws XPathExpressionException
	{
		ReportNode[] rn_root = report.find("./node/node[@kind='"+AnalysisReport.SUPER_CLASS+"']");
		Assert.assertEquals(rn_root.length,1);
		Assert.assertEquals("<b>Superclass:</b>"+pakkage+"."+clazz,rn_root[0].getDescription().getHtml());
	}
	private static void checkNoSuperClass(GenericReport report) throws XPathExpressionException
	{
		ReportNode[] rn_root = report.find("./node/node[@kind='"+AnalysisReport.SUPER_CLASS+"']");
		Assert.assertEquals(rn_root.length,0);
	}
	private static void checkParentClassName(GenericReport report, String pakkage, String clazz) throws XPathExpressionException
	{
		ReportNode[] rn_root = report.find("./node/node[@kind='"+AnalysisReport.PARENT_CLASS+"']");
		Assert.assertEquals(rn_root.length,1);
		Assert.assertEquals("<b>Parent:</b>"+pakkage+"."+clazz,rn_root[0].getDescription().getHtml());
	}
	private static void checkNoParentClass(GenericReport report) throws XPathExpressionException
	{
		ReportNode[] rn_root = report.find("./node/node[@kind='"+AnalysisReport.PARENT_CLASS+"']");
		Assert.assertEquals(rn_root.length,0);
	}
	private void checkInterfaces(GenericReport report, String[][] strings) throws XPathExpressionException {
		ReportNode[] rn_root = report.find("./node/node[@kind='"+AnalysisReport.INTERFACE+"']");
		HashSet<String> hs_found = getDescriptionsAsASet(rn_root);
		HashSet<String> hs_expected = new HashSet<String>();
		for(int i=0;i<strings.length;i++)
			hs_expected.add("<b>Implements:</b>"+strings[i][0]+"."+strings[i][1]);
		assertEquals(hs_expected,hs_found);
	}
	private void checkModifiers(GenericReport report, String... strings) throws XPathExpressionException {
		ReportNode[] rn_root = report.find("./node/node[@kind='"+AnalysisReport.MODIFIER+"']");
		HashSet<String> hs_found = getDescriptionsAsASet(rn_root);
		HashSet<String> hs_expected = new HashSet<String>();
		for(int i=0;i<strings.length;i++)
			hs_expected.add("<b>Modifier:</b>"+strings[i]);
		assertEquals(hs_expected,hs_found);
	}
	private void checkAnnotations(GenericReport report, String[][] strings) throws XPathExpressionException {
		ReportNode[] rn_root = report.find("./node/node[@kind='"+AnalysisReport.ANNOTATION+"']");
		HashSet<String> hs_found = getDescriptionsAsASet(rn_root);
		HashSet<String> hs_expected = new HashSet<String>();
		for(int i=0;i<strings.length;i++)
			hs_expected.add("<b>Annotated:</b>Annotation "+strings[i][0]+"."+strings[i][1]);
		assertEquals(hs_expected,hs_found);
	}
	private void checkInnerClasses(GenericReport report, String[] strings) throws XPathExpressionException {
		ReportNode[] rn_root = report.find("./node/node[@kind='"+AnalysisReport.INNER_CLASS+"']");
		HashSet<String> hs_found = getDescriptionsAsASet(rn_root);
		HashSet<String> hs_expected = new HashSet<String>();
		for(int i=0;i<strings.length;i++)
			hs_expected.add("<b>Contains:</b>"+strings[i]);
		assertEquals(hs_expected,hs_found);
	}
	private void checkReferencedClasses(GenericReport report, String[][] strings) throws XPathExpressionException {
		ReportNode[] rn_root = report.find("./node/node[@kind='"+AnalysisReport.REFERENCED_CLASS+"']");
		HashSet<String> hs_found = getDescriptionsAsASet(rn_root);
		HashSet<String> hs_expected = new HashSet<String>();
		for(int i=0;i<strings.length;i++)
			hs_expected.add("<b>References:</b>"+strings[i][0]+"."+strings[i][1]);
		assertEquals(hs_expected,hs_found);
	}
	
	static class FieldSpec 
	{
		public String fn;
		public String[] type;
		public String[][] annotations=new String[][]{};
		public String[] modifiers=new String[]{};
	}
	
	private void checkFields(GenericReport report, FieldSpec... fields) throws XPathExpressionException
	{
		ReportNode[] rn_root = report.find("./node/node[@kind='"+AnalysisReport.DECLARED_FIELD+"']");
		if(rn_root.length!=fields.length)
		{
//			for(ReportNode rn:rn_root)
//				System.out.println("PF208 "+rn.getDescription().getHtml());
			Assert.fail("Expected "+fields.length+" fields, found "+rn_root.length);
		}
		Map<String,FieldSpec> hm = new HashMap<String,FieldSpec>();
		for(FieldSpec fi:fields)
			hm.put(fi.fn,fi);
		for(ReportNode rn : rn_root)
		{
			FieldSpec fs = hm.get(rn.getDescription().getHtml());
			if(fs==null)
				Assert.fail("Unexpected field "+rn.getDescription().getHtml()+" found");
			Assert.assertEquals(fs.fn,rn.getDescription().getHtml());
			
			ReportNode[] rn_type = rn.find("./node[@kind='"+AnalysisReport.FIELD_TYPE+"']");
			if(rn_type.length==0)
				Assert.fail("Could not find field type");
			if(rn_type.length>1)
				Assert.fail("Found more than one field type");
			Assert.assertEquals((fs.type[0].trim().length()>0 ? fs.type[0]+"." : "")+fs.type[1], rn_type[0].getDescription().getHtml());

			{
				ReportNode[] rn_annotations = rn.find("./node[@kind='"+AnalysisReport.ANNOTATION+"']");
				HashSet<String> hs_found = getDescriptionsAsASet(rn_annotations);
				HashSet<String> hs_expected = new HashSet<String>();
				for(int i=0;i<fs.annotations.length;i++)
					hs_expected.add("<b>Annotated:</b>Annotation "+fs.annotations[i][0]+"."+fs.annotations[i][1]);
				assertEquals(hs_expected,hs_found);
			}

			{
				ReportNode[] rn_modifiers = rn.find("./node[@kind='"+AnalysisReport.MODIFIER+"']");
				HashSet<String> hs_found = getDescriptionsAsASet(rn_modifiers);
				HashSet<String> hs_expected = new HashSet<String>();
				for(int i=0;i<fs.modifiers.length;i++)
					hs_expected.add("<b>Modifier:</b>"+fs.modifiers[i]);
				assertEquals(hs_expected,hs_found);
			}
		}
	}

	static class MethodSpec 
	{
		public String mn;
		public String[][] parameterTypes=new String[][]{};
		public String[][] annotations=new String[][]{};
		public String[] modifiers=new String[]{};
		public String[] returnType;
		public String[][] expectionThrown=new String[][]{};
		public String[][] readFields=new String[][]{};
		public String[][] writeFields=new String[][]{};
		public String[][] instantiatesClass=new String[][]{};
		public String[][] invokesMethod=new String[][]{};
		public String nom(){
			StringBuffer sb=new StringBuffer();
			sb.append(mn);
			sb.append("(");
			boolean first=true;
			for(String[] pt : parameterTypes)
			{
				if(!first)
					sb.append(",");
				sb.append((pt[0].trim().length()==0 ? "" : pt[0]+".")+pt[1]);
				first=false;
			}
			sb.append(")");
			return sb.toString();
		}
	}
	
	private void checkMethods(GenericReport report, MethodSpec... methods) throws XPathExpressionException
	{
		ReportNode[] rn_root = report.find("./node/node[@kind='"+AnalysisReport.DECLARED_METHOD+"']");
		if(rn_root.length!=methods.length)
		{
			Assert.fail("Expected "+methods.length+" methods, found "+rn_root.length);
		}
		Map<String,MethodSpec> hm = new HashMap<String,MethodSpec>();
		for(MethodSpec mi:methods)
		{
//			System.out.println("PF204 "+mi.nom());
			hm.put(mi.nom(),mi);
		}
		for(ReportNode rn : rn_root)
		{
//			System.out.println("PF205 "+rn.getDescription().getHtml());
			MethodSpec ms = hm.get(rn.getDescription().getHtml());
			if(ms==null)
				Assert.fail("Unexpected method "+rn.getDescription().getHtml()+" found, "+hm);
			Assert.assertEquals(ms.nom(),rn.getDescription().getHtml());
			
			{
				ReportNode[] rn_annotations = rn.find("./node[@kind='"+AnalysisReport.ANNOTATION+"']");
				HashSet<String> hs_found = getDescriptionsAsASet(rn_annotations);
				HashSet<String> hs_expected = new HashSet<String>();
				for(int i=0;i<ms.annotations.length;i++)
					hs_expected.add("<b>Annotated:</b>Annotation "+ms.annotations[i][0]+"."+ms.annotations[i][1]);
				assertEquals(hs_expected,hs_found);
			}

			{
				ReportNode[] rn_modifiers = rn.find("./node[@kind='"+AnalysisReport.MODIFIER+"']");
				HashSet<String> hs_found = getDescriptionsAsASet(rn_modifiers);
				HashSet<String> hs_expected = new HashSet<String>();
				for(int i=0;i<ms.modifiers.length;i++)
					hs_expected.add("<b>Modifier:</b>"+ms.modifiers[i]);
				assertEquals(hs_expected,hs_found);
			}

			{
				ReportNode[] rn_rt = rn.find("./node[@kind='"+AnalysisReport.RETURN_TYPE+"']");
				if(rn_rt.length==0)
					Assert.fail("No return type found");
				if(rn_rt.length>1)
					Assert.fail("Multiple return type found");
				Assert.assertEquals((ms.returnType[0].trim().length()>0 ? ms.returnType[0]+"." : "")+ms.returnType[1], rn_rt[0].getDescription().getHtml());
			}
			
			{
				ReportNode[] rn_rt = rn.find("./node[@kind='"+AnalysisReport.PARAMETER_TYPE+"']");
				if(rn_rt.length!=ms.parameterTypes.length)
					Assert.fail("Expected "+ms.parameterTypes.length+", found "+rn_rt.length);
				for(int i=0;i<rn_rt.length;i++)
				{
					Assert.assertEquals((ms.parameterTypes[i][0].trim().length()>0 ? ms.parameterTypes[i][0]+"." : "")+ms.parameterTypes[i][1], rn_rt[0].getDescription().getHtml());
				}
			}

			{
				ReportNode[] rn_exceptions = rn.find("./node[@kind='"+AnalysisReport.EXCEPTION_THROWN+"']");
				HashSet<String> hs_found = getDescriptionsAsASet(rn_exceptions);
				HashSet<String> hs_expected = new HashSet<String>();
				for(int i=0;i<ms.expectionThrown.length;i++)
					hs_expected.add("<b>Throws:</b>"+ms.expectionThrown[i][0]+"."+ms.expectionThrown[i][1]);
				assertEquals(hs_expected,hs_found);
			}
			
			{
				ReportNode[] rn_reads = rn.find("./node[@kind='"+AnalysisReport.READS_FIELD+"']");
				HashSet<String> hs_found = getDescriptionsAsASet(rn_reads);
				HashSet<String> hs_expected = new HashSet<String>();
				for(int i=0;i<ms.readFields.length;i++)
					hs_expected.add("<b>Reads:</b>"+ms.readFields[i][0]+"."+ms.readFields[i][1]+"."+ms.readFields[i][2]);
				assertEquals(hs_expected,hs_found,ms.mn);
			}
			{
				ReportNode[] rn_writes = rn.find("./node[@kind='"+AnalysisReport.WRITES_FIELD+"']");
				HashSet<String> hs_found = getDescriptionsAsASet(rn_writes);
				HashSet<String> hs_expected = new HashSet<String>();
				for(int i=0;i<ms.writeFields.length;i++)
					hs_expected.add("<b>Writes:</b>"+ms.writeFields[i][0]+"."+ms.writeFields[i][1]+"."+ms.writeFields[i][2]);
				assertEquals(hs_expected,hs_found);
			}
			{
				ReportNode[] rn_instantiates = rn.find("./node[@kind='"+AnalysisReport.INSTANTIATES_CLASS+"']");
				HashSet<String> hs_found = getDescriptionsAsASet(rn_instantiates);
				HashSet<String> hs_expected = new HashSet<String>();
				for(int i=0;i<ms.instantiatesClass.length;i++)
					hs_expected.add("<b>Instantiates:</b>"+ms.instantiatesClass[i][0]+"."+ms.instantiatesClass[i][1]);
				assertEquals(hs_expected,hs_found);
			}
			{
				ReportNode[] rn_invokes = rn.find("./node[@kind='"+AnalysisReport.INVOKES_METHOD+"']");
				HashSet<String> hs_found = getDescriptionsAsASet(rn_invokes);
				HashSet<String> hs_expected = new HashSet<String>();
				for(int i=0;i<ms.invokesMethod.length;i++)
					hs_expected.add("<b>Invokes:</b>"+ms.invokesMethod[i][0]+"."+ms.invokesMethod[i][1]+"."+ms.invokesMethod[i][2]);
//				System.out.println("PF209:"+hs_expected);
//				System.out.println("PF210:"+hs_found);
				assertEquals(hs_expected,hs_found,ms.mn);
			}
			
		}
	}

	private void checkConstructors(GenericReport report, MethodSpec... methods) throws XPathExpressionException
	{
		ReportNode[] rn_root = report.find("./node/node[@kind='"+AnalysisReport.DECLARED_CONSTRUCTOR+"']");
		if(rn_root.length!=methods.length)
		{
			Assert.fail("Expected "+methods.length+" methods, found "+rn_root.length);
		}
		Map<String,MethodSpec> hm = new HashMap<String,MethodSpec>();
		for(MethodSpec mi:methods)
			hm.put(mi.nom(),mi);
		for(ReportNode rn : rn_root)
		{
			MethodSpec ms = hm.get(rn.getDescription().getHtml());
			if(ms==null)
				Assert.fail("Unexpected method "+rn.getDescription().getHtml()+" found");
			Assert.assertEquals(ms.nom(),rn.getDescription().getHtml());
			
			{
				ReportNode[] rn_annotations = rn.find("./node[@kind='"+AnalysisReport.ANNOTATION+"']");
				HashSet<String> hs_found = getDescriptionsAsASet(rn_annotations);
				HashSet<String> hs_expected = new HashSet<String>();
				for(int i=0;i<ms.annotations.length;i++)
					hs_expected.add("<b>Annotated:</b>Annotation "+ms.annotations[i][0]+"."+ms.annotations[i][1]);
				assertEquals(hs_expected,hs_found);
			}

			{
				ReportNode[] rn_modifiers = rn.find("./node[@kind='"+AnalysisReport.MODIFIER+"']");
				HashSet<String> hs_found = getDescriptionsAsASet(rn_modifiers);
				HashSet<String> hs_expected = new HashSet<String>();
				for(int i=0;i<ms.modifiers.length;i++)
					hs_expected.add("<b>Modifier:</b>"+ms.modifiers[i]);
				assertEquals(hs_expected,hs_found);
			}

			{
				ReportNode[] rn_rt = rn.find("./node[@kind='"+AnalysisReport.RETURN_TYPE+"']");
				if(rn_rt.length==0)
					Assert.fail("No return type found");
				if(rn_rt.length>1)
					Assert.fail("Multiple return type found");
				Assert.assertEquals((ms.returnType[0].trim().length()>0 ? ms.returnType[0]+"." : "")+ms.returnType[1], rn_rt[0].getDescription().getHtml());
			}

			{
				ReportNode[] rn_rt = rn.find("./node[@kind='"+AnalysisReport.PARAMETER_TYPE+"']");
				if(rn_rt.length!=ms.parameterTypes.length)
					Assert.fail("Expected "+ms.parameterTypes.length+", found "+rn_rt.length);
				for(int i=0;i<rn_rt.length;i++)
				{
					Assert.assertEquals("Parameter#"+i,(ms.parameterTypes[i][0].trim().length()>0 ? ms.parameterTypes[i][0]+"." : "")+ms.parameterTypes[i][1], rn_rt[i].getDescription().getHtml());
				}
			}

			{
				ReportNode[] rn_exceptions = rn.find("./node[@kind='"+AnalysisReport.EXCEPTION_THROWN+"']");
				HashSet<String> hs_found = getDescriptionsAsASet(rn_exceptions);
				HashSet<String> hs_expected = new HashSet<String>();
				for(int i=0;i<ms.expectionThrown.length;i++)
					hs_expected.add("<b>Throws:</b>"+ms.expectionThrown[i][0]+"."+ms.expectionThrown[i][1]);
				assertEquals(hs_expected,hs_found);
			}

			{
				ReportNode[] rn_reads = rn.find("./node[@kind='"+AnalysisReport.READS_FIELD+"']");
				HashSet<String> hs_found = getDescriptionsAsASet(rn_reads);
				HashSet<String> hs_expected = new HashSet<String>();
				for(int i=0;i<ms.readFields.length;i++)
					hs_expected.add("<b>Reads:</b>"+ms.readFields[i][0]+"."+ms.readFields[i][1]+"."+ms.readFields[i][2]);
				assertEquals(hs_expected,hs_found);
			}
			{
				ReportNode[] rn_writes = rn.find("./node[@kind='"+AnalysisReport.WRITES_FIELD+"']");
				HashSet<String> hs_found = getDescriptionsAsASet(rn_writes);
				HashSet<String> hs_expected = new HashSet<String>();
				for(int i=0;i<ms.writeFields.length;i++)
					hs_expected.add("<b>Writes:</b>"+ms.writeFields[i][0]+"."+ms.writeFields[i][1]+"."+ms.writeFields[i][2]);
				assertEquals(hs_expected,hs_found,ms.mn);
			}
			{
				ReportNode[] rn_instantiates = rn.find("./node[@kind='"+AnalysisReport.INSTANTIATES_CLASS+"']");
				HashSet<String> hs_found = getDescriptionsAsASet(rn_instantiates);
				HashSet<String> hs_expected = new HashSet<String>();
				for(int i=0;i<ms.instantiatesClass.length;i++)
					hs_expected.add("<b>Instantiates:</b>"+ms.instantiatesClass[i][0]+"."+ms.instantiatesClass[i][1]);
				assertEquals(hs_expected,hs_found);
			}
			{
				ReportNode[] rn_invokes = rn.find("./node[@kind='"+AnalysisReport.INVOKES_METHOD+"']");
				HashSet<String> hs_found = getDescriptionsAsASet(rn_invokes);
				HashSet<String> hs_expected = new HashSet<String>();
				for(int i=0;i<ms.invokesMethod.length;i++)
					hs_expected.add("<b>Invokes:</b>"+ms.invokesMethod[i][0]+"."+ms.invokesMethod[i][1]+"."+ms.invokesMethod[i][2]);
				assertEquals(hs_expected,hs_found);
			}
		}
	}
	
	private HashSet<String> getDescriptionsAsASet(ReportNode[] rnRoot) {
		HashSet<String> rc=new HashSet<String>();
		for(ReportNode rn:rnRoot)
		{
			rc.add(rn.getDescription().getHtml());
		};
		return rc;
	}

	private void assertEquals(HashSet<String> hsExpected,
			HashSet<String> hsFound, String... mn) {
//		if(hsExpected.size()!=hsFound.size())
//			Assert.fail("Expected "+hsExpected+", found "+hsFound+", not the same length");
		HashSet<String> hs=new HashSet<String>();
		hs.addAll(hsExpected);
		hs.removeAll(hsFound);
		if(hs.size()>0)
			Assert.fail((mn.length>0 ? mn[0]+":" : "") +"Expected "+hsExpected+", found "+hsFound+", Missing "+hs);
		hs=new HashSet<String>();
		hs.addAll(hsFound);
		hs.removeAll(hsExpected);
		if(hs.size()>0)
			Assert.fail((mn.length>0 ? mn[0]+":" : "") +"Expected "+hsExpected+", found "+hsFound+", Unexpected "+hs);
	}

	@Test
	public void testClasseA() throws FileNotFoundException, SAXException, IOException, XPathExpressionException
	{
		GenericReport class_report = new GenericReport(new FileInputStream(tempdir+"/test-analysis-0.1-SNAPSHOT.jar/org/jarco/test/analysis/packageA/ClasseA.analysis.class.ja2.xml"));
		checkClassName(class_report,"org.jarco.test.analysis.packageA","ClasseA");
		checkSuperClassName(class_report,"java.lang","Object");
		checkInterfaces(class_report,new String[][]{{"org.jarco.test.analysis.packageA","InterfaceD"}});
		checkNoParentClass(class_report);
		checkModifiers(class_report, "_public");
		checkAnnotations(class_report,new String[][]{{"org.jarco.test.analysis.packageA","Agent"}});
		checkInnerClasses(class_report,new String[]{"ClasseA$Log1"});
		checkReferencedClasses(class_report,new String[][]{
				{ "java.lang", "String"},
//				{ "org.jarco.test.analysis.packageA", "Property"},
//				{ "org.jarco.test.analysis.packageA", "Agent"},
				{ "java.lang", "RuntimeException" },
				{ "java.lang", "Object" },
				{ "java.io", "PrintStream" },
				{"org.jarco.test.analysis.packageA","InterfaceD"},
				{ "java.util", "Date"},
				{ "java.lang", "System"},
				{ "java.lang", "StringBuilder"},
				{"org.jarco.test.analysis.packageA","ClasseA$Log1"},
		});
		
		FieldSpec fs_constant = new FieldSpec();
		fs_constant.fn="CONSTANT_FIELD";
		fs_constant.modifiers=new String[]{ "_public", "_static", "_final" };
		fs_constant.type =new String[]{ "java.lang", "String" };

		FieldSpec fs_id = new FieldSpec();
		fs_id.fn="id";
		fs_id.modifiers=new String[]{ "_private" };
		fs_id.type =new String[]{ "", "Long:primitive" };
		fs_id.annotations=new String[][]{{"org.jarco.test.analysis.packageA","Property"}};
		
		checkFields(class_report,fs_constant,fs_id);
		
		MethodSpec ms_ctor = new MethodSpec();
		ms_ctor.mn="<init>";
		ms_ctor.modifiers=new String[]{"_public"};
		ms_ctor.returnType=new String[]{"java.lang","Void"};
		MethodSpec ms_target = new MethodSpec();
		ms_target.mn="setId";
		ms_target.parameterTypes=new String[][]{{"","Long:primitive" }};
		ms_ctor.invokesMethod=new String[][]{
				{"java.lang","Object","<init>()"},
				{"org.jarco.test.analysis.packageA","ClasseA",ms_target.nom()}
		};

		/**
	@Property(name="Id")
	protected void setId(long id) throws RuntimeException
	{
		System.out.println(new Log1(new Date(),"id set to "+id));
	}
*/		
		
		MethodSpec ms_setid = new MethodSpec();
		ms_setid.mn="setId";
		ms_setid.modifiers=new String[]{"_protected"};
		ms_setid.annotations=new String[][]{ {"org.jarco.test.analysis.packageA","Property"}};
		ms_setid.parameterTypes=new String[][]{{"","Long:primitive"}};
		ms_setid.returnType=new String[]{"","Void:primitive"};
		ms_setid.expectionThrown=new String[][]{{"java.lang","RuntimeException"}};
		ms_setid.readFields=new String[][]{{"java.lang","System","out"}};
		ms_setid.writeFields=new String[][]{{"org.jarco.test.analysis.packageA","ClasseA","id"}};
		ms_setid.invokesMethod=new String[][]{
				{"java.io","PrintStream","println(java.lang.Object)"},
				{"org.jarco.test.analysis.packageA","ClasseA$Log1","<init>(java.util.Date,java.lang.String)"},
				{"java.util","Date","<init>()"},
//				{"java.lang","StringBuilder","<init>()"},
//TODO 1.0 verifier que l'on ne s'attend pas à instantier un StringBuilder dans ce cas
				{"java.lang","StringBuilder","append(java.lang.String)"},
				{"java.lang","StringBuilder","append(Long:primitive)"},
				{"java.lang","StringBuilder","toString()"}
		};
		ms_setid.instantiatesClass=new String[][]{
				{"java.util","Date"},
				{"org.jarco.test.analysis.packageA","ClasseA$Log1"},
				{"java.lang","StringBuilder"}
		};

		MethodSpec ms_getid = new MethodSpec();
		ms_getid.mn="getId";
		ms_getid.modifiers=new String[]{"_public"};
		ms_getid.annotations=new String[][]{ {"org.jarco.test.analysis.packageA","Property"}};
		ms_getid.returnType=new String[]{"","Long:primitive"};
		ms_getid.readFields=new String[][]{{"org.jarco.test.analysis.packageA","ClasseA","id"}};
		
		checkMethods(class_report,ms_setid,ms_getid);
		checkConstructors(class_report,ms_ctor);
		
		GenericReport inner_class_report = new GenericReport(new FileInputStream(tempdir+"/test-analysis-0.1-SNAPSHOT.jar/org/jarco/test/analysis/packageA/ClasseA$Log1.analysis.class.ja2.xml"));
		checkClassName(inner_class_report,"org.jarco.test.analysis.packageA","ClasseA$Log1");
		checkSuperClassName(inner_class_report,"java.lang","Object");
		checkParentClassName(inner_class_report,"org.jarco.test.analysis.packageA","ClasseA");
		checkInterfaces(inner_class_report,new String[][]{});
		//TODO 1.1 récupérer les modifiers des inner-class AUSSI dans la classe parente
		//TODO 1.1 et réactiver le test
		//		checkModifiers(inner_class_report,"_private","_static");
		checkAnnotations(inner_class_report,new String[][]{});
		checkInnerClasses(inner_class_report,new String[]{});
		checkReferencedClasses(inner_class_report,new String[][]{
				{ "java.lang", "String"},
				{ "java.util", "Date"},
				{ "java.text", "SimpleDateFormat"},
				{ "org.jarco.test.analysis.packageA", "ClasseA"	},
				{ "java.lang", "Object"},
				{ "java.lang", "StringBuilder" }
		});

		FieldSpec fs_date = new FieldSpec();
		fs_date.fn="date";
		fs_date.modifiers=new String[]{ "_private" };
		fs_date.type =new String[]{ "java.util", "Date" };

		FieldSpec fs_message = new FieldSpec();
		fs_message.fn="message";
		fs_message.modifiers=new String[]{ "_private" };
		fs_message.type =new String[]{ "java.lang", "String" };
/*
		FieldSpec fs_synthetic = new FieldSpec();
		fs_synthetic.fn="this$0";
		fs_synthetic.modifiers=new String[]{"_final"};
		fs_synthetic.type=new String[]{"org.jarco.test.analysis.packageA","ClasseA"};
	*/	
		checkFields(inner_class_report,fs_date,fs_message/*,fs_synthetic*/);

		MethodSpec ms_tostring = new MethodSpec();
		ms_tostring.mn="toString";
		ms_tostring.modifiers=new String[]{"_public"};
		ms_tostring.returnType=new String[]{"java.lang","String"};
		ms_tostring.readFields=new String[][]{
				{"org.jarco.test.analysis.packageA","ClasseA$Log1","date"},
				{"org.jarco.test.analysis.packageA","ClasseA$Log1","message"}
		};
		ms_tostring.instantiatesClass=new String[][]{
				{"java.lang","StringBuilder"}
		};
		ms_tostring.invokesMethod=new String[][]{
				{"java.lang","StringBuilder","<init>()"},
				{"java.lang","StringBuilder","toString()"},
				{"java.lang","StringBuilder","append(java.lang.String)"},
				{"java.lang","StringBuilder","append(java.lang.Object)"}
		};

		MethodSpec ms_toarray = new MethodSpec();
		ms_toarray.mn="toArray";
		ms_toarray.modifiers=new String[]{"_public"};
		ms_toarray.returnType=new String[]{"java.lang","String:array[1]"};
		ms_toarray.readFields=new String[][]{
				{"org.jarco.test.analysis.packageA","ClasseA$Log1","date"},
				{"org.jarco.test.analysis.packageA","ClasseA$Log1","message"}
		};
		ms_toarray.instantiatesClass=new String[][]{
				{"java.text","SimpleDateFormat"}
		};
		ms_toarray.invokesMethod=new String[][]{
				{"java.text","DateFormat","format(java.util.Date)"},
				{"java.text","SimpleDateFormat","<init>()"}
		};
		
		checkMethods(inner_class_report,ms_tostring,ms_toarray);

		MethodSpec ms_ctor1 = new MethodSpec();
		ms_ctor1.mn="<init>";
		ms_ctor1.modifiers=new String[]{"_public"};
		ms_ctor1.parameterTypes=new String[][]{{"java.util","Date"},{"java.lang","String"}};
		ms_ctor1.returnType=new String[]{"java.lang","Void"};
		ms_ctor1.writeFields=new String[][]{
				{"org.jarco.test.analysis.packageA","ClasseA$Log1","date"},
				{"org.jarco.test.analysis.packageA","ClasseA$Log1","message"}
		};
		ms_ctor1.invokesMethod=new String[][]{
				{"java.lang","Object","<init>()" }	
		};

		checkConstructors(inner_class_report,ms_ctor1);
	}

	@Test
	public void testClasseB() throws FileNotFoundException, SAXException, IOException, XPathExpressionException
	{
		GenericReport class_report = new GenericReport(new FileInputStream(tempdir+"/test-analysis-0.1-SNAPSHOT.jar/org/jarco/test/analysis/packageA/ClasseB.analysis.class.ja2.xml"));
		checkClassName(class_report,"org.jarco.test.analysis.packageA","ClasseB");
		checkSuperClassName(class_report,"org.jarco.test.analysis.packageA","ClasseA");
		checkNoParentClass(class_report);
		checkInterfaces(class_report,new String[][]{});
		checkModifiers(class_report,"_public");
		checkAnnotations(class_report,new String[][]{});
		checkInnerClasses(class_report,new String[]{});
		checkReferencedClasses(class_report,new String[][]{
				{ "java.lang", "System"},
				{ "org.jarco.test.analysis.packageA", "ClasseA"}
		});
		checkFields(class_report);
		
		MethodSpec ms_ctor=new MethodSpec();
		ms_ctor.mn="<init>";
		ms_ctor.modifiers=new String[]{"_public"};
		ms_ctor.returnType=new String[]{"java.lang","Void"};
		
		ms_ctor.invokesMethod=new String[][]{
				{"org.jarco.test.analysis.packageA","ClasseA","setId(Long:primitive)"},
				{"java.lang","System","currentTimeMillis()"},
				{"org.jarco.test.analysis.packageA","ClasseA","<init>()"}
		};
		
		checkMethods(class_report);
		checkConstructors(class_report,ms_ctor);
	}
	@Test
	public void testClasseC() throws FileNotFoundException, SAXException, IOException, XPathExpressionException
	{
		GenericReport class_report = new GenericReport(new FileInputStream(tempdir+"/test-analysis-0.1-SNAPSHOT.jar/org/jarco/test/analysis/packageB/ClasseC.analysis.class.ja2.xml"));
		checkClassName(class_report,"org.jarco.test.analysis.packageB","ClasseC");
		checkSuperClassName(class_report,"java.lang","Object");
		checkNoParentClass(class_report);
		checkInterfaces(class_report,new String[][]{});
		checkModifiers(class_report,"_public");
		checkAnnotations(class_report,new String[][]{});
		checkInnerClasses(class_report,new String[]{});
		checkReferencedClasses(class_report,new String[][]{
				{ "java.lang", "String"},
				{ "java.lang", "Object"},
				{ "org.jarco.test.analysis.packageA", "ClasseA"}
		});
		checkFields(class_report);

		/*
	public String wrap(T t){
		return t.toString();
	}
	public T unwrap(String s)
	{
		return (T) new ClasseA();
	}
		 */
		
		
		MethodSpec ms_wrap=new MethodSpec();
		ms_wrap.mn="wrap";
		ms_wrap.modifiers=new String[]{"_public"};
		ms_wrap.parameterTypes=new String[][]{{"","*Any*"}};
		ms_wrap.returnType=new String[]{"java.lang","String"};
		ms_wrap.invokesMethod=new String[][]{
				{"java.lang","Object","toString()"} //TODO 1.1 approfondir la généricité
		};

		MethodSpec ms_unwrap=new MethodSpec();
		ms_unwrap.mn="unwrap";
		ms_unwrap.modifiers=new String[]{"_public"};
		ms_unwrap.parameterTypes=new String[][]{{"java.lang","String"}};
		ms_unwrap.returnType=new String[]{"","*Any*"};
		ms_unwrap.instantiatesClass=new String[][]{
				{"org.jarco.test.analysis.packageA", "ClasseA"}
		};
		ms_unwrap.invokesMethod=new String[][]{
				{"org.jarco.test.analysis.packageA", "ClasseA","<init>()"}
		};
		
		MethodSpec ms_ctor=new MethodSpec();
		ms_ctor.mn="<init>";
		ms_ctor.modifiers=new String[]{"_public"};
		ms_ctor.returnType=new String[]{"java.lang","Void"};
		ms_ctor.invokesMethod=new String[][]{
				{"java.lang","Object","<init>()"}
		};
		
		checkConstructors(class_report,ms_ctor);
		checkMethods(class_report,ms_wrap,ms_unwrap);
}
	@Test
	public void testInterfaceD() throws FileNotFoundException, SAXException, IOException, XPathExpressionException
	{
		GenericReport class_report = new GenericReport(new FileInputStream(tempdir+"/test-analysis-0.1-SNAPSHOT.jar/org/jarco/test/analysis/packageA/InterfaceD.analysis.class.ja2.xml"));
		checkClassName(class_report,"org.jarco.test.analysis.packageA","InterfaceD");
		checkNoSuperClass(class_report);
		checkNoParentClass(class_report);
		checkInterfaces(class_report,new String[][]{});
		checkModifiers(class_report,"_interface","_abstract","_public");
		checkAnnotations(class_report,new String[][]{});
		checkInnerClasses(class_report,new String[]{});
		checkReferencedClasses(class_report,new String[][]{
				{ "java.lang", "Object" }
		});
		checkFields(class_report);
		checkConstructors(class_report);
		checkMethods(class_report);
	}

	@Test
	public void testProperty() throws FileNotFoundException, SAXException, IOException, XPathExpressionException
	{
		GenericReport class_report = new GenericReport(new FileInputStream(tempdir+"/test-analysis-0.1-SNAPSHOT.jar/org/jarco/test/analysis/packageA/Property.analysis.class.ja2.xml"));
		checkClassName(class_report,"org.jarco.test.analysis.packageA","Property");
		checkNoSuperClass(class_report);
		checkNoParentClass(class_report);
		checkInterfaces(class_report,new String[][]{{"java.lang.annotation","Annotation"}});
		checkModifiers(class_report,"_annotation","_public","_abstract","_interface");
		checkAnnotations(class_report,new String[][]{{"java.lang.annotation","Retention"}});
		checkInnerClasses(class_report,new String[]{});
		checkReferencedClasses(class_report,new String[][]{
				{ "java.lang", "Object"},
				{ "java.lang.annotation", "Annotation"}
		});
		checkFields(class_report);
		checkConstructors(class_report);
		
		MethodSpec ms_name = new MethodSpec();
		ms_name.mn="name";
		ms_name.modifiers=new String[]{"_abstract","_public"};
		ms_name.returnType=new String[]{ "java.lang","String"};
		checkMethods(class_report,ms_name);
	}

	@Test
	public void testAgent() throws FileNotFoundException, SAXException, IOException, XPathExpressionException
	{
		GenericReport class_report = new GenericReport(new FileInputStream(tempdir+"/test-analysis-0.1-SNAPSHOT.jar/org/jarco/test/analysis/packageA/Agent.analysis.class.ja2.xml"));
		checkClassName(class_report,"org.jarco.test.analysis.packageA","Agent");
		checkNoSuperClass(class_report);
		checkNoParentClass(class_report);
		checkInterfaces(class_report,new String[][]{{"java.lang.annotation","Annotation"}});
		checkModifiers(class_report,"_annotation","_public","_abstract","_interface");
		checkAnnotations(class_report,new String[][]{{"java.lang.annotation","Retention"}});
		checkInnerClasses(class_report,new String[]{});
		checkReferencedClasses(class_report,new String[][]{
				{ "java.lang", "Object"},
				{ "java.lang.annotation", "Annotation"}
		});
		checkFields(class_report);
		checkConstructors(class_report);
		
		MethodSpec ms_name = new MethodSpec();
		ms_name.mn="name";
		ms_name.modifiers=new String[]{"_abstract","_public"};
		ms_name.returnType=new String[]{ "java.lang","String"};
		checkMethods(class_report,ms_name);
	}
	
}
