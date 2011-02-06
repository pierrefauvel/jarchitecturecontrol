package org.jarco.control.report.genericreport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GenericReport {

	static DocumentBuilderFactory dbf;
	static DocumentBuilder db;
	static TransformerFactory tf ;
	static Transformer t ;
	
	static
	{
		try
		{
		dbf = DocumentBuilderFactory.newInstance();
		db=dbf.newDocumentBuilder();
		tf = TransformerFactory.newInstance();
		t = tf.newTransformer();
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
	}
	
	
	private Document _d;
	private Element _current;
	
	public GenericReport(InputStream is) throws SAXException, IOException
	{
		_init(db.parse(is));
	}
	
	private void _init(Document d)
	{
		_d=d;
		_current = _d.getDocumentElement();
	}
	
	public GenericReport(String kind,FormattedText rootDescription, FormattedText detail)
	{
		Document d = db.newDocument();
		Element root = ReportNode.build(d,kind,rootDescription, detail);
		d.appendChild(root);
		_init(d);
	}
	
	public ReportNode getRoot()
	{
		return new ReportNode(_d.getDocumentElement());
	}
	
	public void push(String kind,FormattedText rootDescription, FormattedText detail)
	{
		Element n = ReportNode.build(_d,kind,rootDescription, detail);
		_current.appendChild(n);
		_current=n;
	}
	
	public void pop()
	{
		_current=(Element)(_current.getParentNode());
	}
	
	public void write(OutputStream os) throws TransformerException
	{
		StreamResult sr= new StreamResult(os);
		t.transform(new DOMSource(_d),sr);
	}

	public ReportNode[] find(String string) throws XPathExpressionException {
		XPath xp = XPathFactory.newInstance().newXPath();
		XPathExpression xpe = xp.compile(string);
		NodeList nl = (NodeList)(xpe.evaluate(_d,XPathConstants.NODESET));
		ReportNode[] rc = new ReportNode[nl.getLength()];
		for(int i=0;i<nl.getLength();i++)
		{
			rc[i] = new ReportNode((Element)(nl.item(i)));
		};
		return rc;
	}
	
}
