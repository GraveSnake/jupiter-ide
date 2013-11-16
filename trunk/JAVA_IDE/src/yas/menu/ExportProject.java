package yas.menu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import yas.explorer.Explorer;
import yas.main.Main;
import yas.output.ChooseMain;
import yas.output.ShowMainClass;
import yas.output.ShowProject;
import yas.utils.Jdom;

@SuppressWarnings("serial")
public class ExportProject extends JDialog implements ActionListener{
		JLabel export_header,export_label,filetoexportLa,mainClassLab;
		JTextField outputfolder,filetoexportEd,mainClassEd;
		JButton browse, ok, cancel,browseFile,browseMain;
		JPanel pane;
		File directory = new File("");
		
	public ExportProject(Frame frame, String title, boolean modal) {
		super(frame, title, modal);
		pane = new JPanel();
		export_header = new JLabel(new ImageIcon("res/banners/ExportEntete.jpg"));
		outputfolder = new JTextField(8);
		export_label = new JLabel("Output Folder :");
		filetoexportLa = new JLabel("Project :");
		filetoexportEd = new JTextField(8);
		mainClassLab = new JLabel("Main-Class :");
		mainClassEd = new JTextField(8);
		
		filetoexportEd.addKeyListener( new KeyAdapter() {

	            public void keyReleased( KeyEvent e ) {
	                
	                disEnaFinishBtn	(ExistProject()) ; 
	                disEnaBrowseBtn (ExistProject()) ; 
	                
	            }
		 });
		
		mainClassEd.addKeyListener( new KeyAdapter() {

            public void keyReleased( KeyEvent e ) {
                
                
                if( ExistProject() && !(outputfolder.getText().isEmpty())){
                    disEnaFinishBtn (ExistMain()) ;

 
                }else{
                    disEnaFinishBtn(false);
                }
                
                
            }
     });
		browseFile  =new JButton("Browse ..");
		browseMain =new JButton("Browse.. ");
		browse = new JButton("Browse...");
		ok = new JButton("OK");
		cancel = new JButton("Cancel");
	
		browseMain.addActionListener(this);
		browseFile.addActionListener(this);
		browse.addActionListener(this);
		ok.addActionListener(this);
		cancel.addActionListener(this);
		
		GroupLayout grouplayout = new GroupLayout(pane);
		pane.setLayout(grouplayout);
        grouplayout.setAutoCreateGaps( true );
        grouplayout.setAutoCreateContainerGaps( true );
        
        grouplayout.setHorizontalGroup(
        		grouplayout.createParallelGroup()
        		.addGroup(grouplayout.createSequentialGroup()
                        .addGap(20)
                        .addComponent(filetoexportLa)
                        .addGap(46)
                        .addComponent(filetoexportEd, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(5)
                        .addComponent(browseFile)
                        )
                .addGroup(grouplayout.createSequentialGroup()
                        .addGap(20)
                        .addComponent(mainClassLab)
                        .addGap(22)
                        .addComponent(mainClassEd, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(5)
                        .addComponent(browseMain)
                        )
        		.addGroup(grouplayout.createSequentialGroup()
        				.addGap(20)
        				.addComponent(export_label)
        				.addGap(10)
                		.addComponent(outputfolder, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        				.addGap(5)
        				.addComponent(browse)
        				)
        		.addGroup(grouplayout.createSequentialGroup()
        				.addGap(400)
        				.addComponent(ok)
        				.addGap(10)
        				.addComponent(cancel)
        				)
        		);

        
        grouplayout.setVerticalGroup(
        		grouplayout.createSequentialGroup()
        		.addContainerGap()
        		.addGap(20)
        		 .addGroup(grouplayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                        .addComponent(filetoexportLa)
                        .addComponent(filetoexportEd, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(browseFile)
                )
                .addGap( 10 )
                .addGroup(grouplayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                		.addComponent(export_label)
                		.addComponent(outputfolder, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                		.addComponent(browse)
                )
                .addGap( 10 )
                .addGroup(grouplayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                        .addComponent(mainClassLab)
                        .addComponent(mainClassEd, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(browseMain)
                )
                .addGap(120)
                .addGroup(grouplayout.createParallelGroup()
                		.addComponent(ok)
                		.addComponent(cancel)
                )
        );
        
        disEnaBrowseBtn(false);
        disEnaFinishBtn( false );
        
        add(export_header,BorderLayout.NORTH);
		add(pane, BorderLayout.CENTER);
		
        setPreferredSize(new Dimension(538,380));
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
		setVisible(true);
	}



		@Override
		public void actionPerformed(ActionEvent e) {
			switch (e.getActionCommand()) {
			case "Browse...":
				final JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int response = fc.showDialog(this,"Select");
				if (response == JFileChooser.APPROVE_OPTION) {
					outputfolder.setText(fc.getSelectedFile().getAbsolutePath());
					if(!(mainClassEd.getText().isEmpty()) && !(filetoexportEd.getText().isEmpty()) ){
                        disEnaFinishBtn (true) ; 

                    }else{
                        disEnaFinishBtn (false) ; 

                    }
				}
				break;
				
			case "Browse ..":
			    int response2 = JOptionPane.showConfirmDialog( Main.getInstance(), new ShowProject(), "Project Selection",
	                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE );
	            if ( response2 == JOptionPane.OK_OPTION )
	            {
	                if ( ShowProject.checkout.getText().isEmpty() ) {
	                    filetoexportEd.setText( ShowProject.getChemin( ShowProject.chemin ) );
	                       disEnaBrowseBtn (true) ;
	                       if(!(mainClassEd.getText().isEmpty()) && !(outputfolder.getText().isEmpty()) ){
	                           disEnaFinishBtn (true) ; 

	                       }else{
	                            disEnaFinishBtn (false) ; 

	                        }

	                }
	                else {
	                    browseFile.doClick();
	                }
	            }
			    break;
			case "Browse.. ":
			    int response3 = JOptionPane.showConfirmDialog( Main.getInstance(), new ShowMainClass(filetoexportEd.getText()), "Main Class Selection",
	                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE );
	            if ( response3 == JOptionPane.OK_OPTION )
	            {
	                if ( ShowMainClass.checkout.getText().isEmpty() ) {
	                    mainClassEd.setText( ShowMainClass.getChemin( ShowMainClass.chemin ) );
                        
	                    if(!(outputfolder.getText().isEmpty()) && !(outputfolder.getText().isEmpty()) ){
                            disEnaFinishBtn (true) ; 

                        }else{
                            disEnaFinishBtn (false) ; 

                        }
	                }
	                else {
	                    browseMain.doClick();
	                }
	            }
			    break;
			
			
			case "OK":
				directory = new File(outputfolder.getText());
				if(directory.exists() && directory.isDirectory())
				{
				    try {
		                Jdom.manageBuildJar( filetoexportEd.getText(), outputfolder.getText(),mainClassEd.getText().replace( ".java", "" ).split( "/" )[mainClassEd.getText().split( "/" ).length-1] );
		            } catch ( Exception exec ) {
		                exec.printStackTrace();
		            }
		            
				    
				    
				    ChooseMain.runTarget(new File(Explorer.WORKSPACE+"/"+filetoexportEd.getText()+"/build.xml"),"jar");
				    dispose();
				}else{
					JOptionPane.showMessageDialog(this, "Invalid folder path !");
				}
				break;
			case "Cancel":
				dispose();
				break;
			}
		}
		
		public boolean ExistProject(){
		    File file = new File(Explorer.WORKSPACE+"/"+filetoexportEd.getText());
		    if(filetoexportEd.getText().isEmpty()){
		           return false;
 
		    }else{
		          return file.exists();

		    }
		    
		}
		
		public boolean ExistMain(){
            File file = new File(Explorer.WORKSPACE+"/"+filetoexportEd.getText()+"/src/"+mainClassEd.getText());
            if(mainClassEd.getText().isEmpty()){
                   return false;
 
            }else{
                  return file.exists();

            }
            
        }
		
		
		
		 public void disEnaFinishBtn( Boolean test ) {
		     ok.setEnabled( test );
		    }
		 
		 public void disEnaBrowseBtn( Boolean test ) {
             browseMain.setEnabled( test );
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