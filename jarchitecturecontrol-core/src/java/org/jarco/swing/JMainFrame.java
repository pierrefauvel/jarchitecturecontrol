package org.jarco.swing;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jarco.code.external.CodeRepositoryModel;
import org.jarco.code.external.IClass;
import org.jarco.code.external.ICodeRepository;
import org.jarco.code.external.IProject;
import org.jarco.code.internal.CodeRepositoryInternal;
import org.jarco.code.internal.maven.MavenRef;
import org.jarco.code.internal.maven.MavenRepositorySPI;
import org.jarco.configuration.Configuration;
import org.jarco.configuration.ConfigurationSet;
import org.jarco.configuration.ConfigurationsModel;
import org.jarco.control.Violation;
import org.jarco.control.report.ControlGReport;
import org.jarco.control.report.IControlReport;
import org.jarco.control.report.ITagReport;
import org.jarco.control.report.TagGReport;
import org.jarco.control.report.ViolationGReport;
import org.jarco.control.report.itf.IDependenciesReport;
import org.jarco.control.report.itf.IViolationReport;
import org.jarco.control.specifications.ControlResult;
import org.jarco.control.specifications.SpecificationModel;
import org.jarco.control.specifications.model.Specification;
import org.jarco.swing.components.JTreeViewer;
import org.jarco.swing.components.TreeCellRendererWithIcons;
import org.jarco.swing.icons.JarcoIcon;
import org.jarco.tags.external.ITagRepository;
import org.jarco.tags.internal.TagRepositoryInternal;
import org.jarco.tags.internal.TagRepositoryModel;

public class JMainFrame {
		
//    static ConfigurationSet cfgs = new ConfigurationSet();
    ConfigurationsModel cfgmi = new ConfigurationsModel(new ConfigurationSet(),"D:\\Mon maven2\\repository");
 //   static Specification<ICodeRepository> s = new Specification<ICodeRepository>();
    SpecificationModel sm = new SpecificationModel(new Specification<ICodeRepository>());
//	static TagRepositoryInternal tr = new TagRepositoryInternal();
    TagRepositoryModel trmi = new TagRepositoryModel(new TagRepositoryInternal());

	private void checkButtonsEnabled()
	{
		btn_configuration.setEnabled(true);
		btn_tagrepository.setEnabled(true);
		btn_specification.setEnabled(cfgmi.getSelectedConfiguration()!=null);
		btn_control.setEnabled(cfgmi.getSelectedConfiguration()!=null);
		btn_violations.setEnabled(vr!=null);
		btn_dependencies.setEnabled(dr!=null);
		btn_tagsAndAssociations.setEnabled(tr!=null);
		btn_control_flow.setEnabled(cfr!=null);
		btn_repository_browser.setEnabled(cfr!=null);
	}

    public static void main(String[] args)
	{
    	new JMainFrame();
	}
    
    JLabel lbl_configuration;
    JLabel lbl_tagrepository;
    JLabel lbl_specification;
    JButton btn_configuration;
    JButton btn_tagrepository;
    JButton btn_specification;
    JButton btn_control;
    JButton btn_dependencies;
    JButton btn_violations;
    JButton btn_tagsAndAssociations;
    JButton btn_control_flow;
    JButton btn_repository_browser;

    private IDependenciesReport dr;
    private IViolationReport vr;
    private ITagReport tr;
    private IControlReport cfr;
    private CodeRepositoryInternal cr;
    
