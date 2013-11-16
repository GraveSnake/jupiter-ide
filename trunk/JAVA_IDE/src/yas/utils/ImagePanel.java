package yas.utils;

import java.awt.*;
import javax.swing.*;


@SuppressWarnings("serial")
public class ImagePanel extends JPanel {

	  protected Image img;

	  public ImagePanel(Image img) {
	    this.img = img;
	    Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	    setLayout(null);
	    setOpaque(false);
	   
	  }
	  
	  public void paintComponent(Graphics g){

		  g.drawImage(img, 0, 0, this.getWidth(),this.getHeight(), null);
		  
	  }

	  

}