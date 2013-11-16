package yas.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import yas.editeur.EditeurConfig;
import yas.editeur.TitrePan;
import yas.explorer.Explorer;
import yas.main.Main;
import yas.main.MainLoading;
import yas.output.ChooseMain;
import yas.utils.WinRegistry;

@SuppressWarnings( "serial" )
public class MyMenu extends JMenuBar implements ActionListener {

    JMenu menuFile, menuHelp, menuRun, menuEdit;
    JMenuItem createMI, importMI,exportMI, exitMI, copyMI, cutMI, pasteMI, undoMI, redoMI,
              runMI, aboutMI, resetwMI,saveMI;

    public MyMenu() {
        /* Creating menu  */
        menuFile = new JMenu( "File" );
        menuEdit = new JMenu( "Edit" );
        menuRun = new JMenu( "Run" );
        menuHelp = new JMenu( "Help" );

        /* Creating MenuItems */
        //File Menu
        createMI = new JMenuItem( "Create Project");
        importMI = new JMenuItem( "Import" );
        exportMI = new JMenuItem( "Export" );
        saveMI = new JMenuItem( "Save" );

        exitMI = new JMenuItem( "Exit" );
        
        
        //Edit Menu
        undoMI = new JMenuItem( "Undo" );
        redoMI = new JMenuItem( "Redo" );
        copyMI = new JMenuItem( "Copy" );
        cutMI = new JMenuItem( "Cut" );
        pasteMI = new JMenuItem( "Paste" );
        //Run Menu
        runMI = new JMenuItem( "Run" );
        //Help Menu
        aboutMI = new JMenuItem( "About" );
        resetwMI = new JMenuItem("Switch Workspace");
        
        /* joindre l'icone */
        //File Menu
        createMI.setIcon( new ImageIcon("res/menu/new.png") );
        saveMI.setIcon( new ImageIcon("res/menu/enregistrer.png") );

        importMI.setIcon( new ImageIcon("res/menu/import.png") );
        exportMI.setIcon( new ImageIcon("res/menu/export.png") );
        exitMI.setIcon( new ImageIcon("res/menu/exit.png") );
        resetwMI.setIcon( new ImageIcon("res/menu/reset.png") );
        //Edit Menu
        undoMI.setIcon( new ImageIcon("res/menu/undo.png") );
        redoMI.setIcon( new ImageIcon("res/menu/redo.png") );
        copyMI.setIcon( new ImageIcon("res/menu/copy.png") );
        cutMI.setIcon( new ImageIcon("res/menu/cut.png") );
        pasteMI.setIcon( new ImageIcon("res/menu/paste.png") );
        //Run Menu
        runMI.setIcon( new ImageIcon("res/menu/exec.png") );
        //Help Menu
        aboutMI.setIcon( new ImageIcon("res/menu/about.png") );
        
        /* joindre mnemonic */
        createMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        saveMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        exitMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
        importMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
        exportMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        
        
        /* joindre mnemonic */
        copyMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        pasteMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        cutMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        undoMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        redoMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
        
        
        runMI.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_F9, 0 ) );

        /* Creating items*/
        //File Menu
        menuFile.add( createMI );
        menuFile.addSeparator();
        menuFile.add( saveMI );
        menuFile.addSeparator();
        menuFile.add( importMI );
        menuFile.add( exportMI );
        menuFile.addSeparator();
        menuFile.add(resetwMI);
        menuFile.addSeparator();
        menuFile.add( exitMI );

        //Edit Menu
        menuEdit.add( undoMI );
        menuEdit.add( redoMI );
        menuEdit.addSeparator();
        menuEdit.add( copyMI );
        menuEdit.add( cutMI );
        menuEdit.add( pasteMI );

        //Run Menu
        menuRun.add( runMI );

        //Help Menu
        menuHelp.add( aboutMI );
        
        //Add ActionListners
        createMI.addActionListener( this );
        saveMI.addActionListener( this );
        importMI.addActionListener( this );
        exportMI.addActionListener( this );
        exitMI.addActionListener( this );
        resetwMI.addActionListener(this);
        aboutMI.addActionListener(this);
        runMI.addActionListener(this);
        undoMI.addActionListener( new jsyntaxpane.actions.UndoAction() );
        redoMI.addActionListener( new jsyntaxpane.actions.RedoAction() );
        copyMI.addActionListener(this);
        pasteMI.addActionListener(this);
        cutMI.addActionListener(this);
        
        // Add Menus to JMenuBar 
        add(menuFile);
        add(menuEdit);
        add(menuRun);
        add(menuHelp);
        
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
    	switch (e.getActionCommand()) {
		case "Create Project":
			new CreateProject(Main.getInstance(), "Create Project", true);
			break;
			
		case "Exit":
            System.exit(0);
			break;
			
		case "Switch Workspace":
			String[] choices = {"Yes","No"};
			int confirm = JOptionPane.showOptionDialog(Main.getInstance(), "Are you sure you want to reset the workspace ?\n\n* Note : The Editor will be Restarted !", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, choices, choices[1]);
			if (confirm == JOptionPane.YES_OPTION)
			{
				try {
					WinRegistry.deleteKey(WinRegistry.HKEY_CURRENT_USER, "Software\\JIDENSAO");
					Main.getInstance().dispose();
					Thread.sleep(1000);
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							//new DefineWorkspace(new MainLoading());
						    new MainLoading();
						}
					});

				} catch (Exception e2) {
					System.out.println("Error");
				}
				
			}
			break;
		case "Import":
			new ImportProject(Main.getInstance(), "Import Project", true);
			break;
		case "Export":
			new ExportProject(Main.getInstance(), "Export Project", true);
			break;
		case "About":
			JOptionPane.showMessageDialog(Main.getInstance(), " Jupiter v1.0\n Application Developed By :\n OUAFTOUH Yasser & MILHI Yassine ", "About..", JOptionPane.INFORMATION_MESSAGE);
			break;
		case "Save":
		    MyToolBar.Save(false);
            break;
		case "Copy":
		    if(checkTab()){
		    TitrePan titrepan = (TitrePan) EditeurConfig.getTabbedpane().getTabComponentAt( EditeurConfig.getTabbedpane().getSelectedIndex() );
		    titrepan.getEditcontent().codeEditor.copy();
		    }
		    break;
		case "Paste":
	          if(checkTab()){

		    TitrePan titrepan2 = (TitrePan) EditeurConfig.getTabbedpane().getTabComponentAt( EditeurConfig.getTabbedpane().getSelectedIndex() );
            titrepan2.getEditcontent().codeEditor.paste();
	          }
		    break;
		    
		case "Cut":
	          if(checkTab()){

		    TitrePan titrepan3 = (TitrePan) EditeurConfig.getTabbedpane().getTabComponentAt( EditeurConfig.getTabbedpane().getSelectedIndex() );
            titrepan3.getEditcontent().codeEditor.cut();
	          }
		    break;
		case "Run":
            new ChooseMain( Explorer.getProjectName() );
		    break;
		}

    }

    
    public boolean checkTab(){
       return  ((EditeurConfig.getTabbedpane().getTabCount()==0)?false:true);

    }
}
