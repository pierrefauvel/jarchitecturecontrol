package org.jarco.code.internal;

import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jarco.code.external.ICodeRepository;
import org.jarco.code.external.IPackage;
import org.jarco.code.external.IProject;
import org.jarco.code.external.IXmlDocument;
import org.jarco.code.external.IXmlElement;
import org.jarco.collections.ImmutableList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class XmlDocumentInternal extends ACodeElementInternal implements IXmlDocument {

	private Document peer;
	private IProject project;
	private String archiveFileName;
	private String name;

	static DocumentBuilderFactory dbf;
	static DocumentBuilder db;
	static
	{
		try
		{
			dbf=DocumentBuilderFactory.newInstance();
			db=dbf.newDocumentBuilder();
		}
		catch(Throwable t)
		{
			t.printStackTrace(System.err);
		}
	}
	
	public XmlDocumentInternal(ICodeRepository repo,ProjectInternal projectImpl, String archiveFileName,
			String jen) {
		super(repo);
		project=projectImpl;
		this.archiveFileName=archiveFileName;
		this.name=jen;
	}

	public String getName()
	{
		return name;
	}
	
	public IProject getProject() {
		return project;
	}

	public IXmlElement getDocumentElement() {
		try {
			analyzeDom();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		}
		return new XmlElementInternal(getRepository(),peer.getDocumentElement());
	}

	private void analyzeDom() throws IOException, SAXException {
		if(peer==null)
		{
			JarFile jf = new JarFile(archiveFileName);
			JarEntry je = jf.getJarEntry(name);
			peer = db.parse(jf.getInputStream(je));
		}
	}
	
	public ImmutableList<IXmlElement> getChildNodesForXPath(String xpath)
	{
		return getDocumentElement().getChildNodesForXPath(xpath);
	}
	
	public String toString()
	{
		return "XmlDocument "+name;
	}
}
