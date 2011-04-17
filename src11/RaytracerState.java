import java.awt.*;

/**
 *  RaytracerState.java
 *
 *  overrides component choices for sim card panel.
 *  overrides some messages.
 *  adds help and about messages.
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public class RaytracerState extends SimState{
   public RaytracerState(Sim s){ super(s); }

   public Algorithm get_algorithm(){ return new Raytracer(sim); }
   public AlgSource get_alg_source(){ return new RaytracerSource(); }
   public DisplayCanvas get_display1_canvas(){ 
      return new RaytracerDisplayCanvas1(sim,"display 1", 1); 
   }
   public DisplayCanvas get_display2_canvas(){ 
      return new DisplayCanvasWireFrame(sim,"display 2", 2); 
   }
   public ButtonPanel get_button_panel(){ 
      return new RaytracerButtonPanel(sim); 
   }

   public DisplayCanvas get_display_canvas(){ 
      return new DisplayCanvasWireFrame(sim,"setup display", 1); 
   }
   public SetupButtonPanel get_setup_button_panel(SetupElementsPanel sep){ 
      return new RaytracerSetupButtons(sim,sep); 
   }
   public SetupElementsPanel get_setup_elements_panel(){ 
      return new SetupElementsPanel(sim); 
   }

   public String get_msg_mouse_pressed(int x, int y){ return "point for recording= ("+ x+ ","+ y+ ")"; }
   public String get_msg_code_scrollbar(int i){ return "code_line: "+i; }
   public String get_msg_start_recording(){ return "recording.."; }
   public String get_msg_end_recording(){ return "recording....done"; }
   public String get_msg_start_scene_recording(){ return "recording scene.."; }
   public String get_msg_end_scene_recording(){ return "recording scene....done"; }

   public String get_alg_about_text(){
      return "a simple raytracer\n\n"+
             "ported from a c version by roman kuchuda,\n"+
             "\"an introduction to ray tracing\"\n";
   }

   public String get_msg_ta_help(){
      return "A Simple Raytracer\n\n"+
             "WireFrame Vector Colors\n"+
             "  red- vector from the viewpoint into the scene\n"+
             "  green- vector reflected off a surface\n"+
             "  cyan- vector transmitted through a surface\n"+
             "  yellow- light sources, shadow feelers"+
             "  blue- projection plane\n\n"+
             "Sim Panel:\n"+
             "  render- trace the scene, and display the image \n\tin the first canvas.\n"+
             "  reset- clear all windows, reset all buttons.\n"+
             "  rec- record one ray being traced. select the ray \n\tby clicking on a point inside the first canvas.\n"+
             "  stop- stop the animation in the right canvas.\n"+
             "  step>- step one iteration forward in the simulation.\n"+
             "  <setp- step one iteration backward in the simulation.\n"+
             "  '.'- control speed of continuous animation\n"+
             "  clicking in the left window will select a point \n\tfor recording\n"+
             "\n\n"+
             "Setup:\n"+
             "  Selecting a scene will load a default set of objects, lights, and properties.\n"+
             "  View- controls the viewpoint\n"+
             "  Lighting- controls adding, removing, and modifing light sources\n"+
             "  Objects- controls adding, removing and modifying spheres, boxes, and triangles\n"+
             "  Save- sends current scene to the raytracer algorithm\n";
   }

}

