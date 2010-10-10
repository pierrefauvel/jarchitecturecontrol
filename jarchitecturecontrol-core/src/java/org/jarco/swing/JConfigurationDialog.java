package org.jarco.swing;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

public class JConfigurationDialog extends JDialog{

	private class JConfigurationPane extends JPanel
	{
		private JForm form;
		private Configuration configuration;
		
		public JConfigurationPane(JTabbedPane tbb)
		{
			super();
			setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
			
			form=new JForm(Configuration.class, new HashMap<String,Object>(),tbb);
			//TODO a mettre au point et completer pour avoir des sous editeurs capables de lire des MavenRef
		}
		
		public void setConfiguration(Configuration configuration)
		{
			form.fromInstance(configuration);
			this.configuration=configuration;
		}
		
		public Configuration getConfiguration()
		{
			form.toInstance();
			return configuration;
		}
	}
	
	public JConfigurationDialog(JFrame f)
	{
		super(f,"JArchitectureControl - Configuration");
		setModal(true);
		
		JPanel pnl = (JPanel)(getContentPane());
		
		pnl.setLayout(new BoxLayout(pnl,BoxLayout.Y_AXIS));
		
		final JTabbedPane tbp = new JTabbedPane(JTabbedPane.LEFT);

		JToolBar tbb = new JToolBar(JToolBar.HORIZONTAL);
		tbb.setFloatable(false);
		JButton btn_new = tbb.add(new AbstractAction("New configuration"){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Configuration configuration = new Configuration();
				JConfigurationPane pn = new JConfigurationPane(tbp);
				pn.setConfiguration(configuration);
				//TODO calcul du nom
				//TODO mise à jour du fichier de liste de configuration
			}
		});
		JButton btn_delete = tbb.add(new AbstractAction("Delete configuration"){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//TODO suppression du panel
				//TODO warning : les rapports ne seront pas supprimés
				//TODO mise à jour du fichier de liste de configuration
			}
		});
		//TODO qd fait-on les mises à jour ???
		btn_new.setEnabled(true);
		btn_delete.setEnabled(false);
		
		pnl.add(tbb);
		
		//TODO lire dans le fichier
		String[] configurations = new String[]{"configuration1", "configuration2" };
		
		for(String configuration : configurations)
		{
			JPanel pnli = new JConfigurationPane(tbp);
			tbp.add(configuration,pnli);
		}
		pnl.add(tbp);
		
		pack();
		
		Dimension d_screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension d_dialog = getSize();
		setLocation( (int)((d_screen.getWidth()- d_dialog.getWidth())/2),(int)((d_screen.getWidth()- d_dialog.getWidth())/2-24));
	}
}

