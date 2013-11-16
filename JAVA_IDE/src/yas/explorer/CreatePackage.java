package yas.explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;


import yas.main.Main;
import yas.utils.Jdom;

@SuppressWarnings("serial")
public class CreatePackage extends JDialog implements ActionListener {
	
	JLabel pack_header, pack_label, packName_label;
	JTextField parentPackage, packName_edit;
	JButton browse, ok, cancel;
	JPanel pane;
	File dir = new File("");

	public CreatePackage (String src) {
		super(Main.getInstance(), "New Package", true);
		pane = new JPanel();
		pack_header = new JLabel(
				new ImageIcon("res/banners/createpackage.jpg"));
		pack_label = new JLabel("Create Package under :");
		parentPackage = new JTextField(src);
		packName_edit = new JTextField(25);
		browse = new JButton("Browse...");
		packName_label = new JLabel("Package Name :");
		ok = new JButton("OK");
		ok.setEnabled(false);
		cancel = new JButton("Cancel");

        final ActionListener browsepackageListener = new ActionListener() {
            public void actionPerformed( ActionEvent event ) {
            	int response = JOptionPane.showConfirmDialog(Main.getInstance(), new BrowseAll(), "Folder Selection", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            	if(response==JOptionPane.OK_OPTION)
            	{
            		if(BrowseAll.checkout.getText().isEmpty()){
            			parentPackage.setText( BrowseAll.getChemin() );
           			 	ok.setEnabled(Verify(parentPackage.getText()));
            	}else{
        			browse.doClick();
        		}
            	}
            }
        };
        packName_edit.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyReleased(KeyEvent e) {
        		ok.setEnabled(Checkout(packName_edit.getText()));
        	}

			private boolean Checkout(String text) {
				if(text.isEmpty()){
					return false;
				}else{
					return true;
				}
			}
		});
		 parentPackage.addKeyListener( new KeyAdapter() {
		 @Override
		 public void keyReleased(KeyEvent e) {
			 ok.setEnabled(Verify(parentPackage.getText()));
		 }
		 });

		browse.addActionListener(browsepackageListener);
		ok.addActionListener(this);
		cancel.addActionListener(this);

		GroupLayout grouplayout = new GroupLayout(pane);
		pane.setLayout(grouplayout);
		grouplayout.setAutoCreateGaps(true);
		grouplayout.setAutoCreateContainerGaps(true);

