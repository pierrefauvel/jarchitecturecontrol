package org.jarco.configuration;

import java.util.ArrayList;
import java.util.List;

import org.jarco.control.specifications.model.Specification;
import org.jarco.swing.components.IExposableAsANode;
import org.jarco.xml.FromXmlFactory;
import org.jarco.xml.IPersistableAsXml;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ConfigurationSet implements IExposableAsANode, IPersistableAsXml {

	private List<Configuration> lst=new ArrayList<Configuration>();
	
	public List<Configuration> getChildren()
	{
		return lst;
	}

	public void remove(Configuration uoFils) {
		lst.remove(uoFils);
	}

	public void add(Configuration uoFils) {
		lst.add(uoFils);
	}

	  public void fromXml(FromXmlFactory f, Element e)
//	  public static ConfigurationSet fromXml(FromXmlFactory f, Element e)
	  {
//		  ConfigurationSet cs=new ConfigurationSet();
		  NodeList nl = e.getElementsByTagName("configuration");
		  for(int i=0;i<nl.getLength();i++)
		  {
			  Element ei = (Element)(nl.item(i));
			  this.add((Configuration)(f.fromXml(ei)));
		  };
//		  return cs;
	  }
	  public String toXml()
	  {
		  StringBuffer sb=new StringBuffer();
		  sb.append("<configuration-set>");
		  for(Configuration cfgi:lst)
		  {
			  sb.append(cfgi.toXml());
		  };
		  sb.append("</configuration-set>");
		  return sb.toString();
	  }

	@Override
	public String toLabel() {
		return "<html><b>ConfigurationSet:</b>"+lst.size()+" configurations</html>";
	}
}
