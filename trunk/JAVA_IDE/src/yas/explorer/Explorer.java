package yas.explorer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.metal.MetalIconFactory;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import yas.editeur.EditeurConfig;
import yas.editeur.TitrePan;
import yas.output.ChooseMain;
import yas.output.Output;
import yas.utils.Jdom;
import yas.utils.WinRegistry;

public class Explorer extends JPanel implements TreeSelectionListener {

    private static final long serialVersionUID = 1L;
    public static JTree       tree;
    public static String      WORKSPACE;
    private JTextField        currentSelectionField;
    static File               fileWorkspace;
    static String             chemin           = "";
    static Document           document;
    static int                cmp              = 0, cmpNodes = 1;
    static IconNode[]         nodes            = null;
    static DefaultTreeModel   treemodel;
    boolean                   testmenu         = false, testmenu2 = false;

    public Explorer() {
        this.setLayout( new BorderLayout() );

        try {
            WORKSPACE = WinRegistry.readString( WinRegistry.HKEY_CURRENT_USER,
                    "Software\\JIDENSAO", "Workspace" );
        } catch ( IllegalArgumentException | IllegalAccessException
                | InvocationTargetException e1 ) {
            e1.printStackTrace();
        }

        fileWorkspace = new File( WORKSPACE );

        HowMany( fileWorkspace, 1 );
        if ( cmp == 0 ) {
            nodes = new IconNode[1];
        } else {
            nodes = new IconNode[cmp];

        }
        nodes[0] = new IconNode( "Workspace" );
        nodes[0].setIcon( MetalIconFactory.getFileChooserHomeFolderIcon() );
        treemodel = new DefaultTreeModel( nodes[0] );

        try {
            lister_tree( nodes[0], fileWorkspace );
        } catch ( Exception e1 ) {
            e1.printStackTrace();
        }

        tree = new JTree( nodes[0] );
        tree.putClientProperty( "JTree.icons", makeIcons() );
        tree.setCellRenderer( new IconNodeRenderer() );
        tree.setModel( treemodel );

        final ActionListener JpopupMenuItemListener = new ActionListener() {
            public void actionPerformed( ActionEvent event ) {
                switch ( event.getActionCommand() ) {
                case "New Class":
                    new CreateClass( getSrcChemin(), getPackChemin() );
                    break;
                case "New Package":
                    new CreatePackage( getSrcChemin() );
                    break;
                case "Delete":
                    new Delete();
                    break;
                case "Refresh":
                    try {
                        Explorer.rafrechir();
                    } catch ( Exception e ) {
                        e.printStackTrace();
                    }
                    break;
                case "Run":

                    try {
                        Jdom.manageBuild( getProjectName(), getFileChemin().replace( ".java", "" ) );
                    } catch ( Exception e ) {
                        e.printStackTrace();
                    }
                    Output.clear();
                    File file = new File( Explorer.WORKSPACE + "/"+getProjectName()+"/build.xml" );
                    ChooseMain.runTarget( file, "run" );

                    break;
                }

            }
        };

        final JPopupMenu menu = new JPopupMenu();
        JMenuItem newClass = new JMenuItem( "New Class", new ImageIcon(
                "res/explorer/newClass.png" ) );
        JMenuItem newPack = new JMenuItem( "New Package", new ImageIcon(
                "res/explorer/pack.png" ) );
        final JMenuItem delete = new JMenuItem( "Delete", new ImageIcon(
                "res/explorer/delete.png" ) );
        JMenuItem refresh = new JMenuItem( "Refresh", new ImageIcon(
                "res/explorer/refresh.png" ) );
       
        final JMenuItem run = new JMenuItem( "Run", new ImageIcon(
                "res/menu/exec.png" ) );

        refresh.addActionListener( JpopupMenuItemListener );
        run.addActionListener( JpopupMenuItemListener );
        delete.addActionListener( JpopupMenuItemListener );
        newClass.addActionListener( JpopupMenuItemListener );
        newPack.addActionListener( JpopupMenuItemListener );

        refresh.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_F5, 0 ) );
        newClass.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_C, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK ) );
        newPack.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_P, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK ) );
        run.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0) );

        menu.add( newClass );
        menu.add( newPack );
        menu.add( delete );
        menu.addSeparator();
        menu.add( refresh );

        this.registerKeyboardAction( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent arg0 ) {
                try {
                    Explorer.rafrechir();
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
            }
        }, KeyStroke.getKeyStroke( KeyEvent.VK_F5, 0 ), 1 );

        tree.registerKeyboardAction( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                if ( placeOfSelection() > 1 )
                    new CreateClass( getSrcChemin(), getPackChemin() );
            }
        }, KeyStroke.getKeyStroke( KeyEvent.VK_C, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK ), 1 );
        
        tree.registerKeyboardAction(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(placeOfSelection()>1)
					new CreatePackage(getSrcChemin());
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK|InputEvent.SHIFT_MASK), 1);

        tree.addMouseListener( new MouseAdapter() {
            public void mousePressed( MouseEvent e ) {
                if ( SwingUtilities.isRightMouseButton( e ) ) {
                    tree.setSelectionPath( tree.getClosestPathForLocation(
                            e.getX(), e.getY() ) );
                    if ( placeOfSelection() > 1 ) {

                        TreePath path = tree.getPathForLocation( e.getX(),
                                e.getY() );
                        Rectangle pathBounds = tree.getUI().getPathBounds( tree,
                                path );
                        if ( pathBounds != null && pathBounds.contains( e.getX(), e.getY() ) ) {
                            
                            if(getFileChemin().length()>5 && ".java".equals( getFileChemin().substring( getFileChemin().length()-5 ))){
                                if(!testmenu){
                            	menu.addSeparator();
                                menu.add( run ); 
                                testmenu = true;
                                }
                            }
                            else{
                                if(testmenu){
                                    menu.remove( 5 );
                                    menu.remove( 5 );
                                    testmenu = false;
                                }
                                
                            }
                            
                            if( placeOfSelection()==3 ){
                                if(!testmenu2){
                                menu.remove( 2 );
                                testmenu2 = true;

                                }
                            }else if(testmenu2){
                                menu.add( delete,2 );
                                testmenu2 = false;

                            }
                            
                            menu.show( tree, pathBounds.x, pathBounds.y+ pathBounds.height );
                        }
                    }
                } else if ( e.getClickCount() == 2 ) {
                    // Updated
                    if ( !( "".equals( chemin ) ) ) {
                        if ( !( "Default".equals( getPath().split( "/" )[getPath().split( "/" ).length - 1] ) ) ) {
                            
                            if ( !( new File( WORKSPACE + "/" + getPath() ).isDirectory() ) ) {
                                	if(TitrePan.CheckPathExist(getPath().replaceAll("(/Default)?", ""))==-1){
                                		CreateClass.addEditToDock( getFileChemin(), 2, getPath().replaceAll("(/Default)?", ""));
                                	}else{
                                		EditeurConfig.getTabbedpane().setSelectedIndex(TitrePan.CheckPathExist(getPath().replaceAll("(/Default)?", "")));
                                	}
                                
                            }
                            
                        }
                    }
                }

            }
        } );

        this.setMinimumSize( new Dimension( 200, 300 ) );
        tree.addTreeSelectionListener( this );

        this.add( new JScrollPane( tree ), BorderLayout.CENTER );
        currentSelectionField = new JTextField( "Current Selection: NONE" );
        this.add( currentSelectionField, BorderLayout.SOUTH );

    }

    private Hashtable<String, Icon> makeIcons() {
        Hashtable<String, Icon> icons = new Hashtable<String, Icon>();
        icons.put( "src", new ImageIcon( "res/explorer/src.png" ) );
        icons.put( "project", new ImageIcon( "res/explorer/project.png" ) );
        icons.put( "package", new ImageIcon( "res/explorer/pack.png" ) );
        icons.put( "c", TextIcons.getIcon( "c" ) );
        icons.put( "java", new ImageIcon( "res/explorer/javafile.png" ) );
        icons.put( "html", TextIcons.getIcon( "html" ) );
        return icons;
    }

    public void valueChanged( TreeSelectionEvent event ) {
        chemin = event.getPath().toString().replaceAll( "\\s", "" );

        currentSelectionField.setText( "Current Selection: " + chemin );
    }

    public static void HowMany( File fileEncours, int etat ) {

        for ( File f : fileEncours.listFiles() ) {
            cmp++;
            if ( etat == 1 ) {
                cmp++;
                if ( ifExisteBuild( f ) ) {
                    HowMany( f, 2 );

                }
            } else if ( etat == 2 ) {
                if ( f.isDirectory() ) {
                    HowMany( f, 2 );
                }

            }

        }
    }

    // public static void lister_tree(DefaultMutableTreeNode racineEncours,
    // File fileEncours, int etat) {
    //
    // for (File f : fileEncours.listFiles()) {
    // if (etat == 1) {
    //
    // if (ifExisteBuild(f)) {
    //
    // DefaultMutableTreeNode child = new DefaultMutableTreeNode(
    // f.getName());
    //
    // racineEncours.add(child);
    // lister_tree(child, f, 2);
    //
    // }
    // } else if (etat == 2) {
    // if (f.isDirectory()) {
    // DefaultMutableTreeNode child = new DefaultMutableTreeNode(
    // f.getName());
    //
    // racineEncours.add(child);
    // lister_tree(child, f, 2);
    // } else {
    // if (!("build.xml".equals(f.getName()))) {
    // DefaultMutableTreeNode child = new DefaultMutableTreeNode(
    // f.getName());
    // racineEncours.add(child);
    // }
    //
    // }
    //
    // }
    //
    // }
    // }

    public static void lister_tree( IconNode racineEncours, File fileEncours )
            throws Exception {

        for ( File f : fileEncours.listFiles() ) {

            if ( ifExisteBuild( f ) ) {

                nodes[cmpNodes] = new IconNode( f.getName()
                        .replaceAll( "\\s", "" ) );
                nodes[cmpNodes].setIconName( "project" );

                racineEncours.add( nodes[cmpNodes] );

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                document = dBuilder.parse( WORKSPACE + "/" + f.getName()
                        + "/project.xml" );

                printTree( nodes[cmpNodes++], document.getFirstChild() );

            }
        }

    }

    public static void printTree( IconNode treeNode, Node doc ) {
        try {

            NodeList nl = doc.getChildNodes();

            for ( int i = 0; i < nl.getLength(); i++ ) {
                Node node = nl.item( i );
                if ( node.getNodeType() == Node.ELEMENT_NODE ) {
                    Element element = (Element) node;

                    nodes[cmpNodes] = new IconNode( element.getChildNodes()
                            .item( 0 ).getNodeValue().replaceAll( "\\s", "" ) );

                    treeNode.add( nodes[cmpNodes] );

                    if ( "Class".equals( element.getNodeName() ) ) {
                        nodes[cmpNodes].setIconName( "java" );

                    } else if ( "Package".equals( element.getNodeName() ) ) {
                        nodes[cmpNodes].setIconName( "package" );

                    } else if ( "src".equals( element.getNodeName() ) ) {
                        nodes[cmpNodes].setIconName( "src" );

                    } else {
                        nodes[cmpNodes].setIconName( "html" );

                    }

                    printTree( nodes[cmpNodes], node );
                    cmpNodes++;
                }
            }
        } catch ( Throwable e ) {
            System.out.println( "Cannot print!! " + e.getMessage() );
        }

    }

    public static boolean ifExisteBuild( File fileEncours ) {
        if ( fileEncours.listFiles() != null ) {
            for ( File f : fileEncours.listFiles() ) {

                if ( "project.xml".equals( f.getName() ) ) {
                    return true;
                }
            }

        }
        return false;

    }

    public static void rafrechir() throws Exception {

        nodes[0].removeAllChildren();
        cmp = 0;
        cmpNodes = 1;
        HowMany( fileWorkspace, 1 );

        if ( cmp == 0 ) {
            nodes = new IconNode[1];
        } else {
            nodes = new IconNode[cmp];

        }
        nodes[0] = new IconNode( "Workspace" );
        nodes[0].setIcon( MetalIconFactory.getFileChooserHomeFolderIcon() );
        lister_tree( nodes[0], fileWorkspace );

        treemodel.setRoot( nodes[0] );
        treemodel.reload();
        tree.updateUI();
    }

    public static String getFileChemin() {
        String[] ch = chemin.substring( 1, chemin.length() - 1 ).split( "," );
        return ch[ch.length - 1];

    }

    public static String getSrcChemin() {
        String[] ch = chemin.substring( 1, chemin.length() - 1 ).split( "," );
        String ch1 = "";
        int sem = 0;
        for ( int i = 1; i < ch.length && i < 3; i++ ) {
            ch1 += ch[i];
            ch1 += "/";
            sem++;
        }

        if ( sem < 2 ) {
            ch1 += "src";
        } else {
            ch1 = ch1.substring( 0, ch1.length() - 1 );

        }

        return ch1;

    }

    public static String getPackChemin() {
        String[] ch = chemin.substring( 1, chemin.length() - 1 ).split( "," );
        String ch1 = "";
        File file = null;
        if ( ch.length < 4 ) {
            return "Default";
        } else {
            String path = WORKSPACE + "/" + ch[1] + "/" + ch[2];
            for ( int i = 3; i < ch.length; i++ ) {

                path += "/" + ch[i];
                file = new File( path );
                if ( file.exists() && file.isDirectory() ) {
                    ch1 += ch[i];
                    ch1 += ".";
                }

            }
            if ( !( "".equals( ch1 ) ) ) {
                ch1 = ch1.substring( 0, ch1.length() - 1 );
            }

            return ch1;

        }
    }

    public static int placeOfSelection() {

        return chemin.split( "," ).length;
    }

    public static String getPath() {
        String[] ch = chemin.substring( 1, chemin.length() - 1 ).split( "," );
        String ch1 = "";
        for ( int i = 1; i < ch.length; i++ ) {
            ch1 += ch[i];
            ch1 += "/";
        }
        if ( !( "".equals( ch1 ) ) ) {
            ch1 = ch1.substring( 0, ch1.length() - 1 );
        }
        return ch1;
    }

    // Updated
    public static String getProjectName() {
        if ( !( chemin.isEmpty() ) ) {
            if ( chemin.substring( 1, chemin.length() - 1 ).split( "," ).length > 1 ) {
                return chemin.substring( 1, chemin.length() - 1 ).split( "," )[1];
            } else {
                return "";
            }
        } else {
            return chemin;
        }
    }
    //
}

