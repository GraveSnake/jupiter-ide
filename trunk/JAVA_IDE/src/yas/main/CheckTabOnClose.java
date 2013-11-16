package yas.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import yas.editeur.TitrePan;

public class CheckTabOnClose extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;
    JLabel                    SaveOrNo;
    JButton                   oK, cancel, selectAll, deselectAll;
    JPanel                    panelAll, panelInsideScroll, paneltabs;
    GroupLayout               grouplayout;
    JCheckBox                 checkbox;

    public CheckTabOnClose() {
        super( Main.getInstance(), "Save Ressources", true );

        SaveOrNo = new JLabel( "Select the resources to save:" );
        oK = new JButton( "OK" );
        cancel = new JButton( "Cancel" );
        selectAll = new JButton( "SelectAll" );
        deselectAll = new JButton( "DeselectAll" );
        oK.addActionListener( this );
        cancel.addActionListener( this );
        selectAll.addActionListener( this );
        deselectAll.addActionListener( this );

        panelAll = new JPanel();
        panelInsideScroll = new JPanel();
        panelInsideScroll.setBackground(Color.WHITE);
        paneltabs = new JPanel();

        grouplayout = new GroupLayout( panelAll );
        panelAll.setLayout( grouplayout );

        panelInsideScroll.setLayout( new BoxLayout( panelInsideScroll, BoxLayout.Y_AXIS ) );
        HashMap<Integer, String> hashmap = TitrePan.unSavedTabs();
        @SuppressWarnings( "rawtypes" )
        Iterator iter = hashmap.entrySet().iterator();

        while ( iter.hasNext() ) {
            @SuppressWarnings( "rawtypes" )
            Map.Entry mEntry = (Map.Entry) iter.next();
            checkbox = new JCheckBox( mEntry.getValue().toString(), true );
            checkbox.setName( String.valueOf( mEntry.getKey() ) );
            panelInsideScroll.add( checkbox );
        }

        paneltabs.setLayout( new BorderLayout() );
        paneltabs.add( new JScrollPane( panelInsideScroll ), BorderLayout.CENTER );

        grouplayout.setHorizontalGroup( grouplayout
                .createParallelGroup()
                .addGroup( grouplayout.createSequentialGroup()
                        .addGap( 5 )
                        .addComponent( SaveOrNo )

                )
                .addGroup( grouplayout.createSequentialGroup()
                        .addGap( 5 )

                        .addComponent( paneltabs )
                        .addGap( 5 )

                )
                .addGroup( grouplayout.createSequentialGroup()
                        .addGap( 100 )
                        .addComponent( selectAll )
                        .addComponent( deselectAll )
                )
                .addGroup( grouplayout.createSequentialGroup()
                        .addGap( 100 )
                        .addComponent( oK )
                        .addComponent( cancel )
                )

                );

        grouplayout.setVerticalGroup( grouplayout
                .createSequentialGroup()
                .addContainerGap()
                .addGroup( grouplayout.createParallelGroup()

                        .addComponent( SaveOrNo )
                )
                .addGap( 10 )
                .addGroup( grouplayout.createParallelGroup()

                        .addComponent( paneltabs )
                )
                .addGap( 15 )

                .addGroup( grouplayout.createParallelGroup()
                        .addComponent( selectAll )
                        .addComponent( deselectAll )
                )
                .addGap( 30 )

                .addGroup( grouplayout.createParallelGroup()
                        .addComponent( oK )
                        .addComponent( cancel )
                )

                );
        add( panelAll, BorderLayout.CENTER );

        setPreferredSize( new Dimension( 270, 400 ) );
        setResizable( false );
        pack();
        setLocationRelativeTo( null );
        setVisible( true );
    }

    

    @Override
    public void actionPerformed( ActionEvent evt ) {

        switch ( evt.getActionCommand() ) {
        
        case "SelectAll":
            selectOrDeselectAll(true);
            break;
        case "DeselectAll":
            selectOrDeselectAll(false);
            break;
            
        case "Cancel":
            dispose(); 
             break;
             
        case "OK":
            saveChecked();
            dispose(); 
            System.exit( 0 );
            break;
        }
       

    }

    public void selectOrDeselectAll( boolean test ) {

        for ( int i = 0; i < panelInsideScroll.getComponentCount(); i++ ) {
            JCheckBox jcheckbox = (JCheckBox) panelInsideScroll.getComponent( i );
            jcheckbox.setSelected( test );
        }

    }
    
    public void saveChecked(){

        for ( int i = 0; i < panelInsideScroll.getComponentCount(); i++ ) {
            JCheckBox jcheckbox = (JCheckBox) panelInsideScroll.getComponent( i );
            
            if(jcheckbox.isSelected()){
                TitrePan.Save( jcheckbox.getText() );
            }
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
