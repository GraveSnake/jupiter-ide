package yas.editeur;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import yas.explorer.Explorer;
import yas.main.Main;
import yas.menu.MyToolBar;
import yas.utils.Jdom;

public class EditeurConfig extends JPanel {

    private static final long serialVersionUID = 1L;
    static JTabbedPane        tabbedpane;
    static TitrePan           titrepan;

    public EditeurConfig() {

        tabbedpane = new JTabbedPane();
        setPreferredSize( new Dimension( 500, 450 ) );
        setLayout( new BorderLayout() );
        add( tabbedpane, BorderLayout.CENTER );

        ChangeListener changeListener = new ChangeListener() {
            public void stateChanged( ChangeEvent changeEvent ) {
                JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
                int index = sourceTabbedPane.getSelectedIndex();
                if ( index != -1 ) {
                    TitrePan titrepan = (TitrePan) getTabbedpane().getTabComponentAt( index );
                    if ( titrepan != null ) {
                        confirmDialog( titrepan.path, index, true );

                    }
                }

            }
        };

        tabbedpane.addChangeListener( changeListener );

    }

    public static void confirmDialog( String path, int index, boolean check ) {

        File file = new File( Explorer.WORKSPACE + "/" + path );

        if ( !( file.exists() ) ) {

            String[] choix = { "Yes", "No", "Cancel" };
            int reponse = JOptionPane.showOptionDialog( Main.getInstance(),
                    path.split( "/" )[path.split( "/" ).length - 1]
                            + " is Deleted from the source file, would you like to save it ?",
                    "Confirm",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, choix, choix[0] );
            if ( reponse == JOptionPane.YES_OPTION ) {

                MyToolBar.Save(true);

            }
            else if ( reponse == JOptionPane.NO_OPTION ) {

                try {
                    Jdom.removeElement( path.split( "/" )[0], path );
                    Explorer.rafrechir();

                } catch ( Exception e ) {

                }

                if ( check ) {
                    EditeurConfig.getTabbedpane().remove( index );
                }

            }

        }

    }

    public static void setTitrepan( TitrePan titrepan ) {
        EditeurConfig.titrepan = titrepan;
    }

    public static void AddTab( EditeurContent editContent, String titre, String path ) {
        titrepan = new TitrePan( titre, editContent, path );
        tabbedpane.addTab( titre, editContent );
        int nbre = tabbedpane.indexOfComponent( editContent );
        tabbedpane.setSelectedComponent( editContent );
        tabbedpane.setTabComponentAt( nbre, titrepan );
    }

    public static JTabbedPane getTabbedpane() {
        return tabbedpane;

    }

    public TitrePan getTitrepan() {
        return titrepan;
    }

}