class IconNodeRenderer extends DefaultTreeCellRenderer {

    private static final long serialVersionUID = 1L;

    public Component getTreeCellRendererComponent( JTree tree, Object value,
            boolean sel, boolean expanded, boolean leaf, int row,
            boolean hasFocus ) {

        super.getTreeCellRendererComponent( tree, value, sel, expanded, leaf,
                row, hasFocus );

        Icon icon = ( (IconNode) value ).getIcon();

        if ( icon == null ) {
            @SuppressWarnings( "rawtypes" )
            Hashtable icons = (Hashtable) tree.getClientProperty( "JTree.icons" );
            String name = ( (IconNode) value ).getIconName();
            if ( ( icons != null ) && ( name != null ) ) {
                icon = (Icon) icons.get( name );
                if ( icon != null ) {
                    setIcon( icon );
                }
            }
        } else {
            setIcon( icon );
        }

        return this;
    }
}

class IconNode extends DefaultMutableTreeNode {

    private static final long serialVersionUID = 1L;
    protected Icon            icon;
    protected String          iconName;

    public IconNode() {
        this( null );
    }

    public IconNode( Object userObject ) {
        this( userObject, true, null );
    }

    public IconNode( Object userObject, boolean allowsChildren, Icon icon ) {
        super( userObject, allowsChildren );
        this.icon = icon;
    }

