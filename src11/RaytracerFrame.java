import java.awt.*;

/**
 *  RaytracerFrame.java
 *
 *  a raytracer animation frame.
 *  
 *  since most of the scene does not change,
 *  adds a pointer to a single <code>DisplayFrame</code>
 *  that holds the unchanging data.
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public class RaytracerFrame extends AlgFrame{
   private DisplayFrame static_frame;
   private T3d view_pt;
   private double view_d;

   /**
    *  create one frame of a recording.
    *
    *  @param c line of code to display in code window.
    *  @param s string to display in textfield.
    *  @param f2 image to display in display2 canvas.
    *  @param sf static image to display in display2 canvas.
    *  @param camera_pt camera point.
    *  @param d2 view distance for display 2.
    */
   public RaytracerFrame(int c, String s, DisplayFrame f2, DisplayFrame sf,
                         T3d camera_pt, double d){
      super(c,s,null,f2);

      static_frame=sf;
      view_pt= (T3d)camera_pt.clone();
      view_d=d;
   }

   /**
    *  return data that is static in the scene for the wireframe.
    *    each frame needing this data drawn points to the same structure.
    */
   public DisplayFrame get_static_frame2(){ return static_frame; }

   /**
    *  get view position.
    */
   public T3d get_view_pt(){ return view_pt; }

   /**
    *  get viewing distance.
    */
   public double get_view_d(){ return view_d; }

   public String toString(){
      return super.toString()+
         "\tvpt:"+ view_pt+
         "\tvd:"+ view_d;
   }
}

