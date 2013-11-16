package yas.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import yas.utils.WinRegistry;

@SuppressWarnings( "serial" )
public class DefineWorkspace extends JFrame implements ActionListener {

    JLabel      textinfo;
    JTextField  workspace;
    JButton     browse, ok, quit;
    JPanel      pane;
    File        file;
    MainLoading m;
    Timer timer;
    public DefineWorkspace( MainLoading mainLoading ) {
        m = mainLoading;
        setTitle( "Workspace" );
        pane = new JPanel();
        workspace = new JTextField( 12 );
        browse = new JButton( "Browse.." );
        ok = new JButton( "OK" );
        quit = new JButton( "Quit" );
        textinfo = new JLabel( "Please Specify the workspace :" );
        ok.addActionListener( this );
        quit.addActionListener( this );
        browse.addActionListener( this );

        setLayout( new BorderLayout() );
        GroupLayout grouplayout = new GroupLayout( pane );
        pane.setLayout( grouplayout );
        grouplayout.setAutoCreateGaps( true );
        grouplayout.setAutoCreateContainerGaps( true );

        grouplayout.setHorizontalGroup(
                grouplayout
                        .createParallelGroup()
                        .addGroup( grouplayout.createSequentialGroup()
                                .addGap( 20 )
                                .addComponent( textinfo )
                        )
                        .addGroup(
                                grouplayout
                                        .createSequentialGroup()
                                        .addGap( 20 )
                                        .addComponent( workspace, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                                                Short.MAX_VALUE )
                                        .addGap( 5 )
                                        .addComponent( browse )
                        )
                        .addGroup( grouplayout.createSequentialGroup()
                                .addGap( 20 )
                                .addComponent( ok )
                                .addGap( 6 )
                                .addComponent( quit )
                        )
                );

        grouplayout.setVerticalGroup(
                grouplayout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup( grouplayout.createParallelGroup( GroupLayout.Alignment.LEADING, false )
                                .addComponent( textinfo )
                        )
                        .addGap( 5 )
                        .addGroup(
                                grouplayout
                                        .createParallelGroup( GroupLayout.Alignment.LEADING, false )
                                        .addComponent( workspace, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                                                Short.MAX_VALUE )
                                        .addComponent( browse )
                        )
                        .addGap( 15 )
                        .addGroup( grouplayout.createParallelGroup()
                                .addComponent( ok )
                                .addComponent( quit )
                        )
                );
        setAlwaysOnTop( true );
        add( pane, BorderLayout.CENTER );
        setPreferredSize( new Dimension( 480, 140 ) );
        setResizable( false );
        pack();
        setLocationRelativeTo( null );
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        setVisible( true );

    }

    @Override
    public void actionPerformed( ActionEvent e ) {
        switch ( e.getActionCommand() ) {
        case "OK":
            m.aJProgressBar.setVisible( true );

            file = new File( workspace.getText().toString() );
            if ( file.exists() )
            {

                try {
                    WinRegistry.createKey( WinRegistry.HKEY_CURRENT_USER, "Software\\JIDENSAO" );
                    WinRegistry.writeStringValue( WinRegistry.HKEY_CURRENT_USER, "Software\\JIDENSAO", "Workspace",
                            file.getAbsolutePath() );
                } catch ( IllegalArgumentException | IllegalAccessException
                        | InvocationTargetException e1 ) {
                    e1.printStackTrace();
                }
               
                dispose();

                m.chargement.start();
               
                timer = new Timer(3000, new ActionListener() {  
                public void actionPerformed(ActionEvent event)
                {
                
                    m.dispose();
                    new Main();
                    timer.stop();
                    
                }
                
                });
                timer.start();

              

            } else {
                JOptionPane.showMessageDialog( this, "Path is invalid !\nPlease specify a valid folder." );
            }
            break;
        case "Quit":
            System.exit( 0 );
            break;
        case "Browse..":
            final JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
            int response = fc.showOpenDialog( DefineWorkspace.this );
            if ( response == JFileChooser.APPROVE_OPTION ) {
                workspace.setText( fc.getSelectedFile().getAbsolutePath() );
            }
            break;
        }
    }

}
