/**
 * 
 */
package org.jarco.control.report.genericreport;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ReportNode 
{
	private static final String ELT_NODE = "node";
	private static final String ELT_DETAIL = "detail";
	private static final String ELT_DESCRIPTION = "description";
	private static final String ATT_KIND = "kind";
	private Element _e;

	ReportNode(Element e)
	{
		_e=e;
	}
	
	public String getKind()
	{
		return _e.getAttribute(ATT_KIND);
	}
	
	public FormattedText getDescription()
	{
		NodeList nl = _e.getElementsByTagName(ELT_DESCRIPTION);
		Element e0 = (Element)(nl.item(0));
		CDATASection cd = (CDATASection)(e0.getChildNodes().item(0));
//		CDATASection cd2 = (CDATASection)(e0.getChildNodes().item(1));
		return new FormattedText(cd.getNodeValue()/*, cd2.getNodeValue()*/);
	}
	public FormattedText getDetail()
	{
		NodeList nl = _e.getElementsByTagName(ELT_DETAIL);
		Element e0 = (Element)(nl.item(0));
		CDATASection cd = (CDATASection)(e0.getChildNodes().item(0));
//		CDATASection cd2 = (CDATASection)(e0.getChildNodes().item(1));
		return new FormattedText(cd.getNodeValue()/*,cd2.getNodeValue()*/);
	}
	public List<ReportNode> getSubNodes()
	{
		List<ReportNode> rc = new ArrayList<ReportNode>();
//		NodeList nl = _e.getElementsByTagName(ELT_NODE);
		NodeList nl = _e.getChildNodes();
		for(int i=0;i<nl.getLength();i++)
		{
			Element ei = (Element)(nl.item(i));
			if(ei.getNodeName().compareTo(ELT_NODE)==0)
				rc.add(new ReportNode(ei));
		}
		return rc;
	}

	static Element build(Document d,String kind,FormattedText description, FormattedText detail)
	{
		Element e = d.createElement(ELT_NODE);
		Element e_ds = d.createElement(ELT_DESCRIPTION);
		Element e_dt = d.createElement(ELT_DETAIL);
		CDATASection cdata_ds = d.createCDATASection(description!=null ? description.getHtml() : "-");
//		CDATASection cdata_ds2 = d.createCDATASection(description!=null ? description.getXml() : "");
		CDATASection cdata_dt = d.createCDATASection(detail !=null ? detail.getHtml() : "-");
//		CDATASection cdata_dt2 = d.createCDATASection(detail !=null ? detail.getXml() : "");
		e.setAttribute(ATT_KIND,kind);
		e.appendChild(e_ds);
		e_ds.appendChild(cdata_ds);
//		e_ds.appendChild(cdata_ds2);
		e.appendChild(e_dt);
		e_dt.appendChild(cdata_dt);
//		e_dt.appendChild(cdata_dt2);
		return e;
	}

	public ReportNode[] find(String string) throws XPathExpressionException {
		XPath xp = XPathFactory.newInstance().newXPath();
		XPathExpression xpe = xp.compile(string);
		NodeList nl = (NodeList)(xpe.evaluate(_e,XPathConstants.NODESET));
		ReportNode[] rc = new ReportNode[nl.getLength()];
		for(int i=0;i<nl.getLength();i++)
		{
			rc[i] = new ReportNode((Element)(nl.item(i)));
		};
		return rc;
	}
}