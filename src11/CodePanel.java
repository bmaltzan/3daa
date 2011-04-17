import java.awt.*;
import java.awt.event.*;

/**
 *  CodePanel.java
 *
 *  creates a canvas for drawing code on.
 *  supports font sizes, and a scrollbar.
 *  reads code from
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public class CodePanel extends Panel{
   class CodeWindow extends Canvas{
      public CodeWindow(){}
      public void update(Graphics g){ 
         Dimension d= getSize();
         if ( (buffer_g==null)
           || (d.width != buffer_d.width)
           || (d.height != buffer_d.height) ) {
            buffer_d= d;
            buffer_i= createImage(d.width,d.height);
            buffer_g= buffer_i.getGraphics();
            buffer_g.setColor(Color.white);
            buffer_g.fillRect(0,0, d.width,d.height);
            buffer_g.setFont(new Font(font_name, font_style, font_size));               
            fm= g.getFontMetrics();
            buffer_g.setColor(Color.black);
            buffer_g.drawString(s, d.width/2-fm.stringWidth(s)/2,
                     d.height/2-fm.getHeight()/2 + fm.getAscent());
            drawing_setup= false;
         }
         buffer_g.setColor(Color.black);
         buffer_g.drawRect(0,0,getSize().width-1,getSize().height-1);
         g.drawImage(buffer_i, 0,0, this);
      }
      public void paint(Graphics g){ update(g); }
      public Insets getInsets(){ return new Insets(2,2,2,2); }
   }

   private CodeWindow code_window= new CodeWindow();
   private Scrollbar code_bar= new Scrollbar(Scrollbar.VERTICAL, 0,1,0,1);

   private Image     buffer_i;
   private Graphics  buffer_g;
   private Dimension buffer_d;
   private int i=0,j=0; // general-use counter
   private String s= "code panel", font_name= "Monospaced";
   private int font_style= Font.PLAIN, font_size=14;
   private FontMetrics fm;

   private final int MAXLINES=20;
   private int[] line_pos= new int[MAXLINES];
   private int num_of_lines=0, left_margin=10;
   private int line_selected=0, line_at_top=0;
   private boolean drawing_setup=false;

   private Color color_line_selected= new Color(255,0,0);
   private Color color_plain_text= new Color(0,0,0);
   private String tmp_s;
   private Sim sim;

   /**
    *  adds code canvas and scrollbar.
    */
   public CodePanel(Sim s){
      sim=s;
      setLayout(new BorderLayout());
      add("Center", code_window);
      add("East", code_bar);
      code_bar.addAdjustmentListener(
         new AdjustmentListener(){
            public void adjustmentValueChanged(AdjustmentEvent e) {
               sim.draw_text(sim.get_state().get_msg_code_scrollbar(e.getValue()));
               draw_scrolling(e.getValue());
               code_window.repaint();
      }  }  );
      set_scrollbar_range(sim.get_alg_source().get_lines_of_code());
   }

   /**
    *  gets the current font metrics.
    *  sets up an array of offsets for drawing the lines of code.
    */
   public void setup_for_drawing(){
      fm= buffer_g.getFontMetrics();
      int font_height= fm.getHeight();
      int canvas_height= buffer_d.height;
      for(i=font_height, num_of_lines=0; ((i<canvas_height)&&(num_of_lines<MAXLINES));
          i+=font_height, num_of_lines++){
         line_pos[num_of_lines]= i;
      }
      drawing_setup= true;
      code_bar.setBlockIncrement(num_of_lines-1);  
   }

   /** 
    *  check if code to be displayed is already on the screen.
    *  if so, redraw old line if still on screen, then redraw
    *    new line highlighted.
    *  else redraw canvas with selected line at top.
    */
   public void draw_code(int line_num){
      if((line_at_top<=line_num)&&(line_num<line_at_top+num_of_lines)&&(drawing_setup)){
         if((line_at_top<=line_selected)&&(line_selected<line_at_top+num_of_lines)){
            buffer_g.setColor(color_plain_text);
            buffer_g.drawString(sim.get_alg_source().get_code(line_selected),left_margin,
                                line_pos[line_selected-line_at_top]);
         }
         buffer_g.setColor(color_line_selected);
         buffer_g.drawString(sim.get_alg_source().get_code(line_num),
                  left_margin,line_pos[line_num-line_at_top]);
         line_selected= line_num;
         code_window.repaint();
      }else{ // redraw whole screen
         line_selected= line_num;
         draw_scrolling(line_num);
      }
      code_bar.setValue(line_at_top);
   }

   /**
    *  displays code with line selected at top of code canvas. 
    */
   public void draw_scrolling(int line_num){
      if(!drawing_setup){ setup_for_drawing(); }
      erase_code_window();
      buffer_g.setColor(color_plain_text);
      if(line_num<0) return; // error check, dont try to get code from array index<0
      for(i=line_num, j=0; ((i<line_num+num_of_lines)&&(i<sim.get_alg_source().get_lines_of_code()));
            i++, j++){
         tmp_s= sim.get_alg_source().get_code(i);
         if(i==line_selected){
            buffer_g.setColor(color_line_selected);
            buffer_g.drawString(tmp_s, left_margin,line_pos[j]);
            buffer_g.setColor(color_plain_text);
         }else{
            buffer_g.drawString(tmp_s, left_margin,line_pos[j]);
      }  }
      line_at_top=line_num;
      code_window.repaint();
   }

   /**
    *  erases the code window.
    */
   public void erase_code_window(){
      buffer_g.setColor(Color.white);
      buffer_g.fillRect(0,0, buffer_d.width,buffer_d.height);
      buffer_g.setColor(Color.black);
      buffer_g.drawRect(0,0,buffer_d.width,buffer_d.height);
   }

   /**
    *  sets the font size of the code window to the size specified.
    *  calls set_for_drawing.
    *  calls draw_scrolling.
    */
   public void set_font_size(int size){
      font_size= size;
      if(buffer_g!=null){
         buffer_g.setFont(new Font(font_name, font_style, font_size));
         setup_for_drawing();
         line_at_top=-100;
         draw_scrolling(line_selected);
   }  }

   public void paint(Graphics g){
      g.drawRect(0,0,getSize().width-1,getSize().height-1);
   }

   /**
    *  set the scrollbar from 0 to lines.
    */
   public void set_scrollbar_range(int lines){
      int ten_percent= (int) (lines*.2);
      code_bar.setValues(0, ten_percent, 0, lines+ten_percent );
   }

   /**
    *  erase canvas, set scrollbar to 0.
    */
   public void reset(){
      if(buffer_g!=null){
         line_selected=0; line_at_top=0;
         code_bar.setValue(0);
         draw_scrolling(0);
         buffer_g.setColor(Color.white);
         buffer_g.fillRect(0,0, buffer_d.width,buffer_d.height);
         repaint();
      }
   }

   public void update(Graphics g){ paint(g); }
   public Insets getInsets(){ return new Insets(3,3,3,3); }
   public Dimension getPreferredSize(){ return new Dimension(590,150); }
}

