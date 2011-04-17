import java.awt.*;
import java.awt.event.*;

/**
 *  RaytracerDisplayCanvas1.java
 *
 *  adds mouse clicking to the left canvas.
 *  used to specify the point to animate a rendering.
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public class RaytracerDisplayCanvas1 extends DisplayCanvas implements MouseListener{
   protected Point mouse_click= new Point(0,0);

   public RaytracerDisplayCanvas1(Sim s, String initial_string, int display_instance){
      super(s,initial_string,display_instance);
      addMouseListener(this);
   }

   /**
    *  return last (x,y) point on canvas clicked.
    */
   public Point get_mouse_click(){ return mouse_click; }

   public void mousePressed(MouseEvent e) {
      mouse_click.x= e.getX(); mouse_click.y= e.getY();
      sim.draw_text("point for recording: "+ mouse_click.x+ ","+ mouse_click.y+ ")" );
   }
   public void mouseReleased(MouseEvent e){};
   public void mouseClicked(MouseEvent e){};
   public void mouseEntered(MouseEvent e){};
   public void mouseExited(MouseEvent e){};
}

