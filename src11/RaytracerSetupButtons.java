import java.awt.*;
import java.awt.event.*;

/**
 *  RaytracerSetupButtons.java
 *
 *  realestate on the destop for placing setup control buttons. 
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public class RaytracerSetupButtons extends SetupButtonPanel{
   private final int OBJECT=20;
   private final int LIGHT=8;
   private DisplayFrameElement[] objects= new DisplayFrameElement[OBJECT];
   private TColor[] objects_ambiant= new TColor[OBJECT],
                  objects_diffuse= new TColor[OBJECT],
                  objects_specular= new TColor[OBJECT];
   private T3d objects_color_attrib[]= new T3d[OBJECT]; //stores coef,refl,tran
   
   private TLight[] lights= new TLight[LIGHT];
   private int num_lights=0, num_objects=0,
               num_lights_counter=0,
               tri_counter=0, box_counter=0, sph_counter=0;

   private RaytracerDFEView view= new RaytracerDFEView();
   //private T3d view_pt= new T3d(480,85,70);
   //private double[] view_d_percent= {.5};
   //private double[] view_d= {2400};

   private SetupLightPanel light_panel;
   private SetupColorPanel color_panel;
   private SetupTrianglePanel tri_panel;
   private SetupBoxPanel box_panel;
   private SetupSpherePanel sph_panel;
   private SetupViewPanel view_panel;

   private Button viewing_button, save_button, del_light_button, del_obj_button;
   private Choice scene_choice= new Choice(),
                  light_choice= new Choice(), object_choice= new Choice();

   private RaytracerSetupButtons rtsb;

   public RaytracerSetupButtons(Sim s, SetupElementsPanel ep){
      super(s,ep);
      rtsb= this;

      setLayout(new GridLayout(5,3,5,25) );

      String[] scene= {"-----","scene 1", "scene 2", "define new"};
      for(int i=0; i<scene.length; i++){ scene_choice.addItem(scene[i]); }

      add(new Label(" scene: "));
      add(scene_choice);
      add(new Label(""));

      add(new Label(" viewing: "));
      add(viewing_button= new Button("viewing"));
      add(new Label(""));
      viewing_button.setEnabled(false);

      light_choice.addItem("-----");
      light_choice.addItem("define new");
      add(new Label(" lighting: "));
      add(light_choice);
      add(del_light_button= new Button("del"));

      String[] ostring= {"-----","new triangle", "new box", "new sphere"};
      for(int i=0; i<scene.length; i++){ object_choice.addItem(ostring[i]); }
      add(new Label(" objects: "));
      add(object_choice);
      add(del_obj_button= new Button("del"));

      add(new Label(" use scene: "));
      add(save_button= new Button("save"));
      add(new Label(""));

      scene_choice.addItemListener(
         new ItemListener(){
            public void itemStateChanged(ItemEvent e){
               Raytracer rt= (Raytracer)sim.get_algorithm();
               switch(scene_choice.getSelectedIndex()){
                  case 1: rt.scene1(); new_scene(); rt.get_scene(rtsb); draw_scene(); break;
                  case 2: rt.scene2(); new_scene(); rt.get_scene(rtsb); draw_scene(); break;
                  case 3: new_scene(); draw_scene(); break;
                  default: break;
      }  }  }  );
      viewing_button.addActionListener(
         new ActionListener(){
            public void actionPerformed(ActionEvent e){
               sep.removeAll();
               if(view_panel==null) //reuse object if possible 
                  view_panel= new SetupViewPanel(rtsb, view);
               else view_panel.setView(view);
               sep.add(view_panel);
               sep.validate();
      }  }  );
      light_choice.addItemListener(
         new ItemListener(){
            public void itemStateChanged(ItemEvent e){
               int lc= light_choice.getSelectedIndex();
               if((lc==1)&&(num_lights<LIGHT-1)){ //new light
                  lights[num_lights]= new TLight();
                  sep.removeAll(); sep.validate();
                  if(light_panel==null) //reuse object if possible 
                     light_panel= new SetupLightPanel(lights[num_lights], rtsb);
                  else light_panel.setLight(lights[num_lights]);
                  sep.add(light_panel);
                  num_lights++; num_lights_counter++;
                  light_choice.add("light"+num_lights_counter);
                  draw_scene();
               }else if(lc>1){
                  sep.removeAll(); sep.validate(); sep.repaint();
                  if(light_panel==null)
                     light_panel= new SetupLightPanel(lights[lc-2], rtsb);
                  else light_panel.setLight(lights[lc-2]);
                  sep.add(light_panel);         
               }
               sep.validate(); sep.repaint(); 
               if(light_panel!=null) light_panel.repaint();
      }  }  );
      del_light_button.addActionListener(
         new ActionListener(){
            public void actionPerformed(ActionEvent e){
               int lc= light_choice.getSelectedIndex();
               if(lc>1){
                  int j=lc-2;
                  for(; j<num_lights-1; j++){ lights[j]=lights[j+1]; }
                  num_lights--;
                  lights[j]=null;
                  light_choice.remove(lc);
                  sep.removeAll(); sep.validate();
               }
      }  }  );
      object_choice.addItemListener(
         new ItemListener(){
            public void itemStateChanged(ItemEvent e){
               int oc= object_choice.getSelectedIndex();
               if((oc==1)&&(num_objects<OBJECT-1)){ //new triangle
                  RaytracerDFETriangle t= new RaytracerDFETriangle(
                        new T3d(), new T3d(), new T3d(), null, null);
                  objects[num_objects]= t;
                  sep.removeAll(); sep.validate(); sep.repaint();
                  if(tri_panel==null) //reuse object if possible 
                     tri_panel= new SetupTrianglePanel(rtsb, t.tmp_world[0],
                           objects[num_objects].tmp_world[1], objects[num_objects].tmp_world[2]);
                  else tri_panel.setTriangle(objects[num_objects].tmp_world[0],
                           objects[num_objects].tmp_world[1], objects[num_objects].tmp_world[2]);
                  sep.add(tri_panel);
                  objects_ambiant[num_objects]= new TColor();
                  objects_diffuse[num_objects]= new TColor();
                  objects_specular[num_objects]= new TColor();
                  objects_color_attrib[num_objects]= new T3d();
                  if(color_panel==null) //reuse object if possible 
                     color_panel= new SetupColorPanel(rtsb,
                        objects_ambiant[num_objects], objects_diffuse[num_objects], 
                        objects_specular[num_objects],objects_color_attrib[num_objects]);
                  else color_panel.setColor(objects_ambiant[num_objects],
                        objects_diffuse[num_objects], objects_specular[num_objects],
                        objects_color_attrib[num_objects]);
                  sep.add(color_panel);
                  num_objects++; tri_counter++;
                  object_choice.add("triangle"+tri_counter);
                  draw_scene();
               }else if((oc==2)&&(num_objects<OBJECT-1)){ //new box
                  RaytracerDFEBox b= new RaytracerDFEBox(
                        new T3d(), new T3d(), null, null);
                  objects[num_objects]= b;
                  sep.removeAll(); sep.validate(); sep.repaint();
                  if(box_panel==null) //reuse object if possible 
                     box_panel= new SetupBoxPanel(rtsb, b);
                  else box_panel.setBox(b);
                  sep.add(box_panel);
                  objects_ambiant[num_objects]= new TColor();
                  objects_diffuse[num_objects]= new TColor();
                  objects_specular[num_objects]= new TColor();
                  objects_color_attrib[num_objects]= new T3d();
                  if(color_panel==null) //reuse object if possible 
                     color_panel= new SetupColorPanel(rtsb,
                        objects_ambiant[num_objects], objects_diffuse[num_objects], 
                        objects_specular[num_objects],objects_color_attrib[num_objects]);
                  else color_panel.setColor(objects_ambiant[num_objects],
                        objects_diffuse[num_objects], objects_specular[num_objects],
                        objects_color_attrib[num_objects]);
                  sep.add(color_panel);
                  num_objects++; box_counter++;
                  object_choice.add("box"+box_counter);
                  draw_scene();
               }else if((oc==3)&&(num_objects<OBJECT-1)){ //new sphere
                  RaytracerDFESphere sph= new RaytracerDFESphere(
                        new T3d(), 0, null, null);
                  objects[num_objects]= sph;
                  sep.removeAll(); sep.validate(); sep.repaint();
                  if(sph_panel==null) //reuse object if possible 
                     sph_panel= new SetupSpherePanel(rtsb, sph);
                  else sph_panel.setSphere(sph);
                  sep.add(sph_panel);
                  objects_ambiant[num_objects]= new TColor();
                  objects_diffuse[num_objects]= new TColor();
                  objects_specular[num_objects]= new TColor();
                  objects_color_attrib[num_objects]= new T3d();
                  if(color_panel==null) //reuse object if possible 
                     color_panel= new SetupColorPanel(rtsb,
                        objects_ambiant[num_objects], objects_diffuse[num_objects], 
                        objects_specular[num_objects],objects_color_attrib[num_objects]);
                  else color_panel.setColor(objects_ambiant[num_objects],
                        objects_diffuse[num_objects], objects_specular[num_objects],
                        objects_color_attrib[num_objects]);
                  sep.add(color_panel);
                  num_objects++; sph_counter++;
                  object_choice.add("sphere"+sph_counter);
                  draw_scene();
               }else if(oc>3){
                  sep.removeAll(); sep.validate(); sep.repaint();
                  if(objects[oc-4] instanceof RaytracerDFETriangle){ //Triangle
                     if(tri_panel==null)
                        tri_panel= new SetupTrianglePanel(rtsb, objects[oc-4].tmp_world[0],
                           objects[oc-4].tmp_world[1], objects[oc-4].tmp_world[2]);
                     else tri_panel.setTriangle(objects[oc-4].tmp_world[0],
                           objects[oc-4].tmp_world[1], objects[oc-4].tmp_world[2]);
                     sep.add(tri_panel);
                  }else if(objects[oc-4] instanceof RaytracerDFEBox){ //Box
                     if(box_panel==null) 
                        box_panel= new SetupBoxPanel(rtsb, (RaytracerDFEBox)objects[oc-4]);
                     else box_panel.setBox( (RaytracerDFEBox)objects[oc-4]);
                     sep.add(box_panel);
                  }else if(objects[oc-4] instanceof RaytracerDFESphere){ //Sphere
                     if(sph_panel==null)
                        sph_panel= new SetupSpherePanel(rtsb, (RaytracerDFESphere)objects[oc-4] );
                     else sph_panel.setSphere((RaytracerDFESphere)objects[oc-4]);
                     sep.add(sph_panel);
                  }
                  if(color_panel==null){ //reuse object if possible 
                     color_panel= new SetupColorPanel(rtsb,
                        objects_ambiant[oc-4], objects_diffuse[oc-4], 
                        objects_specular[oc-4],objects_color_attrib[oc-4]);
                  }else{ color_panel.setColor(objects_ambiant[oc-4],
                        objects_diffuse[oc-4], objects_specular[oc-4],
                        objects_color_attrib[oc-4]);
                  }
                  sep.add(color_panel);
                  color_panel.set_values();
                  //color_panel.repaint();
               }
               sep.validate(); sep.repaint(); 
               if(color_panel!=null) color_panel.repaint();
               if(tri_panel!=null) tri_panel.repaint();
               if(box_panel!=null) box_panel.repaint();
               if(sph_panel!=null) sph_panel.repaint();
      }  }  );
      del_obj_button.addActionListener(
         new ActionListener(){
            public void actionPerformed(ActionEvent e){
               int oc= object_choice.getSelectedIndex();
               if(oc>3){
                  int j=oc-4;
                  for(; j<num_objects-1; j++){ 
                     objects[j]=              objects[j+1];
                     objects_ambiant[j]=      objects_ambiant[j+1];
                     objects_diffuse[j]=      objects_diffuse[j+1];
                     objects_specular[j]=     objects_specular[j+1];
                     objects_color_attrib[j]= objects_color_attrib[j+1];
                  }
                  num_objects--;
                  objects[j]=null;
                  object_choice.remove(oc);
                  sep.removeAll(); sep.validate();
               }
      }  }  );
      save_button.addActionListener(
         new ActionListener(){
            public void actionPerformed(ActionEvent e){
               sim.reset();
               save_scene();
               sim.draw_text("Scene has been saved");
               sep.removeAll();
      }  }  );
   }

   /**
    *  draw objects in setup's wireframe using scene and viewing data stored in setup.
    */
   public void draw_scene(){
      DisplayCanvasWireFrame wire= (DisplayCanvasWireFrame)sim.get_setup_display_canvas();
      wire.set_view_pt(view.camera);
      wire.set_view_d(view.d);
      wire.reset(); //erase
      wire.get_graphics().setColor(Color.white);
      int i=0;
      for(; i<num_objects; i++) wire.draw(objects[i]);
      for(i=0; i<num_lights; i++){
         T3d tmp= new T3d(lights[i].x,lights[i].y,lights[i].z);
         wire.draw(new RaytracerDFELine(tmp, tmp, null, null));
      }
      wire.repaint();
   }

   /** 
    *  erase current data from setup.
    */
   public void new_scene(){
      int i=0;
      for(;i<OBJECT;i++) objects[i]=null;
      for(i=0;i<LIGHT;i++) lights[i]=null;
      num_objects=0; tri_counter=0; box_counter=0; sph_counter=0;
      num_lights=0; num_lights_counter=0;

      view= new RaytracerDFEView();
      sep.removeAll();

      light_choice.removeAll();      
      light_choice.addItem("-----");
      light_choice.addItem("define new");

      object_choice.removeAll();      
      String[] ostring= {"-----","new triangle", "new box", "new sphere"};
      for(i=0; i<ostring.length; i++){ object_choice.addItem(ostring[i]); }
   }

   /**
    *  add object from raytracer.
    */
   public void add_object(DisplayFrameElement o, TColor a, TColor d, TColor s, T3d c){
      if(num_objects<OBJECT){
         objects[num_objects]= o;
         objects_ambiant[num_objects]= a;
         objects_diffuse[num_objects]= d;
         objects_specular[num_objects]= s;
         objects_color_attrib[num_objects]= c;
         num_objects++;
         if(o instanceof RaytracerDFETriangle){
            object_choice.addItem("triangle"+tri_counter);
            tri_counter++;
         }
         if(o instanceof RaytracerDFEBox){ 
            object_choice.addItem("box"+box_counter);
            box_counter++;
         }
         if(o instanceof RaytracerDFESphere){ 
            object_choice.addItem("sphere"+sph_counter);
            sph_counter++;
         }
      }
   }

   /**
    *  add light from raytracer.
    */
   public void add_light(TLight l){
      if(num_lights<LIGHT){
         lights[num_lights]= l;
         num_lights++; num_lights_counter++;
         light_choice.addItem("light"+num_lights_counter);
      }
   }

   /**
    *  add light from raytracer.
    */
   public void set_view(T3d v, double d){
      view.camera= v;
      //view.setPercent(d);
   } 
   
   /**
    *  pass current scene to raytracer and generate src code
    *    for the code window.
    */
   public void save_scene(){
      sim.get_alg_source().reset();
      Raytracer rt= (Raytracer) sim.get_algorithm();
      rt.set_view(view);
      rt.viewing2();
      rt.set_lights(lights, num_lights);
      rt.set_objects(objects, objects_ambiant, objects_diffuse, 
                     objects_specular, objects_color_attrib, num_objects);
      sim.get_code_panel().set_scrollbar_range(sim.get_alg_source().get_lines_of_code());
               
   }    
}

