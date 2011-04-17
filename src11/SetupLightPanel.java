import java.awt.*;
import java.awt.event.*;

/**
 *  SetupLightPanel.java
 *
 *  controls modifying a TLight. 
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public class SetupLightPanel extends Panel{

   class TextPanel extends Panel{
      public TextPanel(){
         setLayout(new GridLayout(4,1));
         x_text.setEditable(false);
         y_text.setEditable(false);
         z_text.setEditable(false);
         b_text.setEditable(false);
         add(x_text); add(y_text); add(z_text); add(b_text);
   }  }

   class BarPanel extends Panel{
      public BarPanel(){
         setLayout(new GridLayout(5,1));
         Label name= new Label("light point");
         x_bar= new Scrollbar(Scrollbar.HORIZONTAL, (int)light.x, 50, MIN,MAX+50);
         y_bar= new Scrollbar(Scrollbar.HORIZONTAL, (int)light.y, 50, MIN,MAX+50);
         z_bar= new Scrollbar(Scrollbar.HORIZONTAL, (int)light.z, 50, MIN,MAX+50);
         b_bar= new Scrollbar(Scrollbar.HORIZONTAL, (int)light.bright, 50, MINB,MAXB+50);
         add(name); add(x_bar); add(y_bar); add(z_bar); add(b_bar);

         x_bar.addAdjustmentListener(
            new AdjustmentListener(){
               public void adjustmentValueChanged(AdjustmentEvent e){
                  light.x= e.getValue();
                  x_text.setText("x= "+(int)light.x);
                  rtsb.draw_scene();
         }  }  );
         y_bar.addAdjustmentListener(
            new AdjustmentListener(){
               public void adjustmentValueChanged(AdjustmentEvent e){
                  light.y= e.getValue();
                  y_text.setText("y= "+(int)light.y);
                  rtsb.draw_scene();
         }  }  );                                                         
         z_bar.addAdjustmentListener(
            new AdjustmentListener(){
               public void adjustmentValueChanged(AdjustmentEvent e){
                  light.z= e.getValue();
                  z_text.setText("z= "+(int)light.z);
                  rtsb.draw_scene();
         }  }  );
         b_bar.addAdjustmentListener(
            new AdjustmentListener(){
               public void adjustmentValueChanged(AdjustmentEvent e){
                  light.bright= e.getValue()/100.0;
                  b_text.setText("lum= "+light.bright);
         }  }  );
      }
      public void paint(Graphics g){ g.drawRect(0,0,getSize().width-1,getSize().height-1); }
   }
   
   private final int MIN=-400, MAX=400,
                     MINB=0,   MAXB=100;
   private TextField x_text= new TextField("0",10),
                     y_text= new TextField("0",10),
                     z_text= new TextField("0",10),
                     b_text= new TextField("0",10);
   private Scrollbar x_bar, y_bar, z_bar, b_bar;

   private TextPanel text_panel;
   private BarPanel bar_panel;
   private TLight light;
   private RaytracerSetupButtons rtsb;

   public SetupLightPanel(TLight l, RaytracerSetupButtons r){
      light=l; rtsb=r;
      text_panel= new TextPanel();
      bar_panel= new BarPanel();
      set_values();
      setLayout(new GridLayout(1,2));
      add(bar_panel);
      add(text_panel);
   }

   public void setLight(TLight l){ 
      light=l;
      removeAll(); 
      set_values();
      add(bar_panel);
      add(text_panel);
   }

   private void set_values(){ 
      x_bar.setValue((int)light.x); 
      y_bar.setValue((int)light.y); 
      z_bar.setValue((int)light.z); 
      b_bar.setValue((int)(light.bright*100)); 
      x_text.setText("x= "+(int)light.x);
      y_text.setText("y= "+(int)light.y);
      z_text.setText("z= "+(int)light.z);
      b_text.setText("lum= "+light.bright);
   }
   
   public void paint(Graphics g){
      text_panel.repaint(); bar_panel.repaint();
      g.drawRect(0,0,getSize().width-1,getSize().height-1); 
   }
   public Insets getInsets(){ return new Insets(3,3,3,3); }
   public Dimension getPreferredSize(){ return new Dimension(300,120); }
}


