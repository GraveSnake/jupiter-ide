package yas.menu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import yas.explorer.Explorer;
import yas.utils.Jdom;

public class CreateProject extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;
    JLabel                    newProject_header, newProjectLab, sep;
    JTextField                newProjectEdit;
    JPanel                    allPan;
    JButton 				  Bcreate,Bcancel;

    public CreateProject(Frame frame, String title, boolean modal) {
    	super(frame, title, modal);
        allPan = new JPanel();
        Bcreate = new JButton("Create");
        Bcancel = new JButton("Cancel");
        Bcreate.addActionListener(this);
        Bcancel.addActionListener(this);
        GroupLayout grouplayout = new GroupLayout( allPan );
        allPan.setLayout( grouplayout );
        grouplayout.setAutoCreateGaps( true );
        grouplayout.setAutoCreateContainerGaps( true );

        newProjectEdit = new JTextField( 28 );
        newProjectLab = new JLabel( "Enter Project Name   :" );

        sep = new JLabel(new ImageIcon("res/banners/separator.png"));
		newProject_header = new JLabel(
				new ImageIcon("res/banners/createProject.png"));

        grouplayout.setHorizontalGroup(
                grouplayout.createParallelGroup()

                        .addGroup( grouplayout.createSequentialGroup()
                        		.addGap(40)
                                .addComponent( newProjectLab )
                                .addGap( 15 )
                                .addComponent( newProjectEdit , GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE )
                        )
                        .addGroup( grouplayout.createSequentialGroup()
                        		.addGap(380)
                        		.addComponent( Bcreate )
                        		.addGap(10)
                        		.addComponent(Bcancel)
                        )
                );

        grouplayout.setVerticalGroup(
                grouplayout.createSequentialGroup()
                        .addContainerGap()
                        .addGap(30)
                        .addGroup( grouplayout.createParallelGroup()
                                .addComponent( newProjectLab )
                                .addComponent( newProjectEdit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE )
                        )
                        .addGap(180)
                        .addGroup(grouplayout.createParallelGroup()
                        		.addComponent(Bcreate)
                        		.addComponent(Bcancel)
                        )
                        
                );
		add(newProject_header, BorderLayout.NORTH);
        add( allPan );
        this.setPreferredSize( new Dimension( 538, 380 ) );
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
    }

    

    public void CreateRep() throws Exception {
        String projectName = newProjectEdit.getText();
        File projectRep = new File( Explorer.WORKSPACE +"/"+ projectName );
        projectRep.mkdir();
        File srcRep = new File( Explorer.WORKSPACE +"/"+ projectName + "/src" );
        srcRep.mkdir();

        // Creating project.xml File
		Jdom jdom = new Jdom("projectName", projectName);
        jdom.enregistre( projectName );
       
        // Creating Build.xml File
        Jdom.createBuildFile( projectName );
        
        Explorer.rafrechir();
    }



	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource()==Bcreate)
		{
			if(!newProjectEdit.getText().isEmpty()){
			File ProjectFolder = new File(Explorer.WORKSPACE + "/"
					+ newProjectEdit.getText());
			if (ProjectFolder.exists()) {
				JOptionPane
						.showMessageDialog(
								this,
								"A project with the same name already exists in Workspace !\nPlease Choose a different name.",
								"",
								JOptionPane.INFORMATION_MESSAGE);
			} else {
				try {
					CreateRep();
				} catch ( Exception e) {
					e.printStackTrace();
				}
				dispose();
			}
			}
		}else if(arg0.getSource()==Bcancel)
		{
			dispose();
		}
	}
	
	protected JRootPane createRootPane() {
	    ActionListener actionListener = new ActionListener() {
	      public void actionPerformed(ActionEvent actionEvent) {
	        setVisible(false);
	      }
	    };
	    JRootPane rootPane = new JRootPane();
	    KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
	    rootPane.registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
	    return rootPane;
	  }

}
