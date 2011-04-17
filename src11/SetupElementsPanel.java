import java.awt.*;
import java.awt.event.*;

/**
 *  SetupElementsPanel.java
 *
 *  realestate on the destop for placing component control buttons. 
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public class SetupElementsPanel extends Panel{
   protected Sim sim;
   
   public SetupElementsPanel(Sim s){ sim=s; }
   public void paint(Graphics g){ g.drawRect(0,0,getSize().width-1,getSize().height-1); }
   public void reset(){ removeAll(); }
   public Insets getInsets(){ return new Insets(3,3,3,3); }
   public Dimension getPreferredSize(){ return new Dimension(580,200); }
}
