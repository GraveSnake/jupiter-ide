package yas.utils;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class MonButton extends JButton {

    private static final long serialVersionUID = 1L;
    private String            pathIcon         = "";

    public MonButton( String pathIcon ) {
        this.pathIcon = pathIcon;
        setOpaque( false );
        setContentAreaFilled(false);
        setPreferredSize(new Dimension(20, 20));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public void paint( Graphics g ) {
        super.paint( g );
        g.drawImage( new ImageIcon(pathIcon).getImage(), 0, 3, null );
    }

}