    public void setIcon( Icon icon ) {
        this.icon = icon;
    }

    public Icon getIcon() {
        return icon;
    }

    public String getIconName() {
        if ( iconName != null ) {
            return iconName;
        } else {
            String str = userObject.toString();
            int index = str.lastIndexOf( "." );
            if ( index != -1 ) {
                return str.substring( ++index );
            } else {
                return null;
            }
        }
    }

    public void setIconName( String name ) {
        iconName = name;
    }

}

class TextIcons extends MetalIconFactory.TreeLeafIcon {

    private static final long                serialVersionUID = 1L;

    protected String                         label;

    private static Hashtable<String, String> labels;

    protected TextIcons() {
    }

    public void paintIcon( Component c, Graphics g, int x, int y ) {
        super.paintIcon( c, g, x, y );
        if ( label != null ) {
            FontMetrics fm = g.getFontMetrics();

            int offsetX = ( getIconWidth() - fm.stringWidth( label ) ) / 2;
            int offsetY = ( getIconHeight() - fm.getHeight() ) / 2 - 2;

            g.drawString( label, x + offsetX, y + offsetY + fm.getHeight() );
        }
    }

    public static Icon getIcon( String str ) {
        if ( labels == null ) {
            labels = new Hashtable<String, String>();
            setDefaultSet();
        }
        TextIcons icon = new TextIcons();
        icon.label = (String) labels.get( str );
        return icon;
    }

    public static void setLabelSet( String ext, String label ) {
        if ( labels == null ) {
            labels = new Hashtable<String, String>();
            setDefaultSet();
        }
        labels.put( ext, label );
    }

    private static void setDefaultSet() {
        labels.put( "c", "C" );
        labels.put( "java", "J" );
        labels.put( "html", "H" );
        labels.put( "htm", "H" );

    }
}
