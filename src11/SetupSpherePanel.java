import java.awt.*;
import java.awt.event.*;

/**
 *  SetupSpherePanel.java
 *
 *  controls modifying a sphere. 
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public class SetupSpherePanel extends Panel{

   class PointPanel extends Panel{
      class TextPanel extends Panel{
         public TextPanel(){
            setLayout(new GridLayout(3,1));
            x_text.setEditable(false);
            y_text.setEditable(false);
            z_text.setEditable(false);
            add(x_text); add(y_text); add(z_text);
      }  }
      class BarPanel extends Panel{
         public BarPanel(String s){
            setLayout(new GridLayout(4,1));
            Label name= new Label(s);
            x_bar= new Scrollbar(Scrollbar.HORIZONTAL, (int)sphere.center.x, 50, MIN,MAX+50);
            y_bar= new Scrollbar(Scrollbar.HORIZONTAL, (int)sphere.center.y, 50, MIN,MAX+50);
            z_bar= new Scrollbar(Scrollbar.HORIZONTAL, (int)sphere.center.z, 50, MIN,MAX+50);
            add(name); add(x_bar); add(y_bar); add(z_bar);

            x_bar.addAdjustmentListener(
               new AdjustmentListener(){
                  public void adjustmentValueChanged(AdjustmentEvent e){
                     sphere.center.x= e.getValue();
                     x_text.setText("x= "+(int)sphere.center.x);
                     sphere.make_sphere();
                     rtsb.draw_scene();
            }  }  );
            y_bar.addAdjustmentListener(
               new AdjustmentListener(){
                  public void adjustmentValueChanged(AdjustmentEvent e){
                     sphere.center.y= e.getValue();
                     y_text.setText("y= "+(int)sphere.center.y);
                     sphere.make_sphere();
                     rtsb.draw_scene();
            }  }  );
            z_bar.addAdjustmentListener(
               new AdjustmentListener(){
                  public void adjustmentValueChanged(AdjustmentEvent e){
                     sphere.center.z= e.getValue();
                     z_text.setText("z= "+(int)sphere.center.z);
                     sphere.make_sphere();
                     rtsb.draw_scene();
            }  }  );
         }
      }      

      private final int MIN=-400, MAX=400;
      private TextField x_text= new TextField("0",10),
                        y_text= new TextField("0",10),
                        z_text= new TextField("0",10);
      private BarPanel bar_panel;
      private TextPanel text_panel;
      private Scrollbar x_bar, y_bar, z_bar;

      public PointPanel(String s){
         bar_panel= new BarPanel(s);
         text_panel= new TextPanel();
         set_values();
         setLayout(new GridLayout(1,2));
         add(bar_panel);
         add(text_panel);
      }
      //public void setPoint(T3d p){ point=p; set_values(); }
      private void set_values(){ 
         x_bar.setValue((int)sphere.center.x); 
         y_bar.setValue((int)sphere.center.y); 
         z_bar.setValue((int)sphere.center.z); 
         x_text.setText("x= "+(int)sphere.center.x);
         y_text.setText("y= "+(int)sphere.center.y);
         z_text.setText("z= "+(int)sphere.center.z);
      }
      public Insets getInsets(){ return new Insets(3,3,3,3); }
      public void paint(Graphics g){
         bar_panel.repaint(); text_panel.repaint(); //name.repaint();
         g.drawRect(0,0,getSize().width-1,getSize().height-1); 
      }
   }

   class RadiusPanel extends Panel{
      class TextPanel extends Panel{
         public TextPanel(){
            setLayout(new GridLayout(1,1));
            r_text.setEditable(false);
            add(r_text);
      }  }
      class BarPanel extends Panel{
         public BarPanel(String s){
            setLayout(new GridLayout(4,1));
            r_bar= new Scrollbar(Scrollbar.HORIZONTAL, (int)sphere.radius, 50, MIN,MAX+50);
            Label name= new Label(s);
            add(name); add(r_bar);
            r_bar.addAdjustmentListener(
               new AdjustmentListener(){
                  public void adjustmentValueChanged(AdjustmentEvent e){
                     sphere.radius= e.getValue();
                     r_text.setText("radius= "+(int)sphere.radius);
                     sphere.make_sphere();
                     rtsb.draw_scene();
            }  }  );
         }
      }      

      private final int MIN=0, MAX=400;
      private TextField r_text= new TextField("0",10);
      private BarPanel bar_panel;
      private TextPanel text_panel;
      private Scrollbar r_bar;

      public RadiusPanel(String s){
         bar_panel= new BarPanel(s);
         text_panel= new TextPanel();
         set_values();
         setLayout(new GridLayout(1,2));
         add(bar_panel);
         add(text_panel);
      }
      //public void setWidth(){ set_values(); }
      private void set_values(){ 
         r_bar.setValue((int)sphere.radius); 
         r_text.setText("radius= "+(int)sphere.radius);
      }
      public Insets getInsets(){ return new Insets(3,3,3,3); }
      public void paint(Graphics g){
         bar_panel.repaint(); text_panel.repaint(); //name.repaint();
         g.drawRect(0,0,getSize().width-1,getSize().height-1); 
      }
   }

   private RaytracerSetupButtons rtsb;
   private PointPanel panel1;
   private RadiusPanel panel2;
   private RaytracerDFESphere sphere;
   
   public SetupSpherePanel(RaytracerSetupButtons r, RaytracerDFESphere s){
      rtsb= r; sphere= s;
      panel1= new PointPanel("center");
      panel2= new RadiusPanel("radius");
      set_values(); 
      setLayout(new GridLayout(2,1));
      add(panel1); add(panel2);
   }

   public void setSphere(RaytracerDFESphere s){
      sphere= s;
      removeAll();
      panel1= new PointPanel("center");
      panel2= new RadiusPanel("width");
      set_values();
      add(panel1); add(panel2); 
   }

   private void set_values(){ 
      panel1.set_values();
      panel2.set_values();
   }

   public void paint(Graphics g){
      panel1.repaint(); panel2.repaint();
      g.drawRect(0,0,getSize().width-1,getSize().height-1); 
   }
   public Insets getInsets(){ return new Insets(3,3,3,3); }
   public Dimension getPreferredSize(){ return new Dimension(220,185); }
}


