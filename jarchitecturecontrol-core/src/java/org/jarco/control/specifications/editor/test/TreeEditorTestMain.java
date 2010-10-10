package org.jarco.control.specifications.editor.test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.xml.xpath.XPathExpressionException;

import org.jarco.swing.JTreeEditor;
import org.jarco.swing.ModelInterface;
import org.xml.sax.SAXException;

public class TreeEditorTestMain {
	public static void main(String[] args) throws IllegalArgumentException, SecurityException, XPathExpressionException, IOException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SAXException
	{
	    final ModelInterface mi = new UserObjectModelInterface();
	    JTreeEditor.main(mi, null,null,null);
	}
}
