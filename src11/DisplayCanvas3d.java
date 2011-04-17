import java.awt.*;
import javax.media.j3d.*;

/**
 *  DisplayCanvas.java
 *
 *  a double buffered canvas.
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public class DisplayCanvas3d extends Canvas3d{
   protected int instance;
   protected String initial_string;
   protected Sim sim;

   public DisplayCanvas3d(Sim s, String init_string, int display_instance){
      sim=s;
      initial_string=init_string;
      instance= display_instance;
   }
   public void paint(Graphics g){ update(g); }
   public void update(Graphics g){ 
      Dimension d= getSize();
      if ( (buffer_g==null)
        || (d.width != buffer_d.width)
        || (d.height != buffer_d.height) ) {
         buffer_d= d;
         buffer_i= createImage(d.width,d.height);
         buffer_g= buffer_i.getGraphics();
         buffer_g.setColor(Color.black);
         buffer_g.fillRect(0,0, d.width,d.height);
         FontMetrics fm= g.getFontMetrics();
         buffer_g.setColor(Color.white);
         buffer_g.drawString(initial_string, d.width/2-fm.stringWidth(initial_string)/2,
                   d.height/2-fm.getHeight()/2 + fm.getAscent());
      }
      g.drawImage(buffer_i, 0,0, this);
   }

   /**
    *  erases canvas.
    */
   public void reset(){
      if(buffer_g!=null){
         buffer_g.setColor(Color.black);
         buffer_g.fillRect(0,0, buffer_d.width,buffer_d.height);
         repaint();
      }
   }

   public Dimension getPreferredSize(){ return new Dimension(250,250); }
   public Graphics get_graphics(){ return buffer_g; }
}

