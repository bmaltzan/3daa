import java.awt.*;
import java.awt.event.*;

/**
 *  SetupColorPanel.java
 *
 *  controls modifying an object's color. 
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public class SetupColorPanel extends Panel{

   class AtribPanel extends Panel{
      class TextPanel extends Panel{
         public TextPanel(){
            setLayout(new GridLayout(3,1));
            c_text.setEditable(false);
            r_text.setEditable(false);
            t_text.setEditable(false);
            add(c_text); add(r_text); add(t_text);
      }  }
      class BarPanel extends Panel{
         public BarPanel(){
            setLayout(new GridLayout(3,1));
            c_bar= new Scrollbar(Scrollbar.HORIZONTAL, (int)atrib.x, 50, MIN,MAX+50);
            r_bar= new Scrollbar(Scrollbar.HORIZONTAL, (int)atrib.y, 50, MIN2,MAX2+50);
            t_bar= new Scrollbar(Scrollbar.HORIZONTAL, (int)atrib.z, 50, MIN2,MAX2+50);
            add(c_bar); add(r_bar); add(t_bar);

            c_bar.addAdjustmentListener(
               new AdjustmentListener(){
                  public void adjustmentValueChanged(AdjustmentEvent e){
                     atrib.x= e.getValue()/10.0;
                     c_text.setText("coef= "+atrib.x);
                     rtsb.draw_scene();
            }  }  );
            r_bar.addAdjustmentListener(
               new AdjustmentListener(){
                  public void adjustmentValueChanged(AdjustmentEvent e){
                     atrib.y= e.getValue()/100.0;
                     r_text.setText("refl= "+atrib.y);
                     rtsb.draw_scene();
            }  }  );
            t_bar.addAdjustmentListener(
               new AdjustmentListener(){
                  public void adjustmentValueChanged(AdjustmentEvent e){
                     atrib.z= e.getValue()/100.0;
                     t_text.setText("tran= "+atrib.z);
                     rtsb.draw_scene();
            }  }  );
         }
      }      

      private final int MIN=0, MAX=500,
                        MIN2=0, MAX2=100;
      private T3d atrib;
      private TextField c_text= new TextField("0",10),
                        r_text= new TextField("0",10),
                        t_text= new TextField("0",10);
      private BarPanel bar_panel;
      private TextPanel text_panel;
      private Scrollbar c_bar, r_bar, t_bar;
      private Label name;

      public AtribPanel(T3d a){
         atrib= a;
         bar_panel= new BarPanel();
         text_panel= new TextPanel();
         set_values();
         setLayout(new GridLayout(1,2));
         add(bar_panel);
         add(text_panel);
      }
      public void setAtrib(T3d a){ atrib=a; set_values(); }
      private void set_values(){ 
         c_bar.setValue((int)(atrib.x*10)); 
         r_bar.setValue((int)(atrib.y*100)); 
         t_bar.setValue((int)(atrib.z*100)); 
         c_text.setText("coef= "+atrib.x);
         r_text.setText("refl= "+atrib.y);
         t_text.setText("tran= "+atrib.z);
      }
      public Insets getInsets(){ return new Insets(3,3,3,3); }
      public void paint(Graphics g){ 
         bar_panel.repaint(); text_panel.repaint(); //name.repaint();
         g.drawRect(0,0,getSize().width-1,getSize().height-1); 
      }
   }

   class ColorPanel extends Panel{
      class ColorCanvas extends Canvas{
         public ColorCanvas(){}
         public void paint(Graphics g){ update(g); }
         public void update(Graphics g){ 
            g.setColor(new Color((int)color.r, (int)color.g, (int)color.b));
            g.fillRect(0,0, getSize().width-1,getSize().height-1);
         }
      }
      class BarPanel extends Panel{
         public BarPanel(String s){
            setLayout(new GridLayout(4,1));
            Label name= new Label(s);
            r_bar= new Scrollbar(Scrollbar.HORIZONTAL, (int)color.r, 50, MIN,MAX+50);
            g_bar= new Scrollbar(Scrollbar.HORIZONTAL, (int)color.g, 50, MIN,MAX+50);
            b_bar= new Scrollbar(Scrollbar.HORIZONTAL, (int)color.b, 50, MIN,MAX+50);
            add(name); add(r_bar); add(g_bar); add(b_bar);

            r_bar.addAdjustmentListener(
               new AdjustmentListener(){
                  public void adjustmentValueChanged(AdjustmentEvent e){
                     color.r= e.getValue();
                     pic.repaint();
            }  }  );
            g_bar.addAdjustmentListener(
               new AdjustmentListener(){
                  public void adjustmentValueChanged(AdjustmentEvent e){
                     color.g= e.getValue();
                     pic.repaint();
            }  }  );
            b_bar.addAdjustmentListener(
               new AdjustmentListener(){
                  public void adjustmentValueChanged(AdjustmentEvent e){
                     color.b= e.getValue();
                     pic.repaint();
            }  }  );
         }
      }      

      private final int MIN=0, MAX=255;
      private TColor color;
      private ColorCanvas pic;
      private BarPanel bar_panel;
      private Scrollbar r_bar, g_bar, b_bar;

      public ColorPanel(TColor c, String s){
         color=c;
         pic= new ColorCanvas();
         bar_panel= new BarPanel(s);
         set_values();
         setLayout(new GridLayout(1,2));
         add(bar_panel);
         add(pic);
      }
      public void setColor(TColor c){ 
         color=c;
         removeAll(); 
         set_values(); 
         add(bar_panel);
         add(pic);
      }
      private void set_values(){
         r_bar.setValue((int)color.r); 
         g_bar.setValue((int)color.g); 
         b_bar.setValue((int)color.b); 
         pic.repaint();
      }
      public void paint(Graphics g){
         pic.repaint(); bar_panel.repaint(); //name.invalidate(); name.repaint();
         g.drawRect(0,0,getSize().width-1,getSize().height-1); 
      }
      public Insets getInsets(){ return new Insets(3,3,3,3); }
   }

   private RaytracerSetupButtons rtsb;
   private ColorPanel ap, dp, sp;
   private AtribPanel cap;

   public SetupColorPanel(RaytracerSetupButtons r, 
                          TColor a, TColor d, TColor s, T3d ca){
      rtsb=r;
      ap= new ColorPanel(a, "ambient");
      dp= new ColorPanel(d, "diffuse");
      sp= new ColorPanel(s, "specular");
      cap= new AtribPanel(ca);
      set_values(); 
      setLayout(new GridLayout(2,2));
      add(ap); add(sp); add(dp);
      add(cap);
   }

   public void setColor(TColor a, TColor d, TColor s, T3d ca){
      removeAll();
      ap.setColor(a); 
      dp.setColor(d); 
      sp.setColor(s); 
      cap.setAtrib(ca);
      set_values(); 
      add(ap); add(sp); add(dp);
      add(cap);
   }

   public void set_values(){ 
      ap.set_values();
      sp.set_values();
      dp.set_values();
      cap.set_values();
   }

   public void paint(Graphics g){
      ap.repaint(); dp.repaint(); sp.repaint(); cap.repaint();
      g.drawRect(0,0,getSize().width-1,getSize().height-1); 
   }
   public Insets getInsets(){ return new Insets(3,3,3,3); }
   public Dimension getPreferredSize(){ return new Dimension(340,185); }
}


