package yas.output;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import yas.explorer.Explorer;

public class ShowProject extends JPanel implements TreeSelectionListener {

    private static final long serialVersionUID = 1L;

    JTree                     jtree;
    DefaultMutableTreeNode    root;
    public static String             chemin;
    JLabel                    ChooseLab;
    public static JLabel             checkout;
    JPanel                    allCompenent, explorer;

    public ShowProject() {
        ChooseLab = new JLabel( "Choose a prject :" );
        checkout = new JLabel( " " );
        checkout.setForeground( Color.red );
        allCompenent = new JPanel();
        explorer = new JPanel();
        explorer.setLayout( new BorderLayout() );
        lister();
        jtree = new JTree( root );
        explorer.add( new JScrollPane( jtree ), BorderLayout.CENTER );
        explorer.setPreferredSize( new Dimension( 250, 300 ) );
        jtree.addTreeSelectionListener( this );

        GroupLayout grouplayout = new GroupLayout( allCompenent );
        allCompenent.setLayout( grouplayout );

        grouplayout.setHorizontalGroup(
                grouplayout.createParallelGroup()
                        .addComponent( ChooseLab )
                        .addComponent( explorer )
                        .addGroup( grouplayout.createSequentialGroup()
                                .addGap( 6 )
                                .addComponent( checkout )
                        )

                );

        grouplayout.setVerticalGroup(
                grouplayout.createSequentialGroup()
                        .addContainerGap()
                        .addGap( 10 )
                        .addComponent( ChooseLab )
                        .addGap( 15 )
                        .addComponent( explorer )
                        .addGap( 6 )
                        .addGroup( grouplayout.createParallelGroup( GroupLayout.Alignment.LEADING, false )
                                .addComponent( checkout )
                        )
                        .addGap( 10 )

                );
        add( allCompenent, BorderLayout.CENTER );

    }

    public static String getChemin( String chemin ) {
        if ( chemin.substring( 1, chemin.length() - 1 ).split( "," ).length > 1 ) {
            return chemin.substring( 1, chemin.length() - 1 ).split( "," )[1].replaceAll( "\\s", "" );
        }else{
            return "";
        }
    }

    public void lister() {
        File file = new File( Explorer.WORKSPACE );
        root = new DefaultMutableTreeNode();

        for ( File f : file.listFiles() )
        {
            if ( f.isDirectory() ) {
                if ( Explorer.ifExisteBuild( f ) ) {
                    DefaultMutableTreeNode dir = new DefaultMutableTreeNode( f.getName() );
                    root.add( dir );
                }
            }
        }
    }

    public Boolean testOk( String chemin ) {

        Pattern pattern = Pattern.compile( "\\w{1,30}" );
        Matcher matcher = pattern.matcher( chemin );
        if ( matcher.matches() )
        {
            return true;
        } else {
            return false;

        }

    }

    @Override
    public void valueChanged( TreeSelectionEvent event ) {

        chemin = event.getPath().toString();
        if ( testOk( getChemin( chemin ) ) ) {
            checkout.setText( "" );
        }
        else {
            checkout.setText( "Invalid project folder" );
        }
    }

}
