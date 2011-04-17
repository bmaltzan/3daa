import java.awt.*;
import java.awt.event.*;

/**
 *  RaytracerButtonPanel.java
 *
 *  changes the execute button to say "render"
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public class RaytracerButtonPanel extends ButtonPanel{

   class ColorCanvas extends Canvas{
      private Color fill_color;
      public ColorCanvas(Color c){   fill_color=c; }
      public void setColor(Color c){ fill_color=c; repaint(); }
      public void paint(Graphics g){ update(g); }
      public void update(Graphics g){ 
         g.setColor(fill_color);
         g.fillRect(0,0, getSize().width-1,getSize().height-1);
      }
      public Dimension getPreferredSize(){ return new Dimension(5,10); }
   }

   private ColorCanvas refl=   new ColorCanvas(Color.green);
   private ColorCanvas tran=   new ColorCanvas(Color.cyan);
   private ColorCanvas eyeray= new ColorCanvas(Color.red);
   private ColorCanvas light=  new ColorCanvas(Color.yellow);
   
   public RaytracerButtonPanel(Sim s){
      super(s);
      execute_button.setLabel("render");
      add(new Label(""));
      add(new Label("eyeray")); add(eyeray);
      add(new Label("refl"));   add(refl);
      add(new Label("tran"));   add(tran);
      add(new Label("lt ray")); add(light);
   }
   public void paint(Graphics g){
      g.drawRect(0,0,getSize().width-1,getSize().height-1);
   }
   
   public void setRefl(Color c){ refl.setColor(c); }
   
   public void reset(){}
   public Insets getInsets(){ return new Insets(3,3,3,3); }
   public Dimension getPreferredSize(){ return new Dimension(70,250); }

}


