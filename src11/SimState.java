import java.awt.*;

/**
 *  SimState.java
 *
 *  loads default messages and components for an algorithm
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public abstract class SimState{
   protected Sim sim;
   public SimState(Sim s){ sim=s; }

   // current card selected: pref,setup,sim
   protected int current_card;
   public int get_current_card(){ return current_card; }
   public void set_current_card(int i){ 
      current_card=i;
      sim.get_controller().set_stop();
      //sim.get_control_thread().set_alg_state(AlgControlThread.STOP, 0);
   }

   // determine if scene needs to be recorded again
   protected boolean scene_recorded=false;
   public void set_scene_recorded(boolean b){ scene_recorded=b; }
   public boolean get_scene_recorded(){ return scene_recorded; }

   // 
   public abstract Algorithm get_algorithm();
   public AlgSource get_alg_source(){ return null; }

   public       String get_alg_about_text(){ return "no algorithm text provided"; }
   public final String get_sim_about_text(){
      return "sim webpage:\n   www.bigfoot.com/~brian.maltzan/sim\n\n"+
             "comments to:\n   brian.maltzan@usa.net\n\n"+
             "vcr-like controller by eileen head:\n   www.cs.binghamton.edu/~tools\n\n";
   }

   public DisplayCanvas get_display1_canvas(){ return new DisplayCanvas(sim,"display 1", 1); }
   public DisplayCanvas get_display2_canvas(){ return new DisplayCanvas(sim,"display 2", 2); }
   public ButtonPanel get_button_panel(){ return new ButtonPanel(sim); }
   //public Controller get_controller(){ return new Controller(sim.get_control_thread(),); }
   public CodePanel get_code_panel(){ return new CodePanel(sim); }

   public DisplayCanvas get_display_canvas(){ return new DisplayCanvas(sim,"setup display", 1); }
   public SetupButtonPanel get_setup_button_panel(SetupElementsPanel sep){
      return new SetupButtonPanel(sim, sep);
   }
   public SetupElementsPanel get_setup_elements_panel(){ return new SetupElementsPanel(sim); }

   public String get_msg_alg_selected(){ return null; }
   public String get_msg_reset(){ return null; }
   public String get_msg_start_recording(){ return null; }
   public String get_msg_end_recording(){ return null; }
   public String get_msg_start_scene_recording(){ return null; }
   public String get_msg_end_scene_recording(){ return null; }
   public String get_msg_execute(){ return null; }
   public String get_msg_code_scrollbar(int i){ return null; }
   public String get_msg_mouse_pressed(int x, int y){ return null; }
   public String get_msg_pref_button(){ return null; }
   public String get_msg_setup_button(){ return null; }
   public String get_msg_sim_button(){ return null; }
   public String get_msg_first_tim_in_sim(){ return null; }
   public String get_msg_help(){ return null; }
   public String get_msg_ta_help(){ return null; }
}



