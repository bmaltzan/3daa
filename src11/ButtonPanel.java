import java.awt.*;
import java.awt.event.*;

/**
 *  ButtonPanel.java
 *
 *  realestate on the destop for placing (additional) algorithm 
 *  specific buttons.
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public class ButtonPanel extends Panel{
   protected Button execute_button, reset_button, record_button, auto_button;
   protected Sim sim;
   protected boolean executing=false, executing2=false;
   
   public ButtonPanel(Sim s){
      sim=s;
      execute_button= new Button("execute");
      record_button= new Button("rec");
      reset_button= new Button("reset");
      //auto_button= new Button("auto play");
      add(execute_button); add(record_button); add(reset_button); //add(auto_button);
      
      reset_button.addActionListener(
         new ActionListener(){
            public void actionPerformed(ActionEvent e){
               sim.draw_text(sim.get_state().get_msg_reset());
               sim.reset();
      }  }  );
      record_button.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               sim.draw_text(sim.get_state().get_msg_start_recording());
               sim.set_recording(sim.get_algorithm().record(
                     ((RaytracerDisplayCanvas1)(sim.get_display1_canvas())).get_mouse_click()));
               sim.draw_text(sim.get_state().get_msg_end_recording());
               if(! ((sim.get_state()).get_scene_recorded()) ){
                  sim.draw_text(sim.get_state().get_msg_start_scene_recording());
                  sim.set_setup_recording(sim.get_algorithm().record_scene());
                  sim.get_state().set_scene_recorded(true);
                  sim.draw_text(sim.get_state().get_msg_end_scene_recording());
               }
               sim.get_controller().enableControls();
      }  }  );
      execute_button.addActionListener(
         new ActionListener(){
            public void actionPerformed(ActionEvent e){
               if(!executing){ //only allow 1 execution at a time
                  executing= true; //not thread safe, but close enough...
                  new Thread(){
                     public void run(){
                        sim.draw_text(sim.get_state().get_msg_execute());
                        sim.get_algorithm().execute();
                        executing= false;
                  }  }.start();
               }
      }  }  );
      /*auto_button.addActionListener(
         new ActionListener(){
            public void actionPerformed(ActionEvent e){
               if(!executing){ //only allow 1 execution at a time
                  executing2= true; //not thread safe, but close enough...
                  new Thread(){
                     public void run(){
                        sim.draw_text(sim.get_state().get_msg_execute());
                        sim.get_algorithm().execute();
                        executing2= false;
                  }  }.start();
               }
      }  }  );*/
   }
   public void paint(Graphics g){
      g.drawRect(0,0,getSize().width-1,getSize().height-1);
   }
   public void reset(){}
   public Insets getInsets(){ return new Insets(3,3,3,3); }
   public Dimension getPreferredSize(){ return new Dimension(70,250); }
}
