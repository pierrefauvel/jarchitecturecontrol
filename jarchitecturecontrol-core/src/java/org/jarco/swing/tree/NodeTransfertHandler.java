package org.jarco.swing.tree;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;


public class NodeTransfertHandler extends TransferHandler{

	private ModelInterface mi;
	
	public NodeTransfertHandler(ModelInterface mi)
	{
		this.mi=mi;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5898913007227597843L;
	
	@Override
	protected Transferable createTransferable(final JComponent c)
	{
//		System.out.println(c);
//	    return new StringSelection(c.toString());
        JTree tree = (JTree) c;
        TreePath[] paths = tree.getSelectionPaths();
        if (paths != null && paths.length > 0)
        {
            DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode) paths[0].getLastPathComponent();
            return new TransferableNode(node);
        }
        return null;
	}
	  @Override
	    public int getSourceActions(JComponent c)
	    {
	        return MOVE;
	    }

	  @Override
	    public boolean canImport(TransferHandler.TransferSupport support)
	    {
	        if (!support.isDataFlavorSupported(TransferableNode.FLAVOR) ||
	                !support.isDrop())
	        {
	            return false;
	        }

	        JTree.DropLocation dropLocation =
	                (JTree.DropLocation) support.getDropLocation();
	        if (dropLocation.getPath() == null)
	        {
	            return false;
	        }

	        //ajout
	        Transferable transferable = support.getTransferable();
	        DefaultMutableTreeNode transferData;
	        try
	        {
	        transferData = (DefaultMutableTreeNode) transferable.getTransferData(TransferableNode.FLAVOR);
	        }
	        catch(Throwable t)
	        {
	        	t.printStackTrace();
	        	return false;
	        }
	        
	        if(dropLocation.getPath()==null) return false;
	        
	        DefaultMutableTreeNode parent = (DefaultMutableTreeNode)(dropLocation.getPath().getLastPathComponent());

	        Object uo_fils = transferData.getUserObject();
	        Object uo_pere = parent.getUserObject();
	        
	        boolean b = mi.acceptChild(uo_pere,uo_fils);
	        
	        return b;
	    }


	@Override
    public boolean importData(TransferHandler.TransferSupport support)
    {
        if (!canImport(support))
        {
            return false;
        }

        JTree tree = (JTree) support.getComponent();
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        JTree.DropLocation dropLocation =
                (JTree.DropLocation) support.getDropLocation();
        TreePath path = dropLocation.getPath();
        Transferable transferable = support.getTransferable();
        DefaultMutableTreeNode transferData;
        try
        {
            transferData = (DefaultMutableTreeNode) transferable.getTransferData(TransferableNode.FLAVOR);
        } catch (IOException e)
        {
            return false;
        } catch (UnsupportedFlavorException e)
        {
            return false;
        }

        int childIndex = dropLocation.getChildIndex();
        if (childIndex == -1)
        {
            childIndex = model.getChildCount(path.getLastPathComponent());
        }

        DefaultMutableTreeNode newNode =
                (DefaultMutableTreeNode) transferData;
        DefaultMutableTreeNode parentNode =
                (DefaultMutableTreeNode) path.getLastPathComponent();
        model.insertNodeInto(newNode, parentNode, childIndex);

        Object uo_fils = (Object)(((DefaultMutableTreeNode)newNode).getUserObject());
        Object uo_parent = (Object)(((DefaultMutableTreeNode)parentNode).getUserObject());
        
        mi.addChild(uo_parent, uo_fils);

        TreePath newPath = path.pathByAddingChild(newNode);
        tree.makeVisible(newPath);
        tree.scrollRectToVisible(tree.getPathBounds(newPath));

        
        return true;
    }

    @Override
    protected void exportDone(JComponent source, Transferable data, int action)
    {
        if (action == MOVE)
        {
        	System.out.println("exportDone "+source+","+data+","+action);
        	
            JTree tree = (JTree) source;
            DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
            DefaultMutableTreeNode transferData;
            try
            {
                transferData = (DefaultMutableTreeNode) data.getTransferData(TransferableNode.FLAVOR);

                Object uo_fils = (Object)(((DefaultMutableTreeNode)transferData).getUserObject());
                Object uo_parent = (Object)(((DefaultMutableTreeNode)transferData.getParent()).getUserObject());

                mi.removeChild(uo_parent,uo_fils);
                
                model.removeNodeFromParent(transferData);

            } catch (IOException e)
            {
                return;
            } catch (UnsupportedFlavorException e)
            {
                return;
            }
        }

    }

}