    public JMainFrame()
    {
	    
	    final JFrame f = new JFrame("JArchitectureControl Main Window");
	    JarcoIcon.setupSingleton(f);
	    
	    lbl_configuration = new JLabel("<pas de configuration selectionnee");
	    lbl_tagrepository = new JLabel("<pas de tag repository selectionne");
	    lbl_specification = new JLabel("<pas de specification selectionnee");
	    btn_configuration = new JButton("Configuration Editor",JarcoIcon.instance().CONFIGURATION_SET_ICON);
	    btn_tagrepository = new JButton("Tag Repository Editor", JarcoIcon.instance().TAG_REPOSITORY_ICON);
	    btn_specification = new JButton("Specification Editor",JarcoIcon.instance().SPECIFICATION_ICON);
	    btn_control = new JButton("Run control",JarcoIcon.instance().VIOLATION_ICON);
	    btn_violations = new JButton("Violations Report Viewer",JarcoIcon.instance().VIOLATION_ICON);
	    btn_dependencies = new JButton("Dependencies Report Viewer",null);
	    btn_tagsAndAssociations = new JButton("Tags and associations Report Viewer",JarcoIcon.instance().TAG_REPOSITORY_ICON);
	    btn_control_flow = new JButton("Control Flow Report Viewer",null);
	    btn_repository_browser = new JButton("Code Repository Browser",JarcoIcon.instance().TAG_REPOSITORY_ICON);
	    
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
					checkButtonsEnabled();
				}
				catch(Throwable t)
				{
					t.printStackTrace();
				}
			}
	    	
	    });

	    btn_tagrepository.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					Map<String,Object> injection = new HashMap<String,Object>();
					injection.put(TagRepositoryModel.class.getName(),trmi);
					JTagRepositoryEditor.main(f,trmi, injection);
					lbl_tagrepository.setText(trmi.getRoot().toString());
					checkButtonsEnabled();
				}
				catch(Throwable t)
				{
					t.printStackTrace();
				}
			}
	    	
	    });
	    
	    btn_specification.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					Map<String,Object> injection = new HashMap<String,Object>();
					JSpecificationEditor.main(f,sm, injection,trmi.getTagRepository(),cfgmi.getSelectedConfiguration());
					lbl_specification.setText(sm.getRoot().toString());
					checkButtonsEnabled();
				}
				catch(Throwable t)
				{
					t.printStackTrace();
				}
			}
	    	
	    });
		checkButtonsEnabled();
	    
	    btn_control.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try
				{
				Configuration configuration = cfgmi.getSelectedConfiguration();
				Specification es = (Specification)(sm.getRoot());
				
				final File reports = new File("reports");
				reports.mkdirs();

				dr = configuration.getDependenciesReport();
				final MavenRef[] mr = configuration.getMavenComponentReferences();
				MavenRepositorySPI mrspi = new MavenRepositorySPI(dr,configuration.getMavenRepositoryPath(),mr);
				cr=new CodeRepositoryInternal(
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

				
				//TODO v1.1 à reprendre dans le compte rendu de contrôle
				//es.dump(new SpecificationReport(reports,configuration.getReportPrefix()));

				/*
				FileWriter fw=new FileWriter(new File("reports/"+configuration.getReportPrefix()+".specification.xml"));
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(es.toXml());
				bw.close();
				*/
				
				cfr = new ControlGReport(reports,configuration.getReportPrefix());

				vr = new ViolationGReport(reports,configuration.getReportPrefix());
				ControlResult ctr = new ControlResult(cfr,vr);
				es.visit(cr, ctr);
				
				tr = new TagGReport(reports,configuration.getReportPrefix());
				trmi.getTagRepository().report(tr);

				//TODO v1.1 à reprendre dans le compte rendu de contrôle (dépendences)
				dr.close();
				
//				JViolationViewer ve = new JViolationViewer(f,vr);
				
				JReportViewer.main(f,vr.getReport(),"Violation");
				
				checkButtonsEnabled();
		    	}
			catch(Throwable t)
			{
				t.printStackTrace();
			}
			}
			});
	    
	    btn_violations.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new JReportViewer().main(f, vr.getReport(),"Violation");
			}
	    	
	    });
	    btn_dependencies.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new JReportViewer().main(f, dr.getReport(),"Dependencies");
			}
	    	
	    });
	    btn_tagsAndAssociations.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new JReportViewer().main(f, tr.getReport(),"Tags and associations");
			}
	    	
	    });
	    btn_control_flow.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new JReportViewer().main(f, cfr.getReport(),"Control flow");
			}
	    	
	    });
	    btn_repository_browser.addActionListener(new ActionListener(){
	    		public void actionPerformed(ActionEvent arg){
	    			try
	    			{
	    			CodeRepositoryModel crm = new CodeRepositoryModel(cr);
	    			new JCodeRepositoryBrowser().main(f, crm);
	    			}
	    			catch(Throwable t)
	    			{
	    				t.printStackTrace();
	    			}
	    		}
	    });
	    
	    f.setIconImage(JarcoIcon.instance().SPECIFICATION_IMAGE);
	    f.getContentPane().setLayout(new BoxLayout(f.getContentPane(),BoxLayout.Y_AXIS));
	    
	    {
	    	JPanel q=new JPanel();
	    	q.setLayout(new BoxLayout(q,BoxLayout.X_AXIS));
	    	q.add(btn_configuration);
	    	q.add(new JLabel("Selected configuration: "));
	    	q.add(lbl_configuration);
	    	f.getContentPane().add(q);
	    }
	    {
	    	JPanel q=new JPanel();
	    	q.setLayout(new BoxLayout(q,BoxLayout.X_AXIS));
	    	q.add(btn_tagrepository);
	    	q.add(new JLabel("Selected tag repository: "));
	    	q.add(lbl_tagrepository);
	    	f.getContentPane().add(q);
	    }
	    {
	    	JPanel q=new JPanel();
	    	q.setLayout(new BoxLayout(q,BoxLayout.X_AXIS));
	    	q.add(btn_specification);
	    	q.add(new JLabel("Selected specification: "));
	    	q.add(lbl_specification);
	    	f.getContentPane().add(q);
	    }
	    
	    f.getContentPane().add(btn_control);
	    f.getContentPane().add(btn_violations);
	    f.getContentPane().add(btn_dependencies);
	    f.getContentPane().add(btn_tagsAndAssociations);
	    f.getContentPane().add(btn_control_flow);
	    f.getContentPane().add(btn_repository_browser);

	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    f.pack();
	    f.setVisible(true);
	}
}
