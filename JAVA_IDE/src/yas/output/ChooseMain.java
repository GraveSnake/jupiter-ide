package yas.output;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

import yas.explorer.CreateClass;
import yas.explorer.Explorer;
import yas.main.Main;
import yas.utils.Jdom;

public class ChooseMain extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;
    static JTextField         classEdit, projectEdit;
    JLabel                    projectLabel, classLabel;
    JButton                   browsePrjectBtn, cancelBtn;
    static JButton            browseClassBtn, finishBtn;
    JPanel                    allComponents;
    GroupLayout               grouplayout;

    public ChooseMain( String projectName ) {
        super( Main.getInstance(), "Choose a Main Class", true );

        projectLabel = new JLabel( "Project  :" );
        classLabel = new JLabel( "Main Class :" );

        projectEdit = new JTextField( projectName );
        classEdit = new JTextField();

        browsePrjectBtn = new JButton( "Browse" );
        browseClassBtn = new JButton( "Browse" );
        finishBtn = new JButton( "Finish" );
        cancelBtn = new JButton( "Cancel" );

        browsePrjectBtn.setName( "browsePrjectBtn" );
        browseClassBtn.setName( "browseClassBtn" );
        finishBtn.setName( "finishBtn" );
        cancelBtn.setName( "cancelBtn" );

        browsePrjectBtn.addActionListener( this );
        browseClassBtn.addActionListener( this );
        finishBtn.addActionListener( this );
        cancelBtn.addActionListener( this );

        allComponents = new JPanel();
        grouplayout = new GroupLayout( allComponents );
        allComponents.setLayout( grouplayout );
        grouplayout.setAutoCreateGaps( true );
        grouplayout.setAutoCreateContainerGaps( true );

        projectEdit.addKeyListener( new KeyAdapter() {

            public void keyReleased( KeyEvent e ) {
                disEnaBrowseMainClass( ExistProject( projectEdit.getText() ) );
                disEnaFinishMainClass( ExistProject( projectEdit.getText() ) );

            }

        } );
        
        classEdit.addKeyListener( new KeyAdapter() {

            public void keyReleased( KeyEvent e ) {
                
                disEnaFinishMainClass( ExistProject( projectEdit.getText()+"/src/"+classEdit.getText() ) );

            }

        } );

        grouplayout.setHorizontalGroup(
                grouplayout.createParallelGroup()
                        .addGroup( grouplayout.createSequentialGroup()
                                .addComponent( projectLabel )
                                .addGap( 34 )
                                .addComponent( projectEdit, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE )
                                .addComponent( browsePrjectBtn )

                        )
                        .addGroup(
                                grouplayout
                                        .createSequentialGroup()
                                        .addComponent( classLabel )
                                        .addGap( 14 )
                                        .addComponent( classEdit, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                                                Short.MAX_VALUE )
                                        .addComponent( browseClassBtn )

                        )
                        .addGroup(
                                grouplayout
                                        .createSequentialGroup()
                                        .addGap( 300 )
                                        .addComponent( finishBtn )
                                        .addGap( 3 )
                                        .addComponent( cancelBtn )

                        )
                );

        grouplayout.setVerticalGroup(
                grouplayout.createSequentialGroup()
                		.addGap(10)
                        .addContainerGap()
                        .addGroup( grouplayout.createParallelGroup( GroupLayout.Alignment.LEADING, false )
                                .addComponent( projectLabel )
                                .addComponent( projectEdit, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE )
                                .addComponent( browsePrjectBtn )

                        )
                        .addGap( 20 )
                        .addGroup( grouplayout.createParallelGroup( GroupLayout.Alignment.LEADING, false )
                                .addComponent( classLabel )
                                .addComponent( classEdit, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE )
                                .addComponent( browseClassBtn )

                        )
                        .addGap( 35 )
                        .addGroup(
                                grouplayout
                                        .createParallelGroup( GroupLayout.Alignment.LEADING, false )
                                        .addComponent( finishBtn )
                                        .addComponent( cancelBtn )

                        )
                );

        if ( projectName.isEmpty() ) {
            disEnaBrowseMainClass( false );

        }
        disEnaFinishMainClass( false );


        this.setPreferredSize( new Dimension( 450, 190 ) );

        add( allComponents, BorderLayout.CENTER );
        pack();
        setResizable( false );
        setLocationRelativeTo( null );
        setVisible( true );

    }

    @Override
    public void actionPerformed( ActionEvent event ) {
        JButton buttonSelected = (JButton) event.getSource();

        switch ( buttonSelected.getName() ) {

        case "browsePrjectBtn":

            int response = JOptionPane.showConfirmDialog( Main.getInstance(), new ShowProject(), "Project Selection",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE );
            if ( response == JOptionPane.OK_OPTION )
            {
                if ( ShowProject.checkout.getText().isEmpty() ) {
                    ChooseMain.setText( ShowProject.getChemin( ShowProject.chemin ), "project" );
                    ChooseMain.setText( "", "mainclass" );
                    disEnaBrowseMainClass( true );
                    disEnaFinishMainClass( false);

                }
                else {
                    browsePrjectBtn.doClick();
                }
            }

            break;
        case "browseClassBtn":
            
            int response2 = JOptionPane.showConfirmDialog( Main.getInstance(), new ShowMainClass(projectEdit.getText()), "Main Class Selection",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE );
            if ( response2 == JOptionPane.OK_OPTION )
            {
                if ( ShowMainClass.checkout.getText().isEmpty() ) {
                    ChooseMain.setText( ShowMainClass.getChemin( ShowMainClass.chemin ), "mainclass" );
                    disEnaFinishMainClass( true );

                }
                else {
                    browseClassBtn.doClick();
                }
            }

            break;
        case "finishBtn":

            try {
                Jdom.manageBuild( projectEdit.getText(), classEdit.getText().replace( ".java", "" ).split( "/" )[classEdit.getText().split( "/" ).length-1] );
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            
            Output.clear();
            
            File file = new File( Explorer.WORKSPACE + "/"+projectEdit.getText()+"/build.xml" );
            runTarget( file, "run" );
            dispose();

            break;
        case "cancelBtn":
            dispose();

            break;

        }

    }
    // a optimiser with create class
    public Boolean ExistProject( String filetocheck ) {

        
        if ( CreateClass.matchRegex( "([\\w{1,30}\\p{Punct}?\\w{1,30}?]/?)+", filetocheck ) )
        {
            File file = new File( Explorer.WORKSPACE + "/" + filetocheck );
            if ( !( file.exists() ) ) {
                return false;

            } else {
                return true;
            }
        } else {
            return false;
        }

    }

    public static void disEnaFinishMainClass( Boolean etat ) {
        finishBtn.setEnabled( etat );
    }

    public static void disEnaBrowseMainClass( Boolean etat ) {
        browseClassBtn.setEnabled( etat );
    }

    public static void setText( String path, String compenentCode ) {
        if ( "project".equals( compenentCode ) ) {
            projectEdit.setText( path );

        } else if ( "mainclass".equals( compenentCode ) ) {
            classEdit.setText( path );

        }
    }

    public static void runTarget( File buildFile, String targetName ) {
       
        ProjectHelper projectHelper = ProjectHelper.getProjectHelper();
       
        Project project = new Project();
        project.init();
        project.setUserProperty( "ant.file", buildFile.getAbsolutePath() );
       
        DefaultLogger antDefaultLogger = new DefaultLogger();
        antDefaultLogger.setEmacsMode( true );
        antDefaultLogger.setErrorPrintStream( System.err );
        antDefaultLogger.setOutputPrintStream( System.out );
        antDefaultLogger.setMessageOutputLevel( Project.MSG_ERR );
        project.addBuildListener( antDefaultLogger );
       
        project.addReference( "ant.projectHelper", projectHelper );
       
        projectHelper.parse( project, buildFile );
        try {
        	project.setJavaVersionProperty();
            project.executeTarget( targetName );
        } catch ( BuildException e ) {
           System.out.println( e.getMessage() );
        }
    }

    protected JRootPane createRootPane() {
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed( ActionEvent actionEvent ) {
                setVisible( false );
            }
        };
        JRootPane rootPane = new JRootPane();
        KeyStroke stroke = KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 );
        rootPane.registerKeyboardAction( actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW );
        return rootPane;
    }

}
