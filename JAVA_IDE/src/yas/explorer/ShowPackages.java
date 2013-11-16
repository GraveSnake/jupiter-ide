package yas.explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class ShowPackages extends JPanel implements TreeSelectionListener {

	private static final long serialVersionUID = 1L;
	JTree jtree;
	DefaultMutableTreeNode root;
	static String chemin;
	JLabel ChooseLab;
	static JLabel checkout;
	JPanel allCompenent, explorer;
	int sem = 0;
	String SF = "";
	File file;

	public ShowPackages() {
		ChooseLab = new JLabel("Choose a Folder :");
		checkout = new JLabel(" ");
		checkout.setForeground(Color.red);
		allCompenent = new JPanel();
		explorer = new JPanel();
		explorer.setLayout(new BorderLayout());
		root = new DefaultMutableTreeNode();

		SF = CreateClass.getSourceFolderText();
		file = new File(Explorer.WORKSPACE + "/" + SF.split("/")[0] + "/"
				+ SF.split("/")[1]);

		lister(file, root);

		jtree = new JTree(root);
		explorer.add(new JScrollPane(jtree), BorderLayout.CENTER);
		explorer.setPreferredSize(new Dimension(220, 200));
		jtree.addTreeSelectionListener(this);

		GroupLayout grouplayout = new GroupLayout(allCompenent);
		allCompenent.setLayout(grouplayout);

		grouplayout.setHorizontalGroup(grouplayout
				.createParallelGroup()
				.addComponent(ChooseLab)
				.addComponent(explorer)
				.addGap(6)
				.addComponent(checkout)
		);

		grouplayout.setVerticalGroup(grouplayout
				.createSequentialGroup()
				.addContainerGap()
				.addComponent(ChooseLab)
				.addGap(15)
				.addComponent(explorer)
				.addGap(10)
				.addComponent(checkout)

		);
		add(allCompenent, BorderLayout.CENTER);
	}

	public void lister(File fileEnCour, DefaultMutableTreeNode childNode) {
		DefaultMutableTreeNode dir = null;
		if (sem == 0) {
			dir = new DefaultMutableTreeNode("Default");
			childNode.add(dir);
			sem = 1;

		}

		for (File f : fileEnCour.listFiles()) {
			if (f.isDirectory()) {

				dir = new DefaultMutableTreeNode(f.getName());
				childNode.add(dir);
				lister(f, dir);

			}
		}

	}

	public static String getChemin(String chemin) {
		String[] ch = chemin.substring(1, chemin.length() - 1).split(",");
		String ch1 = "";
		for (int i = 1; i < ch.length; i++) {
			ch1 += ch[i].substring(1);
			ch1 += ".";
		}
		if (!("".equals(ch1))) {
			ch1 = ch1.substring(0, ch1.length() - 1);
		}
		return ch1;

	}

	@Override
	public void valueChanged(TreeSelectionEvent event) {
		chemin = event.getPath().toString();
		if ("[]".equals(chemin)) {
			checkout.setText("Invalid package folder");
		} else {
			checkout.setText("");
		}
	}

}
