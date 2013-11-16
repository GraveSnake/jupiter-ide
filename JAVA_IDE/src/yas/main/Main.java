package yas.main;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSplitPane;

import yas.editeur.EditeurConfig;
import yas.editeur.TitrePan;
import yas.explorer.Explorer;
import yas.menu.MyMenu;
import yas.menu.MyToolBar;
import yas.output.Output;


@SuppressWarnings( "serial" )
public class Main extends JFrame {
    MyMenu   mymenu;
    public static Explorer explorer;
    Output   output;
    static Main jframe;
    static JLabel statusbar;
    static JSplitPane splitPaneVertical;
    public static int compteurDock = 0; 
    static MyToolBar mytoolbar;
    EditeurConfig editeurTab;
    public Main() {
        super( "Jupiter - JAVA IDE" );
        jframe = this;
        setIconImage(new ImageIcon(getClass().getResource("icon/jupiter.png")).getImage());
        mytoolbar = new MyToolBar();
        statusbar = new JLabel("Ready");
        statusbar.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        mymenu = new MyMenu();
        explorer = new Explorer();
        output = new Output();
        setJMenuBar( mymenu );
        editeurTab = new EditeurConfig();
        splitPaneVertical = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
        splitPaneVertical.setOneTouchExpandable( true );
        splitPaneVertical.setDividerLocation( 500 );
        
        JSplitPane splitPaneHorizontal = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
        splitPaneHorizontal.setOneTouchExpandable( true );
        
      
        splitPaneVertical.setTopComponent( editeurTab );
        splitPaneVertical.setBottomComponent( output );
        splitPaneHorizontal.setLeftComponent( explorer );
        splitPaneHorizontal.setRightComponent( splitPaneVertical );
      
        add( splitPaneHorizontal );
        add(mytoolbar,BorderLayout.NORTH);
        add( statusbar, BorderLayout.SOUTH);
        setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
        
        addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing( WindowEvent e ) {
                super.windowClosing( e );
               
                if(TitrePan.unSavedTabsCount()){
                    new CheckTabOnClose();
                }else{
                    System.exit( 0 );
                }
               
            }
        });
        
        pack();
        setLocationRelativeTo(null);
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setVisible( true );
        toFront();
        repaint();

    }

   
    public static Main getInstance() {
        if(Main.jframe == null)
            Main.jframe = new Main();

        return Main.jframe;
    }

}
