/*
* @(#)Controller.java  1.00 Fri 12-27-1997
*
*/

import java.awt.*;
import java.awt.event.*;

/**
* The Controller class 
*
* @author  E F Head
* @version 1.00, 97/12/25
* @since   JDK1.1.5
* @serial
*/

public class Controller extends Panel {

    static int NoCONTROLS;
    static final Color THISWAY = Color.green;
    static final Color OTHERWAY = Color.gray;
//  change to static 11/7
    final Label backwardLabel = new Label ("<<");
    final Label forwardLabel = new Label (">>",Label.RIGHT);

    final CButton headBackButton = new CButton(); 
    final CButton headForeButton = new CButton();
    final Button stepR = new Button("Step>");
    final Button stepL = new Button("<Step");
    final Button stop  = new Button("Stop");

    private boolean xqtNotStarted_ = true;
    private XQTThread gXQT_;

    public Controller (XQTThread x, int num_controls, int font_size) {
      //final XQTThread xqt = x;
      NoCONTROLS= num_controls;
      gXQT_=x;

      int speed = 0;  //?? NoCONTROLS;  // number of controls 
      
      backwardLabel.setFont(new Font ("Helvetica", Font.BOLD, font_size));
      forwardLabel.setFont(new Font ("Helvetica", Font.BOLD, font_size));
      backwardLabel.setForeground(OTHERWAY);
      forwardLabel.setForeground(THISWAY);

      add(backwardLabel);

      CButton b = new CButton();    
      // lowest speed back control button

      CButton lowN = null;  // lower control speed
      CButton highN = null; // higher control speed
         
      for (int i=0; i< NoCONTROLS ; i++) {
         add(b);
         lowN = (i==NoCONTROLS-2)? headBackButton:
         (i==NoCONTROLS-1)? null : new CButton();
         b.lowerNeighbor(lowN);
         b.higherNeighbor(highN);
         // add eventlistener speed++
         { final int speedF = speed++;
           final CButton bF = b;
           
          b.addActionListener( 
            new ActionListener(){ 
               public void actionPerformed(ActionEvent e){
                  if (xqtNotStarted_) {
                     xqtNotStarted_ = false;
                     gXQT_.start();
                  }
                  synchronized(gXQT_) {
                     gXQT_.notify();
                     bF.eventPressed();
                     gXQT_.backward(speedF*200);                   
                  }
                  backwardLabel.setForeground(THISWAY);
                  forwardLabel.setForeground(OTHERWAY);
                  headForeButton.setOFF();
               }
            }
          ); 
         }
         highN = b;
         b = lowN;
      }// for loop

      stepL.setFont(new Font ("Helvetica", Font.PLAIN, font_size));
      { 
       stepL.addActionListener( 
         new ActionListener(){ 
            public void actionPerformed(ActionEvent e){                 
               if (xqtNotStarted_) {
                  xqtNotStarted_ = false;
                  gXQT_.start();
               }
               backwardLabel.setForeground(THISWAY);
               forwardLabel.setForeground(OTHERWAY);
               headForeButton.setOFF();
               headBackButton.setOFF();
               synchronized(gXQT_) {
                  gXQT_.notify();
                  gXQT_.backwardStep();
               }
            }
         }
       );
      }
      add(stepL);  // <STEP

      stop.setFont(new Font ("Helvetica", Font.PLAIN, font_size));
      { 
       stop.addActionListener( 
         new ActionListener(){ 
            public void actionPerformed(ActionEvent e){                 
               backwardLabel.setForeground(OTHERWAY);
               forwardLabel.setForeground(OTHERWAY);
               headForeButton.setOFF();
               headBackButton.setOFF();
               gXQT_.pause();
            }
         }
       );
      }
      add(stop);  // STOP

      stepR.setFont(new Font ("Helvetica", Font.PLAIN, font_size));
      {  
       stepR.addActionListener( 
         new ActionListener(){ 
           public void actionPerformed(ActionEvent e){                 
              if (xqtNotStarted_) {
                   xqtNotStarted_ = false;
                   gXQT_.start();
               }
               backwardLabel.setForeground(OTHERWAY);
               forwardLabel.setForeground(THISWAY);
               headForeButton.setOFF();
               headBackButton.setOFF();
               synchronized (gXQT_) {
                  gXQT_.notify();               
                  gXQT_.forwardStep();
               }
            }
         }
       );
      }
      add(stepR);  // STEP>
            

      b = headForeButton;
      lowN = null;
      speed = NoCONTROLS;
      for (int i=0; i<NoCONTROLS; i++) {
         
         add(b);
         b.higherNeighbor(highN = (i==NoCONTROLS)?null:new CButton());
         b.lowerNeighbor(lowN);
         { speed--;
           final int speedF = speed;
           final CButton bF = b;
           b.addActionListener( 
            new ActionListener(){
               public void actionPerformed(ActionEvent e){
                  if (xqtNotStarted_) {
                     xqtNotStarted_ = false;
                     gXQT_.start();
                  }

                  bF.eventPressed();
                  synchronized (gXQT_) {
                     gXQT_.notify();
                  }
                  gXQT_.forward(speedF*200);
                  backwardLabel.setForeground(OTHERWAY);
                  forwardLabel.setForeground(THISWAY);
                  headBackButton.setOFF();
               }
            }
           );
         }
      lowN = b;
      b = highN;
      }
   add(forwardLabel);
   set_stop();
   disableControls();
   }

