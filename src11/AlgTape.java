/**
 *  AlgTape.java
 *
 *  stores ordered frames of animation.
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public class AlgTape{
   class Link{
      private AlgFrame data;
      private Link next,prev;

      public Link(AlgFrame d, Link p, Link n){ data=d; prev=p; next=n; }
      public void set_next(Link n){ next=n; }
      public void set_prev(Link p){ prev=p; }
      public Link next(){ return next; }
      public Link prev(){ return prev; }
      public AlgFrame data(){ return data; }
   }

   private Link first=null, last=null, current=null;
   private int num_elements=0;

   /**
    *  appends an <code>AlgFrame</code> to the end of the 
    *  sequence of frames.
    */
   public void append(AlgFrame o){
      if(first==null){
         first= new Link(o,null,null);
         last=first;
      }else{
         last.set_next(new Link(o,last,null));
         last= last.next();
      }
      num_elements++;
   }

   /**
    *  replaces current frame.
    */
   public void set_data(AlgFrame o){
      if(current!=null){
         current= new Link(o,current.prev(),current.next());
         current.prev().set_next(current);
         current.next().set_prev(current);
   }  }

   /**
    *  returns next frame.
    *  if frames have not been retrieved or at last frame, returns null.
    */
   public AlgFrame next(){
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

   /**
    *  returns previous frame. 
    *  if frames have not been retrieved or at first frame, returns null.
    */
   public AlgFrame prev(){
      if(current==null) return null;
      else{
         if(current.prev()==null){ return null;
         }else{
            current=current.prev();
            return current.data();
   }  }  }

   /**
    *  return last frame in the sequence, and sets current frame to last.
    */
   public AlgFrame get_last(){  current=last; return last.data();  }

   /**
    *  return first frame in the sequence, and sets current frame to first.
    */
   public AlgFrame get_first(){ current=first; return first.data(); }

   /**
    *  reset, as if no frames have yet been called
    */
   public void reset_current(){ current=null; }

   /**
    *  return current frame
    */
   public AlgFrame current(){ return current.data(); }

   /**
    *  return number of frames.
    */
   public int get_num_elements(){ return num_elements; }

   /**
    *  if at least 1 frame exists, returns whether is last in sequence.
    *  else returns true.
    */
   public boolean is_last(){ return current==last; }

   /**
    *  if at least 1 frame exists, returns whether is first in sequence.
    *  else returns true.
    */
   public boolean is_first(){ return current==first; }

   public String toString(){
      Link tmp= first;
      StringBuffer s= new StringBuffer(100);
      s.append("print all elements:\n");
      while(tmp!=null){
         s.append(tmp.data().toString()+"\n");
         tmp=tmp.next();
      }
      return s.toString();
   }

   // ------------------------  
   public static void main(String args[]){
      AlgTape tape= new AlgTape();

      System.out.println(tape.next());
      tape.append(new AlgFrame(10,null,null,null));
      tape.append(new AlgFrame(20,null,null,null));
      tape.append(new AlgFrame(30,null,null,null));
      tape.append(new AlgFrame(40,null,null,null));
      tape.append(new AlgFrame(50,null,null,null));

      System.out.println(tape.next());
      System.out.println(tape.next());
      System.out.println(tape.prev());
      System.out.println(tape.prev());
      System.out.println(tape.next());
      System.out.println(tape.current());
      System.out.println(tape.next());
      //tape.set_data(new AlgFrame(2,null,null,null));

      System.out.println(tape);
   }
}
