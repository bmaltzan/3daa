import java.awt.*;

/**
 *  DisplayFrame.java
 *
 *  holds <code>DisplayFrameElements</code> for one frame of a recording.
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public class DisplayFrame{
   class Link{
      private DisplayFrameElement data;
      private Link next;

      public Link(DisplayFrameElement d, Link n){ data=d; next=n; }
      public void set_next(Link n){ next=n; }
      public Link next(){ return next; }
      public DisplayFrameElement data(){ return data; }
   }

   private Link first=null, last=null, current=null;
   private int num_elements=0;

   /**
    *  add to frame one element, appended to end of a list.
    *  @param f element to add.
    */
   public void append(DisplayFrameElement d){
      if(first==null){
         first= new Link(d,null);
         last=first;
      }else{
         last.set_next(new Link(d,null));
         last= last.next();
      }
      num_elements++;
   } 

   /**
    *  sets current frame to the first frame in the sequence.
    */
   public void first(){ current= null; }

   /**
    *  returns next element in sequence.
    */
   public DisplayFrameElement next(){
      if(current==null){
         if(first==null) return null;
         else{
           current=first;
           return first.data();
         }
      }else{
         if(current.next()==null) return null;
         else{ 
            current=current.next();
            return current.data();
   }  }  }

   public String toString(){
      Link tmp= first;
      StringBuffer s= new StringBuffer(100);
      s.append("DisplayFrame:67, print all elements:\n");
      while(tmp!=null){
         s.append(tmp.data().toString()+"\n");
         tmp=tmp.next();
      }
      return s.toString();
   }
}

