import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
//import javax.media.j3d.*;

/**
 *  Sim.java
 *
 *  main class for simulation.
 *  controls switching card panels.
 *  holds pointers to all display components.
 *  controls switching algorithms.
 *  contains components that dont require overriding:
 *    font panel, and debug panel.
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public final class Sim extends Applet{

   /**
    *  panel that has buttons to select the pref, setup, and sim panels.
    *  has TextField that algorithms can write to.
    */
   class CardButtonPanel extends Panel {
      private boolean first_press=true;
      public CardButtonPanel(){
         Button  setup_button, pref_button, sim_button, help_button;
         setLayout(new FlowLayout());
         add(pref_button= new Button("Pref"));
         add(setup_button= new Button("Setup"));
         add(sim_button= new Button("Sim"));
         add(tf);
         add(help_button= new Button("?"));
         pref_button.addActionListener(
            new ActionListener(){
               public void actionPerformed(ActionEvent e){
                  draw_text(state.get_msg_pref_button());
                  ((CardLayout)card_panel.getLayout()).show(card_panel,PREF);
                  state.set_current_card(Sim.PREF_SELECTED);
         }  }  );
         setup_button.addActionListener(
            new ActionListener(){
               public void actionPerformed(ActionEvent e){
                  draw_text(state.get_msg_setup_button());
                  ((CardLayout)card_panel.getLayout()).show(card_panel,SETUP);
                  state.set_current_card(Sim.SETUP_SELECTED);
         }  }  );
         sim_button.addActionListener(
            new ActionListener(){
               public void actionPerformed(ActionEvent e){
                  ((CardLayout)card_panel.getLayout()).show(card_panel,SIM);
                  if(first_press){
                     draw_text(state.get_msg_first_tim_in_sim());
                     first_press= false;
                  }else{ draw_text(state.get_msg_sim_button()); }
                  state.set_current_card(Sim.SIM_SELECTED);
         }  }  );
         help_button.addActionListener(
            new ActionListener(){
               public void actionPerformed(ActionEvent e){
                  draw_text(state.get_msg_help());
                  ((CardLayout)card_panel.getLayout()).show(card_panel,HELP);
                  state.set_current_card(Sim.HELP_SELECTED);
         }  }  );
      }
      public void update(Graphics g){ paint(g); }  
      public void paint(Graphics g){
         Dimension size= getSize();
         g.drawRect(0,0, size.width-1,size.height-1);
   }  }

   //------------------------  p r e f    p a n e l s  -------

   /**
    *  prints free memory.
    */
   class DebugPanel extends Panel{
      class FreeMem implements ActionListener{
         public void actionPerformed(ActionEvent e){
            Runtime rt= Runtime.getRuntime();
            tf.setText("total: " + rt.totalMemory() + ", free: " + rt.freeMemory());
      }  }
      //class GC implements ActionListener {
      //   public void actionPerformed(ActionEvent e) {
      //      tf.setText("garbage collector run");
      //}  }
      private Button mem_button= new Button("Mem");
      //private Button gc= new Button("gc");
      public DebugPanel(){
         add(new Label("debugging:"));
         add(mem_button);
         //add(gc);
         mem_button.addActionListener(new FreeMem());
      }
      public Dimension getPreferredSize(){ return new Dimension(160,40); }
      public void paint(Graphics g){
         g.drawRect(0,0,getSize().width-1,getSize().height-1);
   }  }

   /**
    *  allows user to select the font size for the code panel.
    */
   class FontPanel extends Panel{
      private Choice c = new Choice();
      private String[] font_sizes= 
            {"8","9","10","11","12","13","14","15","16","17","18","19","20","21","22"};
      public FontPanel(){
         for(int i=0; i<15; i++){ c.addItem(font_sizes[i]); }
         add(new Label("font size:"));
         c.select(6);
         add(c);
         c.addItemListener(
            new ItemListener(){
               public void itemStateChanged(ItemEvent e) {
                  draw_text("font size: "+ (c.getSelectedIndex()+8) );
                  code_panel.set_font_size(c.getSelectedIndex()+8);
                  help_ta.setFont(new Font("Monospaced", Font.PLAIN, c.getSelectedIndex()+8));
                  about_sim.setFont(new Font("Monospaced", Font.PLAIN, c.getSelectedIndex()+8));
                  about_alg.setFont(new Font("Monospaced", Font.PLAIN, c.getSelectedIndex()+8));
         }  }  );
      }
      public Dimension getPreferredSize(){ return new Dimension(160,40); }
      public void paint(Graphics g){ g.drawRect(0,0,getSize().width-1,getSize().height-1); }
   }

   //-----------------------------  v a r s  -----------
   private final String SETUP= "Setup Scene";
   private final String PREF= "Preferences";
   private final String SIM= "Simulator";
   private final String HELP= "Help";
   private final String BLANK= "Blank";

   private final static int PREF_SELECTED=1, SETUP_SELECTED=2, 
                            SIM_SELECTED=3, HELP_SELECTED=4;

   private SimState state;
   private Algorithm algorithm;
   private AlgSource alg_source;

   private AlgControlThread alg_thread;
   private AlgTape recording, scene_recording;
   private Point mouse_press= new Point(0,0);

   private TextField tf= new TextField(null, 58);
   private TextArea help_ta= new TextArea();

   private Panel setup_panel= new Panel(), pref_panel= new Panel(), 
                 sim_panel= new Panel(),   card_panel= new Panel(),
                 help_panel= new Panel(),  blank_panel= new  Panel();

   private CardButtonPanel card_button_panel= new CardButtonPanel();

   private AlgPanel alg_panel;
   private DebugPanel debug_panel= new DebugPanel();
   private FontPanel font_panel= new FontPanel();
   private TextArea about_sim= new TextArea(12,80);
   private TextArea about_alg= new TextArea(12,80);

   private DisplayCanvas display1_canvas;
   private DisplayCanvas display2_canvas;
   //private DisplayCanvas3d java3d_canvas;
   private ButtonPanel button_panel;
   private Controller controller;
   private CodePanel code_panel;

   private DisplayCanvas setup_display_canvas;
   private SetupElementsPanel setup_elements_panel;
   private SetupButtonPanel setup_button_panel;

   //-----------------------------  s i m  -----------
   public Sim(){}
   public void init(){
      alg_thread= new AlgControlThread(this);
      controller= new Controller(alg_thread,8,14);
      controller.set_stop();
      select_algorithm(new RaytracerState(this));
      alg_panel= new AlgPanel(this);

      tf.setText("most help messages will be displayed here.");
      about_sim.setText(state.get_sim_about_text());
      about_sim.setEditable(false);
      about_alg.setEditable(false);
      tf.setEditable(false);

      pref_panel.add(alg_panel);
      pref_panel.add(debug_panel);
      pref_panel.add(font_panel);
      pref_panel.add(about_sim);
      pref_panel.add(about_alg);

      help_panel.setLayout(new BorderLayout());
      help_panel.add("Center", help_ta);
      help_ta.setEditable(false);

      blank_panel.add(new Label("To get started, press 'Sim', then 'render'"));
      
      card_panel.setLayout(new CardLayout());
      card_panel.add(blank_panel, SETUP);
      card_panel.add(setup_panel, SETUP);
      card_panel.add(pref_panel, PREF);
      card_panel.add(sim_panel, SIM);
      card_panel.add(help_panel, HELP);
      
      ((CardLayout)card_panel.getLayout()).show(card_panel,BLANK);

      setLayout(new BorderLayout());
      add("North", card_button_panel);
      add("Center", card_panel);
   }

   public void stop() { alg_thread=null; }
   public void start() {
      if(alg_thread==null) alg_thread= new AlgControlThread(this);
   }

   //---------------------------------------  access methods  ----

   /**
    *  draw a string of text in the TextField at the top of the screen.
    *  @param s text to be drawn.
    */
   public void draw_text(String s){ if(s!=null)tf.setText(s); }

   /**
    *  use an algorithm's state to get and create it's setup and sim panels,
    *  get it's algorithm, source code, about message, and help message.
    *  @param s the algorithm's state.
    */
   public void select_algorithm(SimState s){
      state=s;
      algorithm= state.get_algorithm();
      alg_source= state.get_alg_source();
      about_alg.setText(state.get_alg_about_text());
      help_ta.setText(state.get_msg_ta_help());

      sim_panel.removeAll();
      display1_canvas= state.get_display1_canvas();
      sim_panel.add(display1_canvas);
      display2_canvas= state.get_display2_canvas();
      sim_panel.add(display2_canvas);
      button_panel= state.get_button_panel();
      sim_panel.add(button_panel);
      sim_panel.add(controller);
      code_panel= state.get_code_panel();
      sim_panel.add(code_panel);

      setup_panel.removeAll();
      setup_display_canvas= state.get_display_canvas();
      setup_panel.add(setup_display_canvas);
      setup_elements_panel= state.get_setup_elements_panel();
      setup_button_panel= state.get_setup_button_panel(setup_elements_panel);
      setup_panel.add(setup_button_panel);
      setup_panel.add(setup_elements_panel);
   }

   /**
    *  signal each component of the sim card panel to
    *  reset itself.
    */
   public void reset(){
      if(display1_canvas!=null) display1_canvas.reset();
      if(display2_canvas!=null) display2_canvas.reset();
      if(tf!=null)              tf.setText(null);
      if(code_panel!=null)      code_panel.reset();
      if(alg_thread!=null)      alg_thread.reset();
      if(button_panel!=null)    button_panel.reset();
      if(controller!=null)      controller.set_stop();
      if(controller!=null)      controller.disableControls();
      scene_recording=null;
      recording=null;
   }

   // -----------------------------  methods to return algorithm elements  -----

   /**
    *  return the setup display panel.
    */
   public DisplayCanvas get_setup_display_canvas(){ return setup_display_canvas; }

   /**
    *  return the setup button panel.
    */
   public SetupButtonPanel get_setup_button_panel(){ return setup_button_panel; }

   /**
    *  return the setup elements panel.
    */
   public SetupElementsPanel get_setup_elements_panel(){ return setup_elements_panel; }

   /**
    *  return the currently selected algorithm.
    */
   public Algorithm get_algorithm(){ return algorithm; }

   /**
    *  return the currently selected algorithm source code.
    */
   public AlgSource get_alg_source(){ return alg_source; }

   /**
    *  return recording of the algorithm.
    */
   public AlgTape get_recording(){ return recording; }

   /**
    *  called by the algorithm to store a recording of 
    *  the algorithm in action.
    */
   public void set_recording(AlgTape t){ recording= t; }

   /**
    *  return recording of the algorithms setup.
    */
   public AlgTape get_setup_recording(){ return scene_recording; }

   /**
    *  called by the algorithm to store a recording of any setup
    *  selected by the user through the setup card panel.
    */
   public void set_setup_recording(AlgTape t){ scene_recording= t; }

   //
   // ------------------------  methods to return the sim's visual components  -----

   /**
    *  return the <code>ButtonPanel</code> for the currently selected algorithm.
    */
   public ButtonPanel get_button_panel(){ return button_panel; }

   /**
    *  return the vcr-like <code>Controller</code>.
    */
   public Controller get_controller(){ return controller; }

   /**
    *  return the <code>SimState</code> for the currently selected algorithm.
    */
   public SimState get_state(){ return state; }

   /**
    *  return the left-side <code>DisplayCanvas</code> for the currently selected algorithm.
    */
   public DisplayCanvas get_display1_canvas(){ return display1_canvas; }

   /**
    *  return the right-side <code>DisplayCanvas</code> for the currently selected algorithm.
    */
   public DisplayCanvas get_display2_canvas(){ return display2_canvas; }

   /**
    *  return the <code>AlgControlThread</code> for the currently selected algorithm.
    */
   public AlgControlThread get_control_thread(){ return alg_thread; }

   /**
    *  return the <code>CodePanel</code> for the currently selected algorithm.
    */
   public CodePanel get_code_panel(){ return code_panel; }

   //----------------  a p p l i c a t i o n  -----------
   public static void main(String[] args) {
      Sim applet = new Sim();
      Frame aFrame = new Frame("3d Animation Applet");
      aFrame.addWindowListener(
         new WindowAdapter(){
            public void windowClosing(WindowEvent e){
               System.exit(0);
      }  }  );
      aFrame.add(applet, BorderLayout.CENTER);
      aFrame.setSize(620,530);
      applet.init();
      applet.start();
      aFrame.setVisible(true);
   }
} //class Sim

