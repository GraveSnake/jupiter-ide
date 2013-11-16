package yas.explorer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import yas.editeur.EditeurConfig;
import yas.editeur.EditeurContent;
import yas.editeur.TitrePan;
import yas.main.Main;
import yas.utils.Jdom;

@SuppressWarnings( "serial" )
public class CreateClass extends JDialog {
    JLabel        javaClassEntete, sourceFolderLab, packageLab, separator1,
                  nameClassLab, modeMethodLab, separator2;
    static JLabel msgError;
    static JTextField sourceFolderEdit, packageEdit, nameClassEdit;
    JPanel            allComponents;
    JButton           browsesourceFolderbtn, browsepackagebtn;
    static JButton    finishBtn;
    JButton           cancelBtn;
    static JCheckBox  checkMain, checkConstructeur, checkAbstract;

    public CreateClass( String src, String pack ) {
        super( Main.getInstance(), "New Java Class", true );
        // Definition des ActionListener

        final ActionListener browsesourceFolderListener = new ActionListener() {
            public void actionPerformed( ActionEvent event ) {
                int response = JOptionPane
                        .showConfirmDialog( Main.getInstance(),
                                new ShowSrcFolder(), "Source Folder Selection",
                                JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.PLAIN_MESSAGE );
                if ( response == JOptionPane.OK_OPTION ) {
                    if ( ShowSrcFolder.checkout.getText().isEmpty() ) {
                        CreateClass.setText(
                                ShowSrcFolder.getChemin( ShowSrcFolder.chemin ),
                                "srcfolder" );
                        CreateClass.setText( "", "package" );
                    } else {
                        browsesourceFolderbtn.doClick();
                    }
                }
            }
        };

        final ActionListener browsepackageListener = new ActionListener() {
            public void actionPerformed( ActionEvent event ) {
                int response = JOptionPane.showConfirmDialog(
                        Main.getInstance(), new ShowPackages(),
                        "Packages Selection", JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE );
                if ( response == JOptionPane.OK_OPTION ) {
                    if ( ShowPackages.checkout.getText().isEmpty() ) {
                        CreateClass.setText(
                                ShowPackages.getChemin( ShowPackages.chemin ),
                                "package" );
                        CreateClass
                                .disEnaFinishBtn( CreateClass
                                        .Existpackage( CreateClass.packageEdit
                                                .getText() ) );
                    } else {
                        browsepackagebtn.doClick();
                    }
                }
            }
        };

        final ActionListener finishListener = new ActionListener() {
            public void actionPerformed( ActionEvent event ) {
                try {
                    creerClass();
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
                dispose();
            }
        };

        final ActionListener cancelListener = new ActionListener() {
            public void actionPerformed( ActionEvent event ) {
                dispose();
            }
        };

        // creation des Compenents

        javaClassEntete = new JLabel( new ImageIcon(
                "res/banners/javaClassEntete.png" ) );
        sourceFolderLab = new JLabel( "Source folder :" );
        sourceFolderEdit = new JTextField( src, 30 );
        browsesourceFolderbtn = new JButton( "browse" );
        browsesourceFolderbtn.addActionListener( browsesourceFolderListener );
        packageLab = new JLabel( "Package :" );
        packageEdit = new JTextField( pack, 30 );
        browsepackagebtn = new JButton( "browse" );
        browsepackagebtn.addActionListener( browsepackageListener );
        separator1 = new JLabel( new ImageIcon( "res/banners/separator.png" ) );
        nameClassLab = new JLabel( "Name : " );
        nameClassEdit = new JTextField( 30 );
        modeMethodLab = new JLabel(
                "Whitch method stubs would you like to create :" );
        checkMain = new JCheckBox( "public static void main(String [] args)" );
        checkConstructeur = new JCheckBox( "Constuctors from superclass" );
        checkAbstract = new JCheckBox( "Inherited abstract methods ", true );
        separator2 = new JLabel( new ImageIcon( "res/banners/separator.png" ) );
        finishBtn = new JButton( "Finish" );
        finishBtn.addActionListener( finishListener );
        cancelBtn = new JButton( "Cancel" );
        cancelBtn.addActionListener( cancelListener );
        msgError = new JLabel( "Create a new Java class." );
        msgError.setPreferredSize( new Dimension( 150, 20 ) );
        msgError.setFont( new Font( "Serif", Font.BOLD, 16 ) );
        // msgError.setForeground(Color.RED);
        allComponents = new JPanel();

        sourceFolderEdit.addKeyListener( new KeyAdapter() {

            public void keyReleased( KeyEvent e ) {

                if ( ExistSrcFolder( sourceFolderEdit.getText() ) ) {
                    if ( Existpackage( packageEdit.getText() ) ) {
                        if ( !Existclass() ) {
                            disEnaFinishBtn( Existclass() );
                        }else{
                            disEnaBrowseBtn( true );
                            disEnaFinishBtn( true );
                        }

                    } else {
                        disEnaFinishBtn( Existpackage( packageEdit.getText() ) );
                    }

                } else {
                    disEnaBrowseBtn( ExistSrcFolder( sourceFolderEdit.getText() ) );
                    disEnaFinishBtn( ExistSrcFolder( sourceFolderEdit.getText() ) );
                }

            }

        } );
        
        
        packageEdit.addKeyListener( new KeyAdapter() {

            public void keyReleased( KeyEvent e ) {
                if ( ExistSrcFolder( sourceFolderEdit.getText() ) ) {
                    if ( Existpackage( packageEdit.getText() ) ) {
                        if ( !Existclass() ) {
                            disEnaFinishBtn( Existclass() );
                        }else{
                            disEnaBrowseBtn( true );
                            disEnaFinishBtn( true );
                        }

                    } else {
                        disEnaFinishBtn( Existpackage( packageEdit.getText() ) );
                    }

                } else {
                    disEnaBrowseBtn( ExistSrcFolder( sourceFolderEdit.getText() ) );
                    disEnaFinishBtn( ExistSrcFolder( sourceFolderEdit.getText() ) );
                }

            }

        } );

        nameClassEdit.addKeyListener( new KeyAdapter() {

            public void keyReleased( KeyEvent e ) {
                if ( ExistSrcFolder( sourceFolderEdit.getText() ) ) {
                    if ( Existpackage( packageEdit.getText() ) ) {
                        if ( !Existclass() ) {
                            disEnaFinishBtn( Existclass() );
                        }else{
                            disEnaBrowseBtn( true );
                            disEnaFinishBtn( true );
                        }

                    } else {
                        disEnaFinishBtn( Existpackage( packageEdit.getText() ) );
                    }

                } else {
                    disEnaBrowseBtn( ExistSrcFolder( sourceFolderEdit.getText() ) );
                    disEnaFinishBtn( ExistSrcFolder( sourceFolderEdit.getText() ) );
                }

            }

        } );
        GroupLayout grouplayout = new GroupLayout( allComponents );

        grouplayout.setAutoCreateGaps( true );
        grouplayout.setAutoCreateContainerGaps( true );

        allComponents.setLayout( grouplayout );

        // définition de la mise en page
        grouplayout.setHorizontalGroup( grouplayout
                .createParallelGroup()
                .addGroup(
                        grouplayout.createSequentialGroup().addGap( 10 )
                                .addComponent( msgError )

                )

                .addGroup(
                        grouplayout
                                .createSequentialGroup()
                                .addGap( 10 )
                                .addComponent( sourceFolderLab )
                                .addGap( 20 )
                                .addComponent( sourceFolderEdit,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE )
                                .addComponent( browsesourceFolderbtn )

                )
                .addGroup(
                        grouplayout
                                .createSequentialGroup()
                                .addGap( 10 )
                                .addComponent( packageLab )
                                .addGap( 44 )
                                .addComponent( packageEdit,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE )
                                .addComponent( browsepackagebtn )

                )
                .addGroup(
                        grouplayout.createSequentialGroup().addGap( 10 )
                                .addComponent( separator1 )

                )
                .addGroup(
                        grouplayout
                                .createSequentialGroup()
                                .addGap( 10 )
                                .addComponent( nameClassLab )
                                .addGap( 57 )
                                .addComponent( nameClassEdit,
                                        GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.PREFERRED_SIZE )

                )
                .addGroup(
                        grouplayout.createSequentialGroup().addGap( 10 )
                                .addComponent( modeMethodLab )

                )
                .addGroup(
                        grouplayout.createSequentialGroup().addGap( 90 )
                                .addComponent( checkMain )

                )
                .addGroup(
                        grouplayout.createSequentialGroup().addGap( 90 )
                                .addComponent( checkConstructeur )

                )
                .addGroup(
                        grouplayout.createSequentialGroup().addGap( 90 )
                                .addComponent( checkAbstract )

                )

                .addGroup(
                        grouplayout.createSequentialGroup().addGap( 10 )
                                .addComponent( separator2 )

                )
                .addGroup(
                        grouplayout.createSequentialGroup().addGap( 385 )

                                .addComponent( finishBtn ).addGap( 3 )
                                .addComponent( cancelBtn )

                )

                );

        grouplayout.setVerticalGroup( grouplayout
                .createSequentialGroup()
                .addContainerGap()
                .addGap( 10 )
                .addGroup(
                        grouplayout.createParallelGroup()
                                .addComponent( msgError )

                )
                .addGap( 15 )
                .addGroup(
                        grouplayout
                                .createParallelGroup(
                                        GroupLayout.Alignment.LEADING, false )
                                .addComponent( sourceFolderLab )
                                .addComponent( sourceFolderEdit,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE )
                                .addComponent( browsesourceFolderbtn )

                )
                .addGap( 20 )
                .addGroup(
                        grouplayout
                                .createParallelGroup(
                                        GroupLayout.Alignment.LEADING, false )
                                .addComponent( packageLab )
                                .addComponent( packageEdit,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE )
                                .addComponent( browsepackagebtn )

                )
                .addGap( 20 )
                .addGroup(
                        grouplayout.createParallelGroup(
                                GroupLayout.Alignment.LEADING, false )
                                .addComponent( separator1 )

                )
                .addGap( 25 )

                .addGroup(
                        grouplayout
                                .createParallelGroup(
                                        GroupLayout.Alignment.LEADING, false )
                                .addComponent( nameClassLab )
                                .addComponent( nameClassEdit,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE )

                )
                .addGap( 30 )
                .addGroup(
                        grouplayout.createParallelGroup().addComponent(
                                modeMethodLab )

                )
                .addGap( 8 )
                .addGroup(
                        grouplayout.createParallelGroup().addComponent(
                                checkMain )

                )
                .addGap( 3 )
                .addGroup( grouplayout.createParallelGroup()

                        .addComponent( checkConstructeur )

                )
                .addGap( 3 )
                .addGroup( grouplayout.createParallelGroup()

                        .addComponent( checkAbstract )

                )
                .addGap( 20 )
                .addGroup(
                        grouplayout.createParallelGroup().addComponent(
                                separator2 )

                )
                .addGap( 20 )
                .addGroup(
                        grouplayout.createParallelGroup()
                                .addComponent( finishBtn )
                                .addComponent( cancelBtn ) )

                );
        disEnaFinishBtn( false );
        add( javaClassEntete, BorderLayout.NORTH );
        add( allComponents, BorderLayout.CENTER );

        this.setPreferredSize( new Dimension( 538, 485 ) );
        pack();
        setResizable( false );
        setLocationRelativeTo( null );
        setVisible( true );

    }

    public Boolean ExistSrcFolder( String filetocheck ) {

        if ( matchRegex( "\\w{1,30}/\\w{1,30}", filetocheck ) ) {
            File file = new File( Explorer.WORKSPACE + "/" + filetocheck );
            if ( !( file.exists() ) ) {
                msgError.setIcon( new ImageIcon( "res/menu/fermer.png" ) );
                msgError.setText( "Source Folder not exist." );
                return false;

            } else {
                msgError.setIcon( null );
                msgError.setText( "Create a new Java class." );
                return true;
            }
        } else {
            msgError.setIcon( new ImageIcon( "res/menu/fermer.png" ) );
            msgError.setText( "Source Folder not exist." );
            return false;
        }

    }

    public Boolean Existclass() {

        String path = sourceFolderEdit.getText()
                + ( ( "Default".equals( packageEdit.getText() ) || "".equals( packageEdit.getText() ) ) ? ""
                        : ( "/" + packageEdit.getText().replace( "Default", "" ) ) ) + "/" + nameClassEdit.getText()
                + ".java";
        if ( nameClassEdit.getText().isEmpty() ) {
            msgError.setIcon( new ImageIcon( "res/menu/fermer.png" ) );
            msgError.setText( "Type name is empty." );
            return false;
        } else {
            if ( new File( Explorer.WORKSPACE + "/" + path ).exists() ) {
                msgError.setIcon( new ImageIcon( "res/menu/fermer.png" ) );
                msgError.setText( "Type already exists." );
                return false;
            } else {
                msgError.setIcon( null );
                msgError.setText( "Create a new Java class." );
                return true;
            }
        }

    }

    public static Boolean Existpackage( String packageToCheck ) {
        Boolean test = true;
        String[] ch;
        String NameFile = Explorer.WORKSPACE + "/" + getSourceFolderText();
        File file;

        if ( !( nameClassEdit.getText().isEmpty() ) ) {

            if ( matchRegex( "(\\w{1,30})?((\\p{Punct}){1}(\\w{1,30}))*",
                    packageToCheck ) ) {
                ch = packageToCheck.split( "\\p{Punct}" );
                if ( "Default".equals( ch[0] ) || "".equals( ch[0] ) ) {
                    if ( ch.length == 1 ) {
                        msgError.setIcon( null );
                        msgError.setText( "Create a new Java class." );
                        test = true;

                    } else {
                        msgError.setIcon( new ImageIcon( "res/menu/fermer.png" ) );
                        msgError.setText( "Package not exist." );
                        test = false;

                    }

                } else {
                    for ( int i = 0; i < ch.length; i++ ) {
                        NameFile += "/" + ch[i];

                        file = new File( NameFile );
                        if ( !( file.exists() ) ) {
                            msgError.setIcon( new ImageIcon( "res/menu/fermer.png" ) );
                            msgError.setText( "Package not exist." );
                            test = false;
                        }
                    }
                }

            } else {
                msgError.setIcon( new ImageIcon( "res/menu/fermer.png" ) );
                msgError.setText( "Package not exist." );
                test = false;
            }
        } else {
            msgError.setIcon( new ImageIcon( "res/menu/fermer.png" ) );
            msgError.setText( "Type name is empty." );
            test = false;
        }
        return test;

    }

    public static void disEnaFinishBtn( Boolean test ) {
        finishBtn.setEnabled( test );

    }

    public static void setText( String path, String compenentCode ) {
        if ( "package".equals( compenentCode ) ) {
            packageEdit.setText( path );

        } else if ( "srcfolder".equals( compenentCode ) ) {
            sourceFolderEdit.setText( path );

        }
    }

    public void disEnaBrowseBtn( Boolean test ) {

        browsepackagebtn.setEnabled( test );
        if ( !test ) {
            finishBtn.setEnabled( false );
        }

    }

    public static String getSourceFolderText() {

        return sourceFolderEdit.getText();
    }

    public static Boolean matchRegex( String regex, String FileToCheck ) {

        Pattern pattern = Pattern.compile( regex );
        Matcher matcher = pattern.matcher( FileToCheck );
        return matcher.matches();
    }

    public void creerClass() throws Exception {
        // 1
        CreateFileInTree();
        // 2
        addEditToDock( nameClassEdit.getText(), 1, getfullpath() );
        // 3
        addClassNameInBuild();
        // 4 Updating JTree
        Explorer.rafrechir();
        // 5
        TitrePan.SaveAct( (TitrePan) EditeurConfig.getTabbedpane().getTabComponentAt(
                TitrePan.returnTabNbr( getfullpath() ) ) );
    }

    public String getfullpath() {
        String PathName = sourceFolderEdit.getText();
        String[] pack = packageEdit.getText().split( "\\p{Punct}" );
        if ( !( "Default".equals( pack[0] ) || pack[0].isEmpty() ) ) {

            for ( int i = 0; i < pack.length; i++ ) {
                PathName += "/" + pack[i];
            }
        }
        PathName += "/" + nameClassEdit.getText() + ".java";
        return PathName;
    }

    public void CreateFileInTree() {

        File file = new File( Explorer.WORKSPACE + "/" + getfullpath() );
        try {
            file.createNewFile();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public void addClassNameInBuild() throws Exception {
        String fichier = sourceFolderEdit.getText().split( "/" )[0];
        String packs[] = packageEdit.getText().split( "\\p{Punct}" );
        if ( packs[0].isEmpty() || "Default".equals( packs[0] ) ) {
            Jdom.CreateClass( fichier, "src", nameClassEdit.getText() + ".java", 0 );
        } else {
            Jdom.CreateClass( fichier, packageEdit.getText(), nameClassEdit.getText() + ".java", 1 );
        }

    }

    public static void addEditToDock( String Title, int etat, String path ) {

        EditeurContent editeur = new EditeurContent( Title );
        if ( etat == 1 ) {

            if ( checkAbstract.isSelected() && checkConstructeur.isSelected()
                    && checkMain.isSelected() ) {
                editeur.addTextToEditeur( 3 );
            } else if ( ( checkAbstract.isSelected() && checkConstructeur
                    .isSelected() ) || checkConstructeur.isSelected() ) {
                editeur.addTextToEditeur( 2 );
            } else if ( ( checkAbstract.isSelected() && checkMain.isSelected() )
                    || checkMain.isSelected() ) {
                editeur.addTextToEditeur( 4 );
            } else if ( checkAbstract.isSelected() ) {
                editeur.addTextToEditeur( 1 );

            }

            EditeurConfig.AddTab( editeur, Title + ".java", path );

        } else if ( etat == 2 ) {
            if ( editeur.addTextToEditeur( 5 ) ) {
                EditeurConfig.AddTab( editeur, Title, path );
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
        rootPane.registerKeyboardAction( actionListener, stroke,
                JComponent.WHEN_IN_FOCUSED_WINDOW );
        return rootPane;
    }

}
