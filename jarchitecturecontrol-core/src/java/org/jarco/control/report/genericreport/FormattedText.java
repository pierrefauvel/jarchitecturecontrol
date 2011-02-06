/**
 * 
 */
package org.jarco.control.report.genericreport;

public class FormattedText
{
	private String _html;
//	private String _xml;
	
	public FormattedText(String html/*, String xml*/)
	{
		_html=html;
//		_xml=xml;
	}
	public String getHtml()
	{
		return _html;
	}
//	public String getXml()
//	{
//		return _xml;
//	}
}