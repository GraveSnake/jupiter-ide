package yas.editeur;

import java.awt.BorderLayout;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class Editeur extends JPanel {

    private static final long serialVersionUID = 1L;
    JEditorPane codeEditor=null;
    
    public Editeur() {

        jsyntaxpane.DefaultSyntaxKit.initKit();

        this.setLayout( new BorderLayout() );
        codeEditor = new JEditorPane();
        JScrollPane scrPane = new JScrollPane(codeEditor);
        codeEditor.setContentType("text/java");
      
        add(scrPane,BorderLayout.CENTER);


    }
    
    
}
