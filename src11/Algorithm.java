import java.awt.*;

/**
 *  Algorithm.java
 *
 *  a generic algorithm structure.
 *
 *  @author  Brian Maltzan
 *  @version 98.11.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public abstract class Algorithm{
   protected Sim sim;
   public Algorithm(Sim s){ sim=s; }

   /**
    *  calls the algorithm to demonstrate it's execution
    *    without any simulation recording.
    */
   public abstract void execute();

   /**
    *  record a sequence from the algorithm to allow
    *  forward, reverse, and steping.
    */
   public AlgTape record(Point p){ return null; }

   /**
    *  record a sequence from the algorithm to allow
    *  forward, reverse, and steping.
    */
   public AlgTape record(){ return null; }

   /**
    *  record the setup section of an algorithm. designed to
    *  allow input to the algorithm from the setup panel
    */
   public AlgTape record_scene(Point p){ return null; }

   /**
    *  record the setup section of an algorithm. designed to
    *  allow input to the algorithm from the setup panel
    */
   public AlgTape record_scene(){ return null; }

   /**
    *  add code from setup changes.
    */
   public void setup_code(){}

   /**
    *  draw one frame of the recording.
    *  @param frame frame to be drawn
    */
   public void draw_frame(AlgFrame frame){}
/*      DisplayFrameElement dfe;
      if(frame==null){}
      else{
         sim.draw_text(frame.get_text());
         int code_line= frame.get_code_line();
         if(code_line>=0){ sim.get_code_panel().draw_code(code_line); }

         Graphics tmp_g;
         DisplayFrame display1= frame.get_display1();
         if(display1!=null){
            tmp_g= sim.get_display1_canvas().get_graphics();
            dfe= display1.next();
            while(dfe!=null){
               dfe.draw(tmp_g);
               dfe= display1.next();
            }
            sim.get_display1_canvas().repaint();
         }

         DisplayFrame display2= frame.get_display2();
         if(display2!=null){
            tmp_g= sim.get_display2_canvas().get_graphics();
            dfe= display2.next();
            while(dfe!=null){
               dfe.draw(tmp_g);
               dfe= display2.next();
            }
            sim.get_display2_canvas().repaint();
         }
   }  }
*/  

}





