/**
 *  AlgControlThread.java
 *
 *  controls retrieving frames of animation.
 *  implements methods in <code>XQTThread</code> which signal
 *  stop, forward, reverse, step forward, step reverse, and
 *  the delay between frames of animation.
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public class AlgControlThread extends XQTThread{
   public static final int REVERSE=1, STOP=2, FORWARD=3,
                           STEP_REVERSE=4, STEP_FORWARD=5;

   private AlgFrame frame;
   private boolean quit_thread=false, busy=false;
   private int code_delay=0, alg_state=2;
   protected Sim sim;

   public AlgControlThread(Sim s){ sim=s; }

   public void run(){
      while(true){
         while(alg_state!=2){ //removes sleep delay on startup
            if(sim.get_recording()==null){
               alg_state=2;
               //control_panel.set_stop();
            }
            busy=true;
            switch(alg_state){
               case 1: //play reverse
                  frame= sim.get_recording().prev();
                  if(frame==null){
                     sim.get_controller().set_stop();
                     alg_state=2;
                  }else{
                     sim.get_algorithm().draw_frame(frame);
                  }
                  break;
               case 3: //play forward
                  frame= sim.get_recording().next();
                  if(frame==null){
                     sim.get_controller().set_stop();
                     alg_state=2;
                  }else{
                     sim.get_algorithm().draw_frame(frame);
                  }
                  break;
               case 4: // step reverse
                  frame= sim.get_recording().prev();
                  if(frame==null){
                     sim.get_controller().set_stop();
                  }else{
                     sim.get_algorithm().draw_frame(frame);
                  }
                  sim.get_controller().set_stop();
                  alg_state=2;
                  break;
               case 5: //step forward
                  frame= sim.get_recording().next();
                  if(frame==null){
                  }else{
                     sim.get_algorithm().draw_frame(frame);
                  }
                  sim.get_controller().set_stop();
                  alg_state=2;
                  break;
            }
            if(frame!=null){
               Utils.pause(code_delay);
               //try{
               //   wait(code_delay);
               //}catch(InterruptedException e){}
               //C:\My Documents\java\project\v.34>view
               //java.lang.IllegalMonitorStateException: current thread not owner
               //at VCRControlThread.run(Compiled Code)
            }
            if(quit_thread){ alg_state=2; return; }
         }//---------- while(alg_state!=2)
         busy=false;
      }//------------- while(true)
   }

   /**
    * prevent other thread from interfering with animation windows
    */
   public boolean get_busy(){ return busy; }

   private final int rev_selected=1,  stop_selected=2,  for_selected=3,
                     step_rev_selected=4, step_for_selected=5;

   public void reset(){ alg_state=2; }
   public void forwardStep(){  alg_state= step_for_selected; code_delay= 0; }
   public void backwardStep(){ alg_state= step_rev_selected; code_delay= 0; }
   public void forward(int speed){  alg_state= for_selected; code_delay= speed; }
   public void backward(int speed){ alg_state= rev_selected; code_delay= speed; }
   public void die(){ quit_thread=true; }
   public void pause(){ alg_state= stop_selected; }

   public void runForward(){ forward(0); }
   public void runBackward(){ backward(0); }

   /**
    *  set the execution mode of the thread: stop,forward,rev,step, and
    *  the length of delay.
    */
   public void set_alg_state(int state, int delay){ alg_state= state; code_delay= delay; }
}
