package yas.utils;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;


public class JTextAreaOutputStream extends OutputStream {
   private JTextArea m_textArea = null;

   public JTextAreaOutputStream(JTextArea aTextArea) {
      m_textArea = aTextArea;
   }

   public void write(int b) throws IOException {
      byte[] bytes = new byte[1];
      bytes[0] = (byte)b;
      String newText = new String(bytes);
      m_textArea.append(newText);
      
      if (newText.indexOf('\n') > -1) {
         try {
            m_textArea.scrollRectToVisible(m_textArea.modelToView(
                        m_textArea.getDocument().getLength()));
         } catch (javax.swing.text.BadLocationException err) {
            err.printStackTrace();
         }
      }
   }

   public final void write(byte[] arg0) throws IOException {
      String txt = new String(arg0);
      m_textArea.append(txt);
      try {
         m_textArea.scrollRectToVisible(m_textArea.modelToView(m_textArea.getDocument().getLength()));
      } catch (javax.swing.text.BadLocationException err) {
         err.printStackTrace();
      }
   }
}
