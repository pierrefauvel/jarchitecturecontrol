package org.jarco.control.specifications.model;

import java.util.HashMap;
import java.util.Map;

import org.jarco.code.external.IXmlElement;
import org.jarco.collections.ImmutableMap;
import org.jarco.control.specifications.itf.IPredicate;
import org.jarco.swing.components.FM;
import org.jarco.swing.components.IExposableAsANode;
import org.jarco.swing.components.FM.kind;
import org.jarco.xml.FromXmlFactory;
import org.w3c.dom.Element;

//TODO V1.1 Dé-dupliquer avec NamePredicate
public class FilterFromXPath<T extends IXmlElement> implements IPredicate<T> {

	@FM(kind=kind.component)
	private String xpath;
	@FM(kind=kind.component)
	private String prefix;
	@FM(kind=kind.component)
	private String variableName;
	@FM(kind=kind.component)
	private String suffix;
	@FM(kind=kind.component)
	private String exactMatch;
	
	//pour l'éditeur graphique
	public FilterFromXPath()
	{
		
	}
	
	public FilterFromXPath(String xpath,String prefix, String variableName, String suffix)
	{
		this.xpath=xpath;
		this.prefix=prefix;
		this.variableName=variableName;
		this.suffix=suffix;
		this.exactMatch=null;
	}
	public FilterFromXPath(String xpath,String exactMatch)
	{
		this.xpath=xpath;
		this.prefix=null;
		this.variableName=null;
		this.suffix=null;
		this.exactMatch=exactMatch;
	}
	
	public ImmutableMap<String, String> include(IXmlElement t) {
		String n = t.getValueForXPath(xpath);
		
		if(exactMatch!=null)
		{
			if(exactMatch.compareTo(n)==0)
			{
			Map<String,String> m = new HashMap<String,String>();
			return new ImmutableMap<String,String>(m);
			}
			return null;
		}
		
		if(prefix.length()>0 && !(n.startsWith(prefix)))
			return null;
		if(suffix.length()>0 && !(n.endsWith(suffix)))
			return null;
		
//		System.out.println("PF60 Ok for "+n+" prefix="+prefix+", suffix="+suffix);
		
		Map<String,String> m = new HashMap<String,String>();
		m.put(variableName, n.substring(prefix.length(),n.length()-suffix.length()));
		return new ImmutableMap<String,String>(m);
	}

	public String toXml() {
		if(exactMatch!=null)
			return "<filter-from-x-path xpath=\""+xpath+"\" name=\""+exactMatch+"\" />";
		else
			return "<filter-from-x-path xpath=\""+xpath+"\" name-prefix=\""+prefix+"\" variable-name=\""+variableName+"\" suffix=\""+suffix+"\" />";
	}
	public String toString() {
		if(exactMatch!=null)
			return "filter from xpath xpath="+xpath+" exactMatch="+exactMatch+"";
		else
			return "filter from xpath="+xpath+" prefix="+prefix+" variable-name="+variableName+" suffix="+suffix+"</html>";
	}
	public String toLabel() {
		if(exactMatch!=null)
			return "<html><b>xpath</b>="+xpath+" <b>exactMatch</b>=\""+exactMatch+"\" </html>";
		else
			return "<html><b>xpath</b>="+xpath+" <b>prefix</b>="+prefix+" <b>variable-name</b>="+variableName+" <b>suffix</b>="+suffix+"</html>";
	}

	public void fromXml(FromXmlFactory f, Element e)
//	public static FilterFromXPath fromXml(FromXmlFactory f, Element e)
	{
		String name = e.getAttribute("name");
		String xpath = e.getAttribute("xpath");
		if(name!=null)
		{
			this.xpath=xpath;
			this.prefix=null;
			this.variableName=null;
			this.suffix=null;
			this.exactMatch=exactMatch;
		}
		else
		{
			String name_prefix = e.getAttribute("name-prefix");
			String variable_name = e.getAttribute("variable-name");
			String suffix = e.getAttribute("suffix");
			this.xpath=xpath;
			this.prefix=prefix;
			this.variableName=variableName;
			this.suffix=suffix;
			this.exactMatch=null;
		}
	}
}
