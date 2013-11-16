package yas.output;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import yas.explorer.Explorer;

public class ShowMainClass extends JPanel implements TreeSelectionListener {

    private static final long serialVersionUID = 1L;

    JTree                     jtree;
    DefaultMutableTreeNode    root;
    public static String             chemin;
    JLabel                    ChooseLab;
    public static JLabel             checkout;
    JPanel                    allCompenent, explorer;

    public ShowMainClass(String project) {

        ChooseLab = new JLabel( "Choose the main class :" ); // Here
        checkout = new JLabel( " " );
        checkout.setForeground( Color.red );
        allCompenent = new JPanel();
        explorer = new JPanel();
        explorer.setLayout( new BorderLayout() );
        root = new DefaultMutableTreeNode( project );

        File file = new File( Explorer.WORKSPACE + "/" + project + "/src" );
        lister_recursive( file, root );

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

    public void lister_recursive( File file, DefaultMutableTreeNode node ) {

        for ( File f : file.listFiles() ) {
            if ( f.isDirectory() ) {
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode( f.getName() );
                node.add( childNode );
                lister_recursive( f, childNode );
            } else {
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode( f.getName() );
                node.add( childNode );
            }
        }

    }

    public static String getChemin( String chemin ) {
        String[] tabChemain = chemin.substring( 1, chemin.length() - 1 ).split( "," );
        if ( tabChemain.length > 1 ) {
            String ch1 = "";
            for ( int i = 1; i < tabChemain.length; i++ ) {
                ch1 += tabChemain[i].replaceAll( "\\s", "" ) ;
                ch1 += "/";
            }
            if ( !( "".equals( ch1 ) ) ) {
                ch1 = ch1.substring( 0, ch1.length() - 1 );
            }
            return ch1;
           

        } else {
            return "";
        }
    }

    public Boolean testOk( String chemin ) {

        if ( chemin.length()>5 && ".java".equals( chemin.substring( chemin.length()-5 ) ))
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
            checkout.setText( "Invalid class" );
        }

    }

}
