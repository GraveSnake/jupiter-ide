package yas.menu;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import yas.editeur.EditeurConfig;
import yas.editeur.TitrePan;
import yas.explorer.Explorer;
import yas.main.Main;
import yas.output.ChooseMain;

public class MyToolBar extends JToolBar implements ActionListener {

    private static final long serialVersionUID = 1L;
    JButton                   cutbtn, copybtn, pastebtn, CompileExecBtn, createProjectBtn, importProjectBtn,
                              exportProjectBtn, undoBtn, redoBtn, savebtn;

    public MyToolBar() {
    	
        setFloatable(false);

        createProjectBtn = new JButton( new ImageIcon( "res/menu/new.png" ) );
        add( createProjectBtn );
        createProjectBtn.addActionListener( this );
        createProjectBtn.setName( "createProjectBtn" );

        importProjectBtn = new JButton( new ImageIcon( "res/menu/import.png" ) );
        add( importProjectBtn );
        importProjectBtn.addActionListener( this );
        importProjectBtn.setName( "importProjectBtn" );

        exportProjectBtn = new JButton( new ImageIcon( "res/menu/export.png" ) );
        add( exportProjectBtn );
        exportProjectBtn.addActionListener( this );
        exportProjectBtn.setName( "exportProjectBtn" );

        addSeparator( new Dimension( 20, 25 ) );

        cutbtn = new JButton( new ImageIcon( "res/menu/cut.png" ) );
        add( cutbtn );
        cutbtn.addActionListener( this );
        cutbtn.setName( "cutbtn" );

        copybtn = new JButton( new ImageIcon( "res/menu/copy.png" ) );
        add( copybtn );
        copybtn.addActionListener( this );
        copybtn.setName( "copybtn" );

        pastebtn = new JButton( new ImageIcon( "res/menu/paste.png" ) );
        add( pastebtn );
        pastebtn.addActionListener( this );
        pastebtn.setName( "pastebtn" );

        addSeparator( new Dimension( 20, 25 ) );

        undoBtn = new JButton( new ImageIcon( "res/menu/undo.png" ) );
        add( undoBtn );
        undoBtn.addActionListener( new jsyntaxpane.actions.UndoAction() );

        redoBtn = new JButton( new ImageIcon( "res/menu/redo.png" ) );
        add( redoBtn );
        redoBtn.addActionListener( new jsyntaxpane.actions.RedoAction() );
        addSeparator( new Dimension( 20, 25 ) );

        CompileExecBtn = new JButton( new ImageIcon( "res/menu/exec.png" ) );
        add( CompileExecBtn );
        CompileExecBtn.addActionListener( this );
        CompileExecBtn.setName( "CompileExecBtn" );

        addSeparator( new Dimension( 20, 25 ) );

        savebtn = new JButton( new ImageIcon( "res/menu/enregistrer.png" ) );
        add( savebtn );
        savebtn.addActionListener( this );
        savebtn.setName( "savebtn" );
    }

    @Override
    public void actionPerformed( ActionEvent event ) {
        JButton source = (JButton) event.getSource();

        switch ( source.getName() ) {

        case "createProjectBtn":
            new CreateProject( Main.getInstance(), "Create Project", true );
            break;
        case "importProjectBtn":
            new ImportProject( Main.getInstance(), "Import Project", true );

            break;
        case "exportProjectBtn":
            new ExportProject( Main.getInstance(), "Export Project", true );

            break;
        case "cutbtn":
            if(checkTab()){
            TitrePan titrepan = (TitrePan) EditeurConfig.getTabbedpane().getTabComponentAt( EditeurConfig.getTabbedpane().getSelectedIndex() );
            titrepan.getEditcontent().codeEditor.cut();
            }
            break;
        case "copybtn":
            if(checkTab()){

            TitrePan titrepan2 = (TitrePan) EditeurConfig.getTabbedpane().getTabComponentAt( EditeurConfig.getTabbedpane().getSelectedIndex() );
            titrepan2.getEditcontent().codeEditor.copy();
            }
            break;
        case "pastebtn":
            if(checkTab()){

            TitrePan titrepan3 = (TitrePan) EditeurConfig.getTabbedpane().getTabComponentAt( EditeurConfig.getTabbedpane().getSelectedIndex() );
            titrepan3.getEditcontent().codeEditor.paste();
            }
            break;
        case "CompileExecBtn":
            new ChooseMain( Explorer.getProjectName() );
            break;

        case "savebtn":
            Save(false);

            break;

        }

    }
    
    public boolean checkTab(){
       return  ((EditeurConfig.getTabbedpane().getTabCount()==0)?false:true);

    }

    public static void Save(boolean check) {
        if ( EditeurConfig.getTabbedpane().getSelectedIndex() != -1 ) {

            TitrePan titrepan = (TitrePan) EditeurConfig.getTabbedpane().getTabComponentAt(
                    EditeurConfig.getTabbedpane().getSelectedIndex() );
            String path = titrepan.getPath();
            
            if ( titrepan.getTitreTab().startsWith( "*" ) || check) {

                String[] retour = titrepan.getEditcontent().codeEditor.getText().split( "\n" );
                try {
                    BufferedWriter br = new BufferedWriter( new FileWriter( Explorer.WORKSPACE + "/" +path ) );
                    for ( int i = 0; i < retour.length; i++ ) {
                        br.write( retour[i] );
                        br.newLine();
                    }
                    if ( br != null ) {
                        br.flush();
                        br.close();
                    }
                } catch ( IOException e ) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if(check && !(titrepan.getTitreTab().startsWith( "*" ))){
                    titrepan.setTitreTab( titrepan.getTitreTab() );

                }else{
                    titrepan.setTitreTab( titrepan.getTitreTab().substring( 2 ) );

                }
            }

        }
    }
}
