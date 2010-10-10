package org.jarco.swing;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.tree.DefaultMutableTreeNode;

public class TransferableNode implements Transferable
{
	public final static DataFlavor FLAVOR =new DataFlavor(DefaultMutableTreeNode.class, "Tree node");
	private static final DataFlavor flavors[] =
	{
		FLAVOR,
		DataFlavor.stringFlavor
	};

private final DefaultMutableTreeNode treeNode;
public TransferableNode(DefaultMutableTreeNode treeNode)
{
this.treeNode = treeNode;
}

public DataFlavor[] getTransferDataFlavors()
{
	return flavors;
}

public boolean isDataFlavorSupported(DataFlavor flavor)
{
	return flavor.equals(FLAVOR) || flavor.equals(DataFlavor.stringFlavor);
}
public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
{
	if (flavor.equals(FLAVOR))
	{
		return treeNode;
	} else if (flavor.equals(DataFlavor.stringFlavor))
	{
		return treeNode.getUserObject().toString();
	} else
	{
		throw new UnsupportedFlavorException(flavor);
	}
	}
}