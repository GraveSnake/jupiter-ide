package yas.explorer;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;

import yas.editeur.EditeurConfig;
import yas.editeur.TitrePan;
import yas.main.Main;
import yas.utils.Jdom;

public class Delete extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;

    JButton                   confirmBtn, cancelBtn;
    JLabel                    message, iconMessage;
    GroupLayout               grouplayout;
    JPanel                    allCompenent;
    String                    fileName         = "", fileType = "", SelectionPath;
    int                       SelectionPlace   = Explorer.placeOfSelection();
    Document document;
    public Delete() {
        super( Main.getInstance(), "Confirm Delete", true );

        confirmBtn = new JButton( "OK" );
        cancelBtn = new JButton( "Cancel" );
        confirmBtn.addActionListener( this );
        cancelBtn.addActionListener( this );

        SelectionPath = Explorer.getPath();
        switch ( SelectionPlace ) {
        case 2:
            fileType = "project : ";
            fileName = SelectionPath + ".";
            break;
        case 3:
            fileType = "src file. ";
            fileName = "";
            break;

        default:
            if ( SelectionPath.split( "/" )[SelectionPath.split( "/" ).length - 1].length() > 5
                    && ( SelectionPath.split( "/" )[SelectionPath.split( "/" ).length - 1].substring( SelectionPath
                            .split( "/" )[SelectionPath.split( "/" ).length - 1].length() - 5 ).equals( ".java" ) ) ) {
                fileType = "file : ";
                fileName = SelectionPath.split( "/" )[SelectionPath.split( "/" ).length - 1] + ".";
            } else {
                fileType = "package : ";
                fileName = SelectionPath.split( "/" )[SelectionPath.split( "/" ).length - 1] + ".";
            }
            break;

        }

        message = new JLabel( "Are you sure you want to delete " + fileType + fileName );
        message.setFont( new Font( "Arial", Font.BOLD, 12 ) );
        iconMessage = new JLabel( new ImageIcon( "res/explorer/deleteGrande.png" ) );
        allCompenent = new JPanel();

        grouplayout = new GroupLayout( allCompenent );
        grouplayout.setAutoCreateGaps( true );
        grouplayout.setAutoCreateContainerGaps( true );
        allCompenent.setLayout( grouplayout );

        grouplayout.setHorizontalGroup(
                grouplayout.createParallelGroup()
                        .addGroup( grouplayout.createSequentialGroup()
                                .addComponent( iconMessage )
                                .addGap( 20 )
                                .addComponent( message )

                        )
                        .addGroup( grouplayout.createSequentialGroup()
                                .addGap( 200 )
                                .addComponent( confirmBtn )
                                .addGap( 3 )
                                .addComponent( cancelBtn )

                        )
                );

        grouplayout.setVerticalGroup(
                grouplayout.createSequentialGroup()
                        .addContainerGap()
                        .addGap( 10 )
                        .addGroup( grouplayout.createParallelGroup()
                                .addComponent( iconMessage )
                                .addComponent( message )

                        )
                        .addGap( 50 )
                        .addGroup( grouplayout.createParallelGroup()
                                .addComponent( confirmBtn )
                                .addComponent( cancelBtn )

                        )
                );

        add( allCompenent, BorderLayout.CENTER );
        pack();
        setResizable( false );
        setLocationRelativeTo( null );
        setVisible( true );
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

    @Override
    public void actionPerformed( ActionEvent event ) {

        if ( "OK".equals( event.getActionCommand() ) ) {
            try {
                removeFile();
            } catch ( Exception e ) {
                
                
                e.printStackTrace();
            }
            dispose();

        } else if ( "Cancel".equals( event.getActionCommand() ) ) {
            dispose();
        }

    }

    public void removeFile() throws Exception {
      
        String pathWithoutDefault = "";
        boolean test = false;
        
        if ( "Default".equals( SelectionPath.split( "/" )[SelectionPath.split( "/" ).length - 1] ) ) {
            test = true;
        } 
        pathWithoutDefault = Explorer.WORKSPACE + "/" + SelectionPath.replace( "/Default", "" );

        if(SelectionPath.split( "/" ).length>1){
            Jdom.removeElement( SelectionPath.split( "/" )[0], SelectionPath );
        }
        RealFileDelete(pathWithoutDefault, test);
        
        Explorer.rafrechir();
        
        
        if ( TitrePan.returnTabNbr( Explorer.getPath().replaceAll( "/Default", "" ) ) != -1 ) {

            EditeurConfig.getTabbedpane().remove( TitrePan.returnTabNbr( Explorer.getPath().replaceAll( "/Default", "" ) ) );

        }
        
    }
    
    public void RealFileDelete(String pathWithoutDefault, Boolean test) {
        File file = new File( pathWithoutDefault );

        if ( test == true ) {
            for ( File f : file.listFiles() ) {
                if ( f != null ) {

                    if ( f.isFile() ) {
                        FileUtils.deleteQuietly( f );
                    }
                }
            }

        } else {

            if ( file.isFile() ) {
                FileUtils.deleteQuietly( file );

            } else if ( file.isDirectory() ) {
                try {
                    FileUtils.deleteDirectory( file );
                } catch ( IOException e ) {
                    
                    
                    e.printStackTrace();
                }

            }
        }
    }

}
