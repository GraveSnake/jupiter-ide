package yas.explorer;

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

@SuppressWarnings( "serial" )
public class ShowSrcFolder extends JPanel implements  TreeSelectionListener {

    static JLabel          ChooseLab, checkout;
    JPanel                 allCompenent, explorer;
    DefaultMutableTreeNode root;
    JTree                  jtree;
    static String          chemin;

    public ShowSrcFolder() {
        ChooseLab = new JLabel( "Choose a souce folder :" );
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
        String[] ch = chemin.substring( 1, chemin.length() - 1 ).split( "," );
        String ch1 = "";
        for ( int i = 1; i < ch.length; i++ ) {
            ch1 += ch[i].substring( 1 );
            ch1 += "/";
        }
        if ( !( "".equals( ch1 ) ) ) {
            ch1 = ch1.substring( 0, ch1.length() - 1 );
        }
        return ch1;

    }

  

    public void lister() {
        File file = new File( Explorer.WORKSPACE );
        root = new DefaultMutableTreeNode();

        for ( File f : file.listFiles() )
        {
            if ( f.isDirectory() ) {
                if ( Explorer.ifExisteBuild( f ) ) {
                    DefaultMutableTreeNode dir = new DefaultMutableTreeNode( f.getName() );
                    dir.add( new DefaultMutableTreeNode( "src" ) );
                    root.add( dir );
                }
            }
        }
    }

    public Boolean testOk( String chemin ) {

        Pattern pattern = Pattern.compile( "\\w{1,30}/\\w{1,30}" );
        Matcher matcher = pattern.matcher( chemin );
        if ( matcher.matches() )
        {
            return true;
        } else {
            return false;

        }

    }

    public void valueChanged( TreeSelectionEvent event ) {
        chemin = event.getPath().toString();
        if ( testOk( getChemin( chemin ) ) ) {
            checkout.setText( "" );
        }
        else {
            checkout.setText( "Invalid source folder" );
        }

    }
}
