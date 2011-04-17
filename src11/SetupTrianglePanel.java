import java.awt.*;
import java.awt.event.*;

/**
 *  SetupTrianglePanel.java
 *
 *  controls modifying a triangle. 
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public class SetupTrianglePanel extends Panel{

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
            x_bar= new Scrollbar(Scrollbar.HORIZONTAL, (int)point.x, 50, MIN,MAX+50);
            y_bar= new Scrollbar(Scrollbar.HORIZONTAL, (int)point.y, 50, MIN,MAX+50);
            z_bar= new Scrollbar(Scrollbar.HORIZONTAL, (int)point.z, 50, MIN,MAX+50);
            add(name); add(x_bar); add(y_bar); add(z_bar);

            x_bar.addAdjustmentListener(
               new AdjustmentListener(){
                  public void adjustmentValueChanged(AdjustmentEvent e){
                     point.x= e.getValue();
                     x_text.setText("x= "+(int)point.x);
                     rtsb.draw_scene();
            }  }  );
            y_bar.addAdjustmentListener(
               new AdjustmentListener(){
                  public void adjustmentValueChanged(AdjustmentEvent e){
                     point.y= e.getValue();
                     y_text.setText("y= "+(int)point.y);
                     rtsb.draw_scene();
            }  }  );
            z_bar.addAdjustmentListener(
               new AdjustmentListener(){
                  public void adjustmentValueChanged(AdjustmentEvent e){
                     point.z= e.getValue();
                     z_text.setText("z= "+(int)point.z);
                     rtsb.draw_scene();
            }  }  );
         }
      }      

      private final int MIN=-400, MAX=400;
      private T3d point;
      private TextField x_text= new TextField("0",10),
                        y_text= new TextField("0",10),
                        z_text= new TextField("0",10);
      private BarPanel bar_panel;
      private TextPanel text_panel;
      private Scrollbar x_bar, y_bar, z_bar;

      public PointPanel(T3d p, String s){
         point= p;
         bar_panel= new BarPanel(s);
         text_panel= new TextPanel();
         set_values();
         setLayout(new GridLayout(1,2));
         add(bar_panel);
         add(text_panel);
      }
      public void setPoint(T3d p){ point=p; set_values(); }
      private void set_values(){ 
         x_bar.setValue((int)point.x); 
         y_bar.setValue((int)point.y); 
         z_bar.setValue((int)point.z); 
         x_text.setText("x= "+(int)point.x);
         y_text.setText("y= "+(int)point.y);
         z_text.setText("z= "+(int)point.z);
      }
      public Insets getInsets(){ return new Insets(3,3,3,3); }
      public void paint(Graphics g){
         bar_panel.repaint(); text_panel.repaint(); //name.repaint();
         g.drawRect(0,0,getSize().width-1,getSize().height-1); 
      }
   }

   private RaytracerSetupButtons rtsb;
   private PointPanel panel1, panel2, panel3;

   public SetupTrianglePanel(RaytracerSetupButtons r, T3d p1, T3d p2, T3d p3){
      rtsb=r;
      removeAll();
      panel1= new PointPanel(p1, "point1");
      panel2= new PointPanel(p2, "point2");
      panel3= new PointPanel(p3, "point3");
      set_values(); 
      setLayout(new GridLayout(3,1));
      add(panel1); add(panel2); add(panel3);
   }

   public void setTriangle(T3d p1, T3d p2, T3d p3){
      removeAll();
      panel1= new PointPanel(p1, "point1");
      panel2= new PointPanel(p2, "point2");
      panel3= new PointPanel(p3, "point3");
      add(panel1); add(panel2); add(panel3);
      set_values(); 
   }

   private void set_values(){ 
      panel1.set_values();
      panel2.set_values();
      panel3.set_values();
   }

   public void paint(Graphics g){
      panel1.repaint(); panel2.repaint(); panel3.repaint(); 
      g.drawRect(0,0,getSize().width-1,getSize().height-1); 
   }
   public Insets getInsets(){ return new Insets(3,3,3,3); }
   public Dimension getPreferredSize(){ return new Dimension(220,185); }
}