   public void set_stop(){
       backwardLabel.setForeground(OTHERWAY);
       forwardLabel.setForeground(OTHERWAY);
       headForeButton.setOFF();
       headBackButton.setOFF();
       gXQT_.pause();
   }
   public void disableControls() {
       backwardLabel.setForeground(OTHERWAY);
       forwardLabel.setForeground(OTHERWAY);
       headForeButton.setOFF();
       headBackButton.setOFF();
       headForeButton.disableCButton();
       headBackButton.disableCButton();
       stepL.setEnabled(false);
       stepR.setEnabled(false);
       repaint();
      }
   public void enableControls() {
      backwardLabel.setForeground(OTHERWAY);
      forwardLabel.setForeground(OTHERWAY);
      stepL.setEnabled(true);
      stepR.setEnabled(true);
      headForeButton.enableCButton();
      headBackButton.enableCButton();
      repaint();
   }
   public void enableReverseControls() {
       backwardLabel.setForeground(OTHERWAY);
       headBackButton.enableCButton();
       stepL.setEnabled(true);
       repaint();
   }   
   public void disableReverseControls() {
      backwardLabel.setForeground(OTHERWAY);
      headBackButton.setOFF();
      stepL.setEnabled(false);
      forwardLabel.setForeground(OTHERWAY);
      headBackButton.disableCButton();
      headForeButton.setOFF();
      repaint();
   }   
   public void enableForwardControls() {
       forwardLabel.setForeground(OTHERWAY);
       headForeButton.enableCButton();
       stepR.setEnabled(true);
       repaint();
   }   
   public void disableForwardControls() {
      forwardLabel.setForeground(OTHERWAY);
      headForeButton.setOFF();
      stepR.setEnabled(false);
      backwardLabel.setForeground(OTHERWAY);
      headBackButton.disableCButton();
      headBackButton.setOFF();
      repaint();
   }   
   public void stopReverse() {
     backwardLabel.setForeground(OTHERWAY);
     headBackButton.setOFF();
     repaint();
   }
   public void stopForeward() {
     forwardLabel.setForeground(OTHERWAY);
     headForeButton.setOFF();
     repaint();
   }

 static final Color OFF = Color.gray;
 static final Color ON = Color.red;

 private class CButton extends Button {
   protected CButton higherNeighbor_ = null;
   protected CButton lowerNeighbor_ = null;

   CButton () {
      super(".");
      setBackground(OFF);
   }
   CButton (String l) {
      super(l);
      setFont(new Font ("Helvetica", Font.BOLD, 20) );
      setBackground(OFF);
   }
   void higherNeighbor (CButton b) {
      higherNeighbor_ = b;
   }
   void lowerNeighbor (CButton b) {
      lowerNeighbor_ = b;
   }
   public void eventPressed () {
      setBackground(ON);
      if (higherNeighbor_ != null) higherNeighbor_.setOFF();
      if (lowerNeighbor_ != null) lowerNeighbor_.setON();
   }
   void setON() {
      setBackground(ON);
      if (lowerNeighbor_ != null) lowerNeighbor_.setON();
      repaint();
   }
   void disableCButton() {
      setEnabled(false);
      if (higherNeighbor_ != null) higherNeighbor_.disableCButton();
   }
   void setOFF() {
      setBackground(OFF);
      if (higherNeighbor_ != null) higherNeighbor_.setOFF();
       repaint();
   }
   void enableCButton() {
      setBackground(OFF);
      setEnabled(true);
      if(higherNeighbor_ != null) higherNeighbor_.enableCButton();
   }
   public void update(Graphics g) {
      paint(g);
   }
 } // end of CButton
}         
