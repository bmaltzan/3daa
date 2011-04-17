import java.awt.*;
import java.awt.event.*;

/**
 *  SetupViewPanel.java
 *
 *  controls modifying a sphere. 
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public class SetupViewPanel extends Panel{

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
            x_bar= new Scrollbar(Scrollbar.HORIZONTAL, (int)view.camera.x, 50, MIN,MAX+50);
            y_bar= new Scrollbar(Scrollbar.HORIZONTAL, (int)view.camera.y, 50, MIN,MAX+50);
            z_bar= new Scrollbar(Scrollbar.HORIZONTAL, (int)view.camera.z, 50, MIN,MAX+50);
            add(name); add(x_bar); add(y_bar); add(z_bar);

            x_bar.addAdjustmentListener(
               new AdjustmentListener(){
                  public void adjustmentValueChanged(AdjustmentEvent e){
                     view.camera.x= e.getValue()*100;
                     x_text.setText(""+(int)view.camera.x/100);
                     rtsb.draw_scene();
            }  }  );
            y_bar.addAdjustmentListener(
               new AdjustmentListener(){
                  public void adjustmentValueChanged(AdjustmentEvent e){
                     view.camera.y= e.getValue()*100;
                     y_text.setText(""+(int)view.camera.y/100);
                     rtsb.draw_scene();
            }  }  );
            z_bar.addAdjustmentListener(
               new AdjustmentListener(){
                  public void adjustmentValueChanged(AdjustmentEvent e){
                     view.camera.z= e.getValue()*100;
                     if(view.camera.z==0)view.camera.z=100;
                     z_text.setText(""+(int)view.camera.z/100);
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
         x_bar.setValue((int)view.camera.x/100); 
         y_bar.setValue((int)view.camera.y/100); 
         z_bar.setValue((int)view.camera.z/100); 
         x_text.setText(""+(int)view.camera.x/100);
         y_text.setText(""+(int)view.camera.y/100);
         z_text.setText(""+(int)view.camera.z/100);
      }
      public Insets getInsets(){ return new Insets(3,3,3,3); }
      public void paint(Graphics g){
         bar_panel.repaint(); text_panel.repaint(); //name.repaint();
         g.drawRect(0,0,getSize().width-1,getSize().height-1); 
      }
   }

   class DPanel extends Panel{
      class TextPanel extends Panel{
         public TextPanel(){
            setLayout(new GridLayout(1,1));
            d_text.setEditable(false);
            add(d_text);
      }  }
      class BarPanel extends Panel{
         public BarPanel(String s){
            setLayout(new GridLayout(3,1));
            d_bar= new Scrollbar(Scrollbar.HORIZONTAL, (int)view.d_percent, 50, MIN,MAX+50);
            Label name= new Label(s);
            Label name2= new Label("(does not affect rendering)");
            add(name); add(d_bar); add(name2);
            
            d_bar.addAdjustmentListener(
               new AdjustmentListener(){
                  public void adjustmentValueChanged(AdjustmentEvent e){
                     view.setPercent(e.getValue()/100.0);
                     d_text.setText("d%= "+view.d_percent);
                     rtsb.draw_scene();
            }  }  );
         }
      }      

      private final int MIN=0, MAX=100;
      private TextField d_text= new TextField("0",10);
      private BarPanel bar_panel;
      private TextPanel text_panel;
      private Scrollbar d_bar;

      public DPanel(String s){
         bar_panel= new BarPanel(s);
         text_panel= new TextPanel();
         set_values();
         setLayout(new GridLayout(1,2));
         add(bar_panel);
         add(text_panel);
      }

      private void set_values(){ 
         d_bar.setValue((int)(view.d_percent*100.0)); 
         d_text.setText("d%= "+view.d_percent);
      }
      public Insets getInsets(){ return new Insets(3,3,3,3); }
      public void paint(Graphics g){
         bar_panel.repaint(); text_panel.repaint(); //name.repaint();
         g.drawRect(0,0,getSize().width-1,getSize().height-1); 
      }
   }

   private RaytracerSetupButtons rtsb;
   private PointPanel panel1;
   private DPanel panel2;
   private RaytracerDFEView view;
   
   public SetupViewPanel(RaytracerSetupButtons r, RaytracerDFEView v){
      rtsb= r; view= v;
      panel1= new PointPanel("camera location");
      panel2= new DPanel("viewing 'd'%");
      set_values(); 
      setLayout(new GridLayout(2,1));
      add(panel1); add(panel2);
   }

   public void setView(RaytracerDFEView v){
      view= v;
      removeAll();
      panel1= new PointPanel("camera location");
      panel2= new DPanel("viewing 'd'%");
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
   public Dimension getPreferredSize(){ return new Dimension(400,180); }
}


