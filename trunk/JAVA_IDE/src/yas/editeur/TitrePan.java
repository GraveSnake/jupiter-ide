package yas.editeur;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import yas.explorer.Explorer;
import yas.main.Main;
import yas.utils.MonButton;

public class TitrePan extends JPanel {

    private static final long serialVersionUID = 1L;
    JLabel                    titreTab;
    MonButton                 closeButton;
    String 					  path;
    EditeurContent			  editcontent;


    public TitrePan( String titre, EditeurContent edcon, String path) {
        titreTab = new JLabel( titre );
        this.editcontent = edcon;
        this.path = path;
        closeButton = new MonButton( "res/main/close.png" );
        closeButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {

                if ( titreTab.getText().startsWith( "*" ) ) {

                    String[] choix = { "Yes", "No", "Cancel" };
                    int reponse = JOptionPane.showOptionDialog( Main.getInstance(),
                            "Would you like to save "+titreTab.getText().substring( 2 ) +" file ?",
                            "Confirm",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null, choix, choix[0] );
                    if ( reponse == JOptionPane.YES_OPTION ) {

                        Save(getPath());
                        
                    	EditeurConfig.getTabbedpane().remove( returnTabNbr(getPath()));


                    }
                    else if ( reponse == JOptionPane.NO_OPTION ) {

                    	EditeurConfig.getTabbedpane().remove( returnTabNbr(getPath()));

                    }

                } else {
                    EditeurConfig.getTabbedpane().remove( returnTabNbr(getPath()) );

                }
            }

        } );

        setLayout( new BorderLayout(5,0) );
        add( titreTab, BorderLayout.WEST );
        add( closeButton, BorderLayout.EAST );
        setOpaque(false);
    }

    public String getTitreTab() {
        return titreTab.getText();
    }

    public void setTitreTab( String titreTab ) {
        this.titreTab.setText( titreTab );
        repaint();
    }
    public String getPath() {
        return path;
    }

    public void setPath( String path ) {
        this.path = path;
    }
    
    
    public EditeurContent getEditcontent() {
        return editcontent;
    }

    public void setEditcontent( EditeurContent editcontent ) {
        this.editcontent = editcontent;
    }
    public static int CheckPathExist(String path){
    	for(int i=0;i<EditeurConfig.getTabbedpane().getTabCount();i++){
    		TitrePan titrepan =  (TitrePan) EditeurConfig.getTabbedpane().getTabComponentAt(i);
    		if(titrepan.path.equals(path)){
    			return i;
    		}
    	}
    	return -1;
    }
    
    
    public static int returnTabNbr(String Path) {

    	for(int i=0;i<EditeurConfig.getTabbedpane().getTabCount();i++){
            TitrePan titrepan =  (TitrePan) EditeurConfig.getTabbedpane().getTabComponentAt(i);
    		if(titrepan.path.equals(Path)){
    			return i;
    		}                
        }
        
        return -1;

    }
    
    public static void Save(String Path) {
        TitrePan titrepan =  (TitrePan) EditeurConfig.getTabbedpane().getTabComponentAt(returnTabNbr(Path));

        if ( titrepan.titreTab.getText().startsWith( "*" ) ) {
        	SaveAct(titrepan);
	        titrepan.setTitreTab(titrepan.titreTab.getText().substring(2));
	        }
    }
    
    public static void SaveAct(TitrePan titrepan){
        String[] retour = titrepan.editcontent.codeEditor.getText().split( "\n" );
        try {
            BufferedWriter br = new BufferedWriter( new FileWriter( Explorer.WORKSPACE + "/" +titrepan.path ) );
            for ( int i = 0; i < retour.length; i++ ) {
                br.write( retour[i] );
                br.newLine();
            }
            if ( br != null ) {
                br.flush();
                br.close();
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }
    
    public static boolean unSavedTabsCount(){
        
        TitrePan titrepan;
        for(int i=0;i<EditeurConfig.getTabbedpane().getTabCount();i++){
            titrepan =  (TitrePan) EditeurConfig.getTabbedpane().getTabComponentAt(i);
            if(titrepan.titreTab.getText().startsWith( "*" )){
                return true;
            }

        }
        
        return false;

    }
    
    
    public static HashMap<Integer,String> unSavedTabs(){
       
        HashMap<Integer,String> intList = new HashMap<Integer,String>();
        TitrePan titrepan;
        for(int i=0;i<EditeurConfig.getTabbedpane().getTabCount();i++){
            titrepan =  (TitrePan) EditeurConfig.getTabbedpane().getTabComponentAt(i);
            if(titrepan.titreTab.getText().startsWith( "*" )){
                intList.put( i, titrepan.path );
            }

        }
        
        return intList;

    }
    
}
