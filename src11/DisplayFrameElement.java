import java.awt.*;

/**
 *  DisplayFrameElement.java
 *
 *  generic element to be drawn on a canvas.
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public abstract class DisplayFrameElement {
   public T3d[] tmp_world;
   public int size=0;
   public Color color;
   
   public DisplayFrameElement(){}
   public DisplayFrameElement(Color c){ color=c; }
   
   /**
    *  return world coordinates of the element's wireframe points.
    */   
   public final T3d[] get_world_points(){ return tmp_world; }

   /**
    *  return world coordinates of the element's wireframe points.
    */   
   public final int get_num_points(){ return size; }

   /**
    *  draw screen points on canvas.
    */
   public abstract void draw(Graphics g, Point[] p);
   
   public String toString(){
      StringBuffer s= new StringBuffer(100);
      for(int i=0; i<size; i++){
         s.append("i="+ i+ "\t"+ tmp_world[i].toString()+ "\n");
      }
      return "DisplayFrameElement:33\n"+
             s.toString();
   }
}

