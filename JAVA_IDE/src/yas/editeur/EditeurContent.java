package yas.editeur;

import java.awt.BorderLayout;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import yas.explorer.Explorer;
import yas.main.Main;
import yas.utils.Jdom;

public class EditeurContent extends JPanel {

    private static final long serialVersionUID = 1L;
    public JEditorPane        codeEditor       = null;
    static String             nameofClass      = "";
    int                       test             = 0;
    static boolean            check            = true;

    public EditeurContent( String nameClass ) {

        nameofClass = nameClass;
        jsyntaxpane.DefaultSyntaxKit.initKit();

        this.setLayout( new BorderLayout() );
        codeEditor = new JEditorPane();
        JScrollPane scrPane = new JScrollPane( codeEditor );
        codeEditor.setContentType( "text/java" );
        add( scrPane, BorderLayout.CENTER );

        codeEditor.addKeyListener( new KeyAdapter() {

            public void keyReleased( KeyEvent e ) {

                if ( !( checkKey( e.getKeyCode() ) ) ) {

                    if ( e.getModifiersEx() == 128 && e.getKeyCode() == 83 ) {
                        test = 1;
                    }
                    else
                    if ( !( test == 1 && e.getKeyCode() == 17 ) ) {
                        TitrePan titrepan = (TitrePan) EditeurConfig.getTabbedpane().getTabComponentAt(
                                EditeurConfig.getTabbedpane().getSelectedIndex() );
                        titrepan.setTitreTab( "* "
                                + EditeurConfig.getTabbedpane().getTitleAt(
                                        EditeurConfig.getTabbedpane().getSelectedIndex() ) );
                        test = 0;
                    }

                }
            }
        } );
    }

    public boolean addTextToEditeur( int who ) {
        StringBuffer SB = null;
        switch ( who ) {
        case 1:
            codeEditor.setText( "public class " + nameofClass + " {\n\n}" );
            break;
        case 2:
            codeEditor.setText( "public class " + nameofClass + " {\n\n public " + nameofClass + "() {\n\n }\n}" );

            break;
        case 3:

            codeEditor.setText( "public class " + nameofClass +
                    " {\n\n public " + nameofClass
                    + "() {\n\n }\n\n\n public static void main( String[] args ) {\n\n }\n\n}" );

            break;

        case 4:
            codeEditor.setText( "public class " + nameofClass +
                    " {\n\n\n public static void main( String[] args ) {\n\n }\n\n}" );
            break;
        case 5:

            SB = getTextFile( getChemin() );
            codeEditor.setText( SB.toString() );
            break;
        }
        return check;
    }

    public static String getChemin() {
        return Explorer.getSrcChemin()
                + "/"
                + ( "".equals( Explorer.getPackChemin().replace( ".", "/" ) ) ? "" : Explorer.getPackChemin().replace(
                        ".", "/" )
                        + "/" ) + Explorer.getFileChemin();
    }

    public static StringBuffer getTextFile( String path ) {
        StringBuffer st = new StringBuffer();
        BufferedReader br;
        String s;
        int t = 0;
        check = true;
        try {
            br = new BufferedReader( new FileReader( new File( Explorer.WORKSPACE + "/" + path ) ) );

            while ( ( s = br.readLine() ) != null ) {
                if ( t > 0 ) {
                    st.append( "\n" );
                }
                st.append( s );
                t++;
            }
            if ( br != null ) {
                br.close();
            }
        } catch ( FileNotFoundException e ) {
            check = false;
            confirmDialog( path, EditeurConfig.getTabbedpane().getSelectedIndex() );
        } catch ( IOException e ) {
            e.printStackTrace();
        }

        return st;

    }

    public static void confirmDialog( String path, int index ) {

        File file = new File( Explorer.WORKSPACE + "/" + path );

        if ( !( file.exists() ) ) {

            String[] choix = { "OK" };
            int reponse = JOptionPane.showOptionDialog( Main.getInstance(),
                    path.split( "/" )[path.split( "/" ).length - 1]
                            + " will be deleted from the tree",
                    "Confirm",
                    JOptionPane.OK_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, choix, choix[0] );
            if ( reponse == JOptionPane.YES_OPTION ) {

                try {
                    Jdom.removeElement( path.split( "/" )[0], path );
                    Explorer.rafrechir();

                } catch ( Exception e ) {

                }
            }

        }

    }

    public boolean checkKey( int keyCode ) {

        int[] restrictedKey = { 37, 38, 39, 40, 27, 524, 18, 16, 20, 144, 112, 113, 114, 115, 116, 117, 118, 119, 120,
                122, 155 };

        for ( int i = 0; i < restrictedKey.length; i++ ) {
            if ( restrictedKey[i] == keyCode ) {
                return true;
            }
        }

        return false;
    }

}
