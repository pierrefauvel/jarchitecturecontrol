package org.jarco.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jarco.code.external.IClass;
import org.jarco.code.external.ICodeRepository;
import org.jarco.code.external.IProject;
import org.jarco.code.internal.CodeRepositoryInternal;
import org.jarco.code.internal.maven.MavenRef;
import org.jarco.code.internal.maven.MavenRepositorySPI;
import org.jarco.configuration.Configuration;
import org.jarco.configuration.ConfigurationSet;
import org.jarco.configuration.ConfigurationsModel;
import org.jarco.control.report.DependenciesReport;
import org.jarco.control.report.ResultReport;
import org.jarco.control.report.TagReport;
import org.jarco.control.report.memory.InMemoryViolationReport;
import org.jarco.control.specifications.ControlResult;
import org.jarco.control.specifications.SpecificationModel;
import org.jarco.control.specifications.Violation;
import org.jarco.control.specifications.model.Specification;
import org.jarco.swing.icons.JarcoIcon;
import org.jarco.swing.tree.TreeCellRendererWithIcons;
import org.jarco.tags.external.ITagRepository;
import org.jarco.tags.internal.TagRepositoryInternal;
import org.jarco.tags.internal.TagRepositoryModel;

public class JMainFrame {
		
	public static void main(String[] args)
	{
	    final ConfigurationSet cfgs = new ConfigurationSet();
	    final ConfigurationsModel cfgmi = new ConfigurationsModel(cfgs,"D:\\Mon maven2\\repository");
	    final Specification<ICodeRepository> s = new Specification<ICodeRepository>();
	    final SpecificationModel sm = new SpecificationModel(s);
	    final TagRepositoryInternal tr = new TagRepositoryInternal();
	    final TagRepositoryModel trmi = new TagRepositoryModel(tr);
	    final JLabel lbl_configuration = new JLabel("<pas de configuration selectionnee");
	    final JLabel lbl_tagrepository = new JLabel("<pas de tag repository selectionne");
	    final JLabel lbl_specification = new JLabel("<pas de specification selectionnee");
	    
//      map.put(org.jarco.tags.external.ITagRepository.class.getName(), new PetClinicSpecification());
//	    final ModelInterface mi = new SpecificationModel(bs);

	    //TODO V1.0 brancher le tag repository
	    //TODO V1.0 sortir les icones du tree cell renderer
	    
	    final JFrame f = new JFrame("JArchitectureControl Main Window");

	    JButton btn_configuration = new JButton("Configuration Editor",TreeCellRendererWithIcons.CONFIGURATION_SET_ICON);
	    btn_configuration.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					Map<String,Object> injection = new HashMap<String,Object>();
					JConfigurationEditor.main(f,cfgmi, injection);
					Configuration cs = cfgmi.getSelectedConfiguration();
					if(cs == null)
						lbl_configuration.setText("Pas de configuration selectionnee");
					else
						lbl_configuration.setText(cs.toString());
				}
				catch(Throwable t)
				{
					t.printStackTrace();
				}
			}
	    	
	    });

	    JButton btn_tagrepository = new JButton("Tag Repository Editor", TreeCellRendererWithIcons.TAG_REPOSITORY_ICON);
	    btn_tagrepository.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					Map<String,Object> injection = new HashMap<String,Object>();
					JTagRepositoryEditor.main(f,trmi, injection);
					//TODO V1.0 à compléter qd on pourra selectionner
					lbl_configuration.setText(trmi.getRoot().toString());
				}
				catch(Throwable t)
				{
					t.printStackTrace();
				}
			}
	    	
	    });
	    
	    JButton btn_specification = new JButton("Specification Editor",TreeCellRendererWithIcons.SPECIFICATION_ICON);
	    btn_specification.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					Map<String,Object> injection = new HashMap<String,Object>();
					JSpecificationEditor.main(f,sm, injection,trmi.getTagRepository(),cfgmi.getSelectedConfiguration());
					//TODO V1.0 à compléter qd on pourra selectionner
					lbl_configuration.setText(trmi.getRoot().toString());
				}
				catch(Throwable t)
				{
					t.printStackTrace();
				}
			}
	    	
	    });
	    
	    JButton btn_control = new JButton("Run control",TreeCellRendererWithIcons.VIOLATION_ICON);
	    btn_control.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try
				{
				Configuration configuration = cfgmi.getSelectedConfiguration();
				Specification es = (Specification)(sm.getRoot());
				
				final File reports = new File("reports");
				reports.mkdirs();

				final DependenciesReport dr = configuration.getDependenciesReport();
				final MavenRef[] mr = configuration.getMavenComponentReferences();
				MavenRepositorySPI mrspi = new MavenRepositorySPI(dr,configuration.getMavenRepositoryPath(),mr);
				final CodeRepositoryInternal cr=new CodeRepositoryInternal(
						dr,
						configuration.getJDKPath(),
						mrspi
				);
				cr.flush();
				
				System.out.println("Loading projects");
				for(MavenRef mri : mr)
				{
					IProject prj = cr.findProject(mri,true);
					System.out.println("Project "+mri+":"+prj.getClasses().count()+" classes");
//					AnalysisReport.report(reports,cr.findProject(mri,true));
				}

				
				System.out.println("[Specification report]");
				
				//TODO v1.1 à reprendre dans le compte rendu de contrôle
				//es.dump(new SpecificationReport(reports,configuration.getReportPrefix()));

				/*
				FileWriter fw=new FileWriter(new File("reports/"+configuration.getReportPrefix()+".specification.xml"));
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(es.toXml());
				bw.close();
				*/
				
				System.out.println("[Control, result & violation reports]");
				ResultReport rr = new ResultReport(reports,configuration.getReportPrefix());
				InMemoryViolationReport vr = new InMemoryViolationReport();
				ControlResult ctr = new ControlResult(rr,vr);
				es.visit(cr, ctr);
				
				System.out.println("[Tag report]");
				//TODO v1.1 à reprendre dans le compte rendu de contrôle
//				TagReport tgr = new TagReport(reports,configuration.getReportPrefix());
//				tr.report(tgr);

				//TODO v1.1 à reprendre dans le compte rendu de contrôle (dépendences)
//				dr.close();
				
				Violation[] v = vr.getViolations();
				Object[][] o = new Object[v.length][];
				for(int i=0;i<v.length;i++)
				{
					o[i]=new Object[2];
					o[i][0]=v[i].getId();
					o[i][1]=v[i].getElement().getName();
				};
				
				String[] h = new String[]{"ID", "Element"};
				DefaultTableModel dtm = new DefaultTableModel(o,h)
				{
					public boolean isCellEditable( int row, int col)
					{
						return false;
					}
				};

			    JTabbedPane tbb = new JTabbedPane();
			    JTable tbl_violations = new JTable();
				tbb.add("Violations",new JScrollPane(tbl_violations));
				tbl_violations.setModel(dtm);
				System.out.println(o.length+" rows");
				JDialog dlg_result = new JDialog(f,"Control Result",true);
				dlg_result.getContentPane().setLayout(new BoxLayout(dlg_result.getContentPane(),BoxLayout.Y_AXIS));
				dlg_result.getContentPane().add(tbb);
				dlg_result.pack();
				dlg_result.setVisible(true);
		    	}
			catch(Throwable t)
			{
				t.printStackTrace();
			}
			}
			});
	    
	    //TODO V1.0 passer de 1 tag repository à N tag repository
	    //TODO V1.0 passer de 1 specification à N specifications
	    //TODO V1.0 passer les fichiers xml (configuration, tag repository, specification) à 1 fichier par instance d'entité et non 1 fichier par classe d'entités
	    
	    f.setIconImage(TreeCellRendererWithIcons.SPECIFICATION_IMAGE);
	    f.getContentPane().setLayout(new BoxLayout(f.getContentPane(),BoxLayout.Y_AXIS));
	    f.getContentPane().add(btn_configuration);
	    f.getContentPane().add(lbl_configuration);
	    f.getContentPane().add(btn_tagrepository);
	    f.getContentPane().add(lbl_tagrepository);
	    f.getContentPane().add(btn_specification);
	    f.getContentPane().add(lbl_specification);
	    f.getContentPane().add(btn_control);

	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    f.pack();
	    f.setVisible(true);
	}
}
