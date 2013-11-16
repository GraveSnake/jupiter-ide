package yas.menu;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Jdom {
    private Element  racine   = null;
    private Document document = null;

    public Jdom() {
        racine = new Element( "project" );
        document = new Document( racine );
    }

    public void createElement( String elementName, String elementValue ) {
        Element element = new Element( elementName );
        element.setText( elementValue );
        racine.addContent( element );
    }
    
    public void createElementwithAttribute( String elementName, String elementValue,String attributeName,String AttributeValue ) {
        Element element = new Element( elementName );
        element.setText( elementValue );
        racine.addContent( element );
        Attribute attribute = new Attribute( attributeName, AttributeValue );
        element.setAttribute( attribute );       
    }

    public void affiche() {
        try {
            XMLOutputter sortie = new XMLOutputter( Format.getPrettyFormat() );
            sortie.output( document, System.out );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public void enregistre( String projectName ) {
        try {
            XMLOutputter sortie = new XMLOutputter( Format.getPrettyFormat() );
            sortie.output( document, new FileOutputStream( "C:/Users/Administrateur/workspaceJAVAIDE/"+projectName+"/build.xml" ) );
        } catch ( FileNotFoundException e ) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

}
