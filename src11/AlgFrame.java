/**
 *  AlgFrame.java
 *
 *  holds data for one frame of animation.
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public class AlgFrame{

   private int current_code_line;
   private String text;
   private DisplayFrame display1, display2;

   /**
    *  create one frame of a recording.
    *
    *  @param c line of code to display in code window
    *  @param s string to display in textfield
    *  @param f1 image to display in display1 canvas
    *  @param f2 image to display in display2 canvas
    */
   public AlgFrame(int c, String s, DisplayFrame f1, DisplayFrame f2){ 
      current_code_line=c;
      text=s;
      display1=f1; display2=f2;
   }

   /**
    *  return line of code this animation frame describes.
    */
   public int get_code_line(){ return current_code_line; }

   /**
    *  return text to display.
    */
   public String get_text(){ return text; }

   /**
    *  get data for left display frame.
    */
   public DisplayFrame get_display_frame1(){ return display1; }

   /**
    * get data for right display frame.
    */
   public DisplayFrame get_display_frame2(){ return display2; }

   public String toString(){ 
      return "alg frame: "+ current_code_line+ 
             "\ttext: "+ text+
             "\td1:"+ display1+ 
             "\td2:"+ display2;
   }
}