		grouplayout.setHorizontalGroup(grouplayout
				.createParallelGroup()
				.addGroup(
						grouplayout
								.createSequentialGroup()
								.addGap(20)
								.addComponent(pack_label)
								.addGap(10)
								.addComponent(parentPackage,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addGap(5)
								.addComponent(browse))
				.addGroup(grouplayout.createSequentialGroup()
						.addGap(20)
						.addComponent(packName_label)
						.addGap(48)
						.addComponent(packName_edit, GroupLayout.DEFAULT_SIZE,
								GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						)
				.addGroup(
						grouplayout.createSequentialGroup()
								.addGap(400)
								.addComponent(ok).addGap(10)
								.addComponent(cancel)));

		grouplayout.setVerticalGroup(grouplayout
				.createSequentialGroup()
				.addContainerGap()
				.addGap(20)
				.addGroup(
						grouplayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING, false)
								.addComponent(pack_label)
								.addComponent(parentPackage,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(browse))
				.addGap(30)
				.addGroup(grouplayout.createParallelGroup()
								.addComponent(packName_label)
								.addComponent(packName_edit, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
						)
				.addGap(100)
				.addGroup(
						grouplayout.createParallelGroup()
								.addComponent(ok)
								.addComponent(cancel)));

		add(pack_header, BorderLayout.NORTH);
		add(pane, BorderLayout.CENTER);

		setPreferredSize(new Dimension(538, 340));
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private boolean Verify(String filetocheck) {
		File file = new File( Explorer.WORKSPACE + "/" + filetocheck );
        if ( file.exists() && !(packName_edit.getText().isEmpty()) ) {
            return true;
        } else {
            return false;
        }
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "OK":
			File file = new File( Explorer.WORKSPACE + "/" + parentPackage.getText() );
			int same = 0;
			for(File f : file.listFiles())
			{
				if(f.isDirectory()){
					if(f.getName().equals(packName_edit.getText())){
						same = 1;
						JOptionPane.showMessageDialog(Main.getInstance(), "A package with the same name already exists !", "Warning", JOptionPane.ERROR_MESSAGE);
						break;
					}
				}
			}
			if(same==0){
				CreatePack();
				dispose();
			}
			break;
		case "Cancel":
			dispose();
			break;
		}
	}


	private void CreatePack() {
		//1
		CreateFile();
		//2
		try {
			AddToXml();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		//3
        try {
			Explorer.rafrechir();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void AddToXml() throws Exception {
    	String parents[] = parentPackage.getText().split( "/" );
    	if(parents.length<3){
    		Jdom.CreatePackage(parents[0] ,parents, packName_edit.getText(), 0);
    	}else{
    		Jdom.CreatePackage(parents[0] ,parents, packName_edit.getText(), 1);
    	}
    	

	}

	private void CreateFile() {
		new File(Explorer.WORKSPACE+"/"+parentPackage.getText()+"/"+packName_edit.getText()).mkdir();
	}

	protected JRootPane createRootPane() {
	    ActionListener actionListener = new ActionListener() {
	      public void actionPerformed(ActionEvent actionEvent) {
	        setVisible(false);
	      }
	    };
	    JRootPane rootPane = new JRootPane();
	    KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
	    rootPane.registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
	    return rootPane;
	  }

	
	static class BrowseAll extends JPanel implements TreeSelectionListener{
		JLabel 				   ChooseLab;
		static JLabel checkout;
		JPanel                 allComponents, explorer;
	    DefaultMutableTreeNode root;
	    JTree                  tree;
	    static String                 chemin;
		public BrowseAll(){
			ChooseLab = new JLabel( "Choose a souce folder :" );
	        checkout = new JLabel(" ");
	        checkout.setForeground(Color.red);
	        allComponents = new JPanel();
	        explorer = new JPanel();
	        explorer.setLayout( new BorderLayout() );
	        browse();
	        tree = new JTree( root );
	        explorer.add( new JScrollPane( tree ), BorderLayout.CENTER );
	        explorer.setPreferredSize(new Dimension(250, 300));
	        tree.addTreeSelectionListener( this );
	        
	        GroupLayout grouplayout = new GroupLayout( allComponents );
	        allComponents.setLayout( grouplayout );

	        grouplayout.setHorizontalGroup(
	                grouplayout.createParallelGroup()
	                        .addComponent( ChooseLab )
	                        .addComponent( explorer )
	                        .addGroup( grouplayout.createSequentialGroup()
	                                .addGap( 6 )
	                                .addComponent( checkout )
	                        )

	                );

	        grouplayout.setVerticalGroup(
	                grouplayout.createSequentialGroup()
	                        .addContainerGap()
	                        .addGap( 10 )
	                        .addComponent( ChooseLab )
	                        .addGap( 15 )
	                        .addComponent( explorer )
	                        .addGap( 6 )
	                        .addGroup( grouplayout.createParallelGroup( GroupLayout.Alignment.LEADING, false )
	                                .addComponent( checkout )
	                        )
	                        .addGap(10)

	                );
	        add( allComponents, BorderLayout.CENTER );
		}
		
	    public void browse() {
	        File file = new File( Explorer.WORKSPACE );
	        root = new DefaultMutableTreeNode();

	        for ( File f : file.listFiles() )
	        {
	            if ( f.isDirectory() ) {
	                if ( Explorer.ifExisteBuild( f ) ) {
	                    DefaultMutableTreeNode dir = new DefaultMutableTreeNode( f.getName() );
	                    for(File subfile : f.listFiles())
	                    {
	                    	if(subfile.isDirectory() && subfile.getName().equals("src"))
	                    	{
	    	                    DefaultMutableTreeNode srcdir = new DefaultMutableTreeNode( subfile.getName() );
	    	                    dir.add( srcdir );
	    	                    root.add( dir );
	    	                    lister(subfile, srcdir);
	                    	}
	                    }
	                }
	            }
	        }
	    }
	    public void lister(File Currentfile, DefaultMutableTreeNode childNode){
			for (File f : Currentfile.listFiles()) {
				if (f.isDirectory()) {
					DefaultMutableTreeNode dir = new DefaultMutableTreeNode(f.getName());
					childNode.add(dir);
					lister(f, dir);
				}
			}
	    }

		@Override
		public void valueChanged(TreeSelectionEvent event) {
	        chemin = AdjustPath((event.getPath().toString()));
	        if(TestisOK( chemin)){checkout.setText("");}
	    	else{checkout.setText("Invalid folder");}
		}
	    public Boolean TestisOK( String path ) {

	        Pattern pattern = Pattern.compile( "\\w{1,30}(/\\w{1,30})+" );
	        Matcher matcher = pattern.matcher( path );
	        if ( matcher.matches() )
	        {
	            return true;
	        } else {
	            return false;

	        }

	    }
	    public String AdjustPath(String chemin) {
	        String[] ch = chemin.substring( 1, chemin.length() - 1 ).split( "," );
	        String ch1 = "";
	        for ( int i = 1; i < ch.length; i++ ) {
	            ch1 += ch[i].substring( 1 );
	            ch1 += "/";
	        }
	        if ( !( "".equals( ch1 ) ) ) {
	            ch1 = ch1.substring( 0, ch1.length() - 1 );
	        }
	        return ch1;

	    }
		public static String getChemin() {
			return chemin;
		}
	}
	
}
