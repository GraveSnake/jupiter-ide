package yas.output;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import yas.explorer.Explorer;
import yas.utils.JTextAreaOutputStream;

public class Output extends JPanel  {

    private static final long serialVersionUID = 1L;
    static JTextArea                  textarea;
    JTextAreaOutputStream outStream ;
    static StringBuffer sb;
    public Output() {
        
        textarea = new JTextArea( 8, 100 );
        textarea.setEditable( false );
        outStream = new JTextAreaOutputStream(textarea); 
        System.setOut(new PrintStream(outStream));
        System.setErr(new PrintStream(outStream) );
        
        setLayout( new BorderLayout() );
        this.add( new JScrollPane( textarea ), BorderLayout.CENTER );
        this.setMinimumSize( new Dimension( 300, 100 ) );
        this.setPreferredSize( new Dimension( 300, 100 ) );
    }

  
   public static void setFileLogContent(String projetctame){
       File file = new File(Explorer.WORKSPACE+"/"+projetctame+"/logs.log");
       if(file.exists()){
           sb =  getTextFile(Explorer.WORKSPACE+"/"+projetctame+"/logs.log");
           textarea.setText( sb.toString() );
       }else{
           System.out.println(Explorer.WORKSPACE+"/"+projetctame+"/logs.log");

       }
   }
   
   
   @SuppressWarnings( "resource" )
   public static StringBuffer getTextFile( String  path ) {
       StringBuffer st = new StringBuffer();
       BufferedReader br;
       String s;
       int t = 0;
       try {
           br = new BufferedReader( new FileReader( new File(path) ) );

           while ( ( s = br.readLine() ) != null ) {
               if ( t > 0 ) {
                   st.append( "\n" );
               }
               st.append( s );
               t++;
           }

       } catch ( FileNotFoundException e ) {
           e.printStackTrace();
       } catch ( IOException e ) {
           e.printStackTrace();
       }
       return st;

   }
   public static void clear(){
       textarea.setText( "" );
   }
   
}
