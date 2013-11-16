package yas.menu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import yas.explorer.Explorer;

@SuppressWarnings("serial")
public class ImportProject extends JDialog implements ActionListener {

	JLabel import_header,import_label;
	JTextField projfolder;
	JButton browse, ok, cancel;
	JPanel pane;
	File dir = new File("");

	public ImportProject(Frame frame, String title, boolean modal) {
		super(frame, title, modal);
		pane = new JPanel();
		import_header = new JLabel(
				new ImageIcon("res/banners/ImportEntete.jpg"));
		import_label = new JLabel("Project Folder :");
		projfolder = new JTextField(8);
		browse = new JButton("Browse...");
		ok = new JButton("OK");
		// ok.setEnabled(false);
		cancel = new JButton("Cancel");

		// projfolder.addCaretListener(new CaretListener() {
		//
		// @Override
		// public void caretUpdate(CaretEvent arg0) {
		// verify();
		// }
		// });
		// projfolder.addKeyListener( new KeyAdapter() {
		// @Override
		// public void keyReleased(KeyEvent e) {
		// verify();
		// }
		// });

		browse.addActionListener(this);
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
								.addComponent(import_label)
								.addGap(10)
								.addComponent(projfolder,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE).addGap(5)
								.addComponent(browse))
				.addGroup(
						grouplayout.createSequentialGroup().addGap(400)
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
								.addComponent(import_label)
								.addComponent(projfolder,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(browse))
				.addGap(200)
				.addGroup(
						grouplayout.createParallelGroup().addComponent(ok)
								.addComponent(cancel)));

		add(import_header, BorderLayout.NORTH);
		add(pane, BorderLayout.CENTER);

		setPreferredSize(new Dimension(538, 380));
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Browse...":
			final JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int response = fc.showOpenDialog(this);
			if (response == JFileChooser.APPROVE_OPTION) {
				projfolder.setText(fc.getSelectedFile().getAbsolutePath());
			}
			break;
		case "OK":
			// test if a project with the same name already exist in workspace :
			// error, else :
			// copy folder in workspace
			dir = new File(projfolder.getText());
			if (dir.exists()) {
				if (Explorer.ifExisteBuild(dir)) {
					File destination = new File(Explorer.WORKSPACE + "/"
							+ dir.getName());
					
					if (destination.exists()) {
						JOptionPane
								.showMessageDialog(
										this,
										"A project with the same name already exists in Workspace !\nPlease Choose a different one.",
										"Error",
										JOptionPane.INFORMATION_MESSAGE);
					} else {

						copyFolder(dir, destination);
						try {
							Explorer.rafrechir();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						dispose();
					}
				} else {
					showError("Invalid project folder !");
				}
			} else {
				showError("Invalid folder path !");
			}
			break;
		case "Cancel":
			dispose();
			break;
		}
	}

	private void showError(String string) {
		JOptionPane.showMessageDialog(this, string, "Error",
				JOptionPane.INFORMATION_MESSAGE);
	}

	private void copyFolder(File src, File dest) {
		if (src.isDirectory()) {

			// if directory not exists, create it
			if (!dest.exists()) {
				dest.mkdir();
				System.out.println("Directory copied from " + src + "  to "
						+ dest);
			}

			// list all the directory contents
			String files[] = src.list();

			for (String file : files) {
				// construct the src and dest file structure
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// recursive copy
				copyFolder(srcFile, destFile);
			}

		} else {
			// if file, then copy it
			// Use bytes stream to support all file types
			InputStream in;
			try {
				in = new FileInputStream(src);

				OutputStream out = new FileOutputStream(dest);

				byte[] buffer = new byte[1024];

				int length;
				// copy the file content in bytes
				while ((length = in.read(buffer)) > 0) {
					out.write(buffer, 0, length);
				}

				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("File copied from " + src + " to " + dest);
		}

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
	
}
