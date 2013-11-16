package yas.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import yas.explorer.Explorer;
import yas.main.MainLoading;

public class Jdom {
    public static Element  racine   = null;
    public static Document document = null;

    public Jdom( String elementName, String elementValue ) {
        racine = new Element( elementName );
        racine.setText( elementValue );
        document = new Document( racine );
        Element elementSrc = new Element( "src" );
        elementSrc.setText( "src" );
        racine.addContent( elementSrc );
    }

    
    public static void createSubElement( String elementName, String elementValue, Element elementPere ) {
        Element element = new Element( elementName );
        element.setText( elementValue );
        elementPere.addContent( element );
    }

    public static void createElementwithAttribute( String elementName, String elementValue, String attributeName,
            String AttributeValue ) {
        Element element = new Element( elementName );
        element.setText( elementValue );
        racine.addContent( element );
        Attribute attribute = new Attribute( attributeName, AttributeValue );
        element.setAttribute( attribute );
    }

    public static void affiche() {
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
            FileOutputStream FOS = new FileOutputStream( Explorer.WORKSPACE + "/" + projectName + "/project.xml" );
            sortie.output( document, FOS );
            FOS.flush();
            FOS.close();

        } catch ( FileNotFoundException e ) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }
    public void enregistreWithoutWPPath( String projectName ,String path) {
        try {
            XMLOutputter sortie = new XMLOutputter( Format.getPrettyFormat() );
            FileOutputStream FOS = new FileOutputStream( path + "/" + projectName + "/project.xml" );
            sortie.output( document, FOS );
            FOS.flush();
            FOS.close();

        } catch ( FileNotFoundException e ) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public static void enregistre( String projectName, Document document, String xmlFileName ) {
        try {
            XMLOutputter sortie = new XMLOutputter( Format.getPrettyFormat() );
            FileOutputStream FOS = new FileOutputStream( Explorer.WORKSPACE + "/" + projectName + "/" + xmlFileName );
            sortie.output( document, FOS );
            FOS.flush();
            FOS.close();

        } catch ( FileNotFoundException e ) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public static Document getDocumentInBuild( String fichier, String xmlFileName ) throws Exception {
        SAXBuilder sxb = new SAXBuilder();
        document = sxb.build( new File( Explorer.WORKSPACE + "/" + fichier + "/" + xmlFileName ) );

        return document;
    }

    public static Element getRootElementInBuild( String fichier ) throws Exception {
        Element elementToReturn = null;
        SAXBuilder sxb = new SAXBuilder();
        document = sxb.build( new File( Explorer.WORKSPACE + "/" + fichier + "/project.xml" ) );
        elementToReturn = document.getRootElement();

        return elementToReturn;
    }

    public static Element returnElement( String fichier, String nodeToReturn, Element eltt ) throws Exception
    {

        Element toReturn = null;

        List<Element> liste = null;

        racine = getRootElementInBuild( fichier );

        if ( eltt == null ) {
            Element elt2 = racine.getChild( "src" );
            liste = elt2.getChildren();

        } else {

            liste = eltt.getChildren();
        }

        Iterator<Element> i2 = liste.iterator();

        while ( i2.hasNext() ) {
            Element elementCourant = (Element) i2.next();

            if ( nodeToReturn.equals( elementCourant.getText().replaceAll( "\\s", "" ) ) ) {
                toReturn = elementCourant;

                break;
            }

        }

        return toReturn;
    }

    public static void removeElement( String projectName, String pathToFile ) throws Exception {

        String xpath = "//src";

        String parents[] = pathToFile.split( "/" );
        String lastValue = parents[parents.length - 1];
        if ( parents.length > 2 ) {
            if(parents.length==3 && lastValue.substring( lastValue.length() - 5 ).equals( ".java" ))
            {
                xpath+="/Package[normalize-space(text()[1])='Default']";
            }
            for ( int i = 2; i < parents.length - 1; i++ ) {
                xpath += "/Package[normalize-space(text()[1])='" + parents[i] + "']";
            }

            if ( lastValue.length() > 5 && ( lastValue.substring( lastValue.length() - 5 ).equals( ".java" ) ) ) {

                xpath += "/Class[normalize-space(text()[1])='" + lastValue + "']";

            } else {
                xpath += "/Package[normalize-space(text()[1])='" + lastValue + "']";

            }
        }
        Document document = getDocumentInBuild( projectName, "project.xml" );

        XPathFactory xpfac = XPathFactory.instance();
        XPathExpression<Element> xpex = xpfac.compile( xpath, Filters.element() );
        List<Element> ListElt = xpex.evaluate( document );
        ListElt.get( 0 ).getParentElement().removeContent( ListElt.get( 0 ) );

        enregistre( projectName, document, "project.xml" );

    }

    public static void manageBuild( String projectName, String attribValue ) throws Exception {

        String xpath = "//target[@name='run']/java";
        Document document = getDocumentInBuild( projectName, "build.xml" );

        XPathFactory xpfac = XPathFactory.instance();
        XPathExpression<Element> xpex = xpfac.compile( xpath, Filters.element() );
        List<Element> ListElt = xpex.evaluate( document );

        ListElt.get( 0 ).removeAttribute( "classname" );
        Attribute attribute = new Attribute( "classname", attribValue );
        ListElt.get( 0 ).setAttribute( attribute );

        enregistre( projectName, document, "build.xml" );

    }
    
    public static void manageBuildJar( String projectName, String jarDir, String mainclass ) throws Exception {
        Document document = getDocumentInBuild( projectName, "build.xml" );


        XPathFactory xpfac = XPathFactory.instance();
        
        String xpath = "//property[@name='jar.dir']";

        XPathExpression<Element> xpex = xpfac.compile( xpath, Filters.element() );
        List<Element> ListElt = xpex.evaluate( document );

        ListElt.get( 0 ).removeAttribute( "value" );
        Attribute attribute = new Attribute( "value", jarDir );
        ListElt.get( 0 ).setAttribute( attribute );

        
        xpath = "//property[@name='ant.project.name']";

        XPathExpression<Element> xpex2 = xpfac.compile( xpath, Filters.element() );
        List<Element> ListElt2 = xpex2.evaluate( document );

        ListElt2.get( 0 ).removeAttribute( "value" );
        attribute = new Attribute( "value", projectName );
        ListElt2.get( 0 ).setAttribute( attribute );
        
        xpath = "//property[@name='main-class']";

        XPathExpression<Element> xpex3 = xpfac.compile( xpath, Filters.element() );
        List<Element> ListElt3 = xpex3.evaluate( document );

        ListElt3.get( 0 ).removeAttribute( "value" );
        attribute = new Attribute( "value", mainclass );
        ListElt3.get( 0 ).setAttribute( attribute );
        
        enregistre( projectName, document, "build.xml" );

    }

    public static void CreatePackage( String projectName, String[] parents, String value, int etat ) throws Exception {
        String xpath = "//src";

        if ( etat == 1 ) {
            for ( int i = 2; i < parents.length; i++ ) {
                xpath += "/Package[normalize-space(text()[1])='" + parents[i] + "']";
            }
        }
        Document document = getDocumentInBuild( projectName, "project.xml" );

        XPathFactory xpfac = XPathFactory.instance();
        XPathExpression<Element> xpex = xpfac.compile( xpath, Filters.element() );
        List<Element> ListElt = xpex.evaluate( document );

        createSubElement( "Package", value, ListElt.get( 0 ) );
        enregistre( projectName, document, "project.xml" );

    }

    public static void CreateClass( String projectName, String parentValue, String value, int etat ) throws Exception {
        Document document = getDocumentInBuild( projectName, "project.xml" );
        XPathFactory xpfac = XPathFactory.instance();
        XPathExpression<Element> xpex;
        String xpath = "//src";
        if ( etat == 0 ) {
            xpath += "/Package[normalize-space(text()[1])='Default']";
            if ( xpfac.compile( xpath, Filters.element() ).evaluate( document ).isEmpty() ) {
                createSubElement( "Package", "Default", xpfac.compile( "//src", Filters.element() ).evaluate( document )
                        .get( 0 ) );
            }
        }
        if ( etat == 1 ) {
            String parents[] = parentValue.split( "\\p{Punct}" );
            for ( int i = 0; i < parents.length; i++ ) {
                xpath += "/Package[normalize-space(text()[1])='" + parents[i] + "']";
            }
        }
        xpex = xpfac.compile( xpath, Filters.element() );
        List<Element> ListElt = xpex.evaluate( document );

        createSubElement( "Class", value, ListElt.get( 0 ) );
        enregistre( projectName, document, "project.xml" );

    }
    
    
    public static void fillXml( String projectName, String parentValue, String value,String tagName,int etat ) throws Exception {
        SAXBuilder sxb = new SAXBuilder(); 
        Document document = sxb.build( new File( MainLoading.WP + "/" + projectName + "/project.xml" ) );
        XPathFactory xpfac = XPathFactory.instance();
        XPathExpression<Element> xpex;
        int test = 0;
        String xpath = "//src";
        for(int i=0;i<parentValue.split( "\\\\" ).length - 1;i++){
            xpath += "/Package[normalize-space(text()[1])='"+parentValue.split( "\\\\" )[i]+"']";
            test  = 1;
        }
        
        if(test == 0 && "Class".equals( tagName)){
            if(etat==0){
                createSubElement( "Package", "Default", xpfac.compile( xpath, Filters.element() ).evaluate( document ).get( 0 )  );
            }
            xpath += "/Package[normalize-space(text()[1])='Default']";

        }

        xpex = xpfac.compile( xpath, Filters.element() );
        List<Element> ListElt = xpex.evaluate( document );

        createSubElement( tagName, value, ListElt.get( 0 ) );
        enregistreWithoutWPPathDoc( projectName, document, MainLoading.WP );

    }

    public static void enregistreWithoutWPPathDoc( String projectName ,Document document,String path) {
        try {
            XMLOutputter sortie = new XMLOutputter( Format.getPrettyFormat() );
            FileOutputStream FOS = new FileOutputStream( path + "/" + projectName + "/project.xml" );
            sortie.output( document, FOS );
            FOS.flush();
            FOS.close();

        } catch ( FileNotFoundException e ) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }
    public static void createBuildFile( String projectName ) {
        Element racine = new Element( "project" );
        racine.setAttribute( new Attribute( "default", "compile" ) );
        racine.setAttribute( new Attribute( "basedir", "." ) );

        Document document = new Document( racine );

        Element element = new Element( "property" );
        element.setAttribute( new Attribute( "name", "app.home" ) );
        element.setAttribute( new Attribute( "value", "." ) );
        racine.addContent( element );

        element = new Element( "property" );
        element.setAttribute( new Attribute( "name", "app.name" ) );
        element.setAttribute( new Attribute( "value", "monappli" ) );

        racine.addContent( element );

        element = new Element( "property" );
        element.setAttribute( new Attribute( "name", "src.home" ) );
        element.setAttribute( new Attribute( "value", "${app.home}/src" ) );

        racine.addContent( element );

        element = new Element( "property" );
        element.setAttribute( new Attribute( "name", "bin.home" ) );
        element.setAttribute( new Attribute( "value", "${app.home}/bin" ) );

        racine.addContent( element );
        
        element = new Element( "property" );
        element.setAttribute( new Attribute( "name", "debuglevel" ) );
        element.setAttribute( new Attribute( "value", "source,lines,vars" ) );

        racine.addContent( element );

        element = new Element( "property" );
        element.setAttribute( new Attribute( "name", "jar.dir" ) );
        element.setAttribute( new Attribute( "value", "" ) );

        racine.addContent( element );
        
        element = new Element( "property" );
        element.setAttribute( new Attribute( "name", "ant.project.name" ) );
        element.setAttribute( new Attribute( "value", "" ) );

        racine.addContent( element ); 
        
        element = new Element( "property" );
        element.setAttribute( new Attribute( "name", "main-class" ) );
        element.setAttribute( new Attribute( "value", "" ) );

        racine.addContent( element );
     
        
        
        element = new Element( "path" );
        element.setAttribute( new Attribute( "id", "compile.classpath" ) );
        Element subElement = new Element( "pathelement" );
        subElement.setAttribute( new Attribute( "location", "${bin.home}" ) );
        element.addContent( subElement );

        racine.addContent( element );

        element = new Element( "target" );
        element.setAttribute( new Attribute( "name", "prepare" ) );
        subElement = new Element( "mkdir" );
        subElement.setAttribute( new Attribute( "dir", "${bin.home}" ) );
        element.addContent( subElement );
       

        racine.addContent( element );

        element = new Element( "target" );
        element.setAttribute( new Attribute( "name", "compile" ) );
        element.setAttribute( new Attribute( "depends", "prepare" ) );
        subElement = new Element( "javac" );
        subElement.setAttribute( new Attribute( "srcdir", "${src.home}" ) );
        subElement.setAttribute( new Attribute( "destdir", "${bin.home}" ) );
        subElement.setAttribute( new Attribute( "debug", "true" ) );
        subElement.setAttribute( new Attribute( "debuglevel", "${debuglevel}" ) );
        subElement.setAttribute( new Attribute( "includeantruntime", "false" ) );
        Element subsubElement = new Element( "classpath" );
        subsubElement.setAttribute( new Attribute( "refid", "compile.classpath" ) );
        subElement.addContent( subsubElement );
        element.addContent( subElement );

        racine.addContent( element );
        
        
        element = new Element( "target" );
        element.setAttribute( new Attribute( "name", "jar" ) );
        element.setAttribute( new Attribute( "depends", "compile" ) );
        subElement = new Element( "mkdir" );
        subElement.setAttribute( new Attribute( "dir", "${jar.dir}" ) );
        element.addContent( subElement );
        
        subElement = new Element( "jar" );
        subElement.setAttribute( new Attribute( "destfile", "${jar.dir}/${ant.project.name}.jar" ) );
        subElement.setAttribute( new Attribute( "basedir", "${app.home}/bin" ) );

        subsubElement = new Element( "manifest" );
        Element subsubsubElement = new Element( "attribute" );
        subsubsubElement.setAttribute( new Attribute( "name", "Main-Class" ) );
        subsubsubElement.setAttribute( new Attribute( "value", "${main-class}" ) );
        subsubElement.addContent( subsubsubElement );
        subElement.addContent( subsubElement );
        element.addContent( subElement );

        racine.addContent( element );
        
        
        element = new Element( "target" );
        element.setAttribute( new Attribute( "name", "run" ) );
        element.setAttribute( new Attribute( "depends", "compile" ) );
        subElement = new Element( "java" );
        subElement.setAttribute( new Attribute( "classname", "" ) );
        subsubElement = new Element( "classpath" );
        subsubsubElement = new Element( "pathelement" );
        subsubsubElement.setAttribute( new Attribute( "location", "bin" ) );
        subsubElement.addContent( subsubsubElement );
        subElement.addContent( subsubElement );
        element.addContent( subElement );

        racine.addContent( element );

        enregistre( projectName, document, "build.xml" );

    }

}
