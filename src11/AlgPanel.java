import java.awt.*;
import java.awt.event.*;

/**
 *  AlgPanel.java
 *
 *  allows user choice of algorithm to simulate.
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public class AlgPanel extends Panel{

   private String[] alg={
      "Raytracer"
   };
   // add a string to appear in the algorithm panel in pref cardpanel.

   private Sim sim;
   private Choice c = new Choice();

   public AlgPanel(Sim s){
      sim=s;
      add(new Label("algorithm:"));
      for(int i=0; i<alg.length; i++){ c.addItem(alg[i]); }
      c.select(0);
      add(c);
      c.addItemListener(
         new ItemListener(){
            public void itemStateChanged(ItemEvent e){
               SimState tmp_state;
               sim.draw_text(sim.get_state().get_msg_alg_selected());
               switch(c.getSelectedIndex()){
                  default:
                  case 0: tmp_state= new RaytracerState(sim); break;

                  // add other cases here. 
                  // should override SimState.

               }
               sim.select_algorithm(tmp_state);
      }  }  );
   }
   public Dimension getPreferredSize(){ return new Dimension(220,40); }
   public void paint(Graphics g){ g.drawRect(0,0,getSize().width-1,getSize().height-1); }
}