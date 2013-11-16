package yas.main;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Robot;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.io.FileUtils;

import yas.explorer.Explorer;
import yas.utils.ImagePanel;
import yas.utils.Jdom;
import yas.utils.WinRegistry;
import yas.utils.WindowUtilities;

public class MainLoading extends JWindow {
    private static final long serialVersionUID = 1L;
    ImagePanel                image;
    JLabel                    progress;
    JProgressBar              aJProgressBar;
    MainLoading               instance;
    Thread                    chargement;
    public static String      WP               = null;
    int                       etat             = 0;

    public MainLoading() {
        instance = this;
        setSize( 580, 400 );
        image = new ImagePanel( new ImageIcon( "res/main/loading.jpg").getImage() );
        image.setLayout( new BorderLayout() );
        add( image );

        aJProgressBar = new JProgressBar( JProgressBar.HORIZONTAL );
        aJProgressBar.setIndeterminate( true );
        // aJProgressBar.setString( "Please Wait ..." );
        aJProgressBar.setVisible( false );
        // aJProgressBar.setStringPainted( true );
        chargement = new chargementThreads();
        image.add( aJProgressBar, BorderLayout.SOUTH );

        // progress = new JLabel();
        // progress.setIcon(new ImageIcon("res/main/progress.gif"));
        // progress.setHorizontalAlignment(SwingConstants.CENTER);

        setLocationRelativeTo( null );
        addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing( WindowEvent e ) {
                super.windowClosing( e );
                System.exit( 0 );
            }
        } );
        setAlwaysOnTop( true );
        setVisible( true );

        try {
            WP = WinRegistry.readString( WinRegistry.HKEY_CURRENT_USER,
                    "Software\\JIDENSAO", "Workspace" );
        } catch ( IllegalArgumentException | IllegalAccessException
                | InvocationTargetException e ) {
            System.out.println( "Problem reading Reg" );
            e.printStackTrace();
        }
        if ( WP == null ) {

            new DefineWorkspace( this );

        } else {

            chargement.start();
            try {
                updateXmlFile();
                Thread.sleep( 1000 );
            } catch ( InterruptedException e ) {
                e.printStackTrace();
            }
            dispose();
            new Main();
        }

    }

    public static void main( String[] args ) throws UnsupportedLookAndFeelException {

        WindowUtilities.setNimbusLookAndFeel();
        new MainLoading();
    }

    class chargementThreads extends Thread {

        @Override
        public void run() {
            super.run();
            aJProgressBar.setVisible( true );
            try {
                new Robot().mousePress( 0 );
            } catch ( AWTException e ) {
                e.printStackTrace();
            }
        }
    }

    public void updateXmlFile() {
        File workspaceFile = new File( WP );
        for ( File file : workspaceFile.listFiles() ) {

            if ( Explorer.ifExisteBuild( file ) ) {
                FileUtils.deleteQuietly( new File( WP + "/" + file.getName() + "/" + "project.xml" ) );
                etat = 0;
                for ( File f : file.listFiles() ) {
                    if ( f.isDirectory() && "src".equals( f.getName() ) ) {

                        Jdom jdom = new Jdom( "projectName", file.getName() );
                        jdom.enregistreWithoutWPPath( file.getName(), WP );
                        updateXmlFileRecursive( f, 1 );

                    }
                }

            }

        }

    }

    public void updateXmlFileRecursive( File file, int niveau ) {
        for ( File f : file.listFiles() ) {
            String path = f.getAbsolutePath().substring( WP.length() + 1 );
            String parentValue = path.substring( path.split( "\\\\" )[0].length() + path.split( "\\\\" )[1].length()
                    + 2 );
            if ( f.isDirectory() ) {
                try {
                    Jdom.fillXml( path.split( "\\\\" )[0], parentValue, f.getName(), "Package", 1 );
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
                updateXmlFileRecursive( f, 2 );
            } else {

                try {
                      Jdom.fillXml( path.split( "\\\\" )[0], parentValue, f.getName(), "Class", etat );
                    
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
                if ( niveau == 1 ) {
                    etat=1;
                }
                 
            }

        }
    }
}
