/**
 * 
 */
package org.jarco.code.external;

import java.io.File;
import java.io.IOException;

import javax.xml.xpath.XPathExpressionException;

import org.jarco.collections.ImmutableList;
import org.jarco.collections.ImmutableMap;
import org.xml.sax.SAXException;

public interface IRepositorySPIRef
{
	public String getCompositeId();
	public String getVersion();
	public String getName();
	public String getComponent();
	public boolean isConcrete();
	public String getArchiveFileName();
    public ImmutableMap<String,String> getProperties() throws SAXException, IOException;
    public IRepositorySPIRef getParentRef() throws SAXException, IOException;
    public String resolveProperty(String name);
	public String getExtension();
	public ImmutableList<IRepositorySPIRef> getDependencies() throws XPathExpressionException, SAXException, IOException;
	public String toLabel();
}