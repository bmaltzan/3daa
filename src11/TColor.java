/**
 *  TColor.java
 *
 *  holds an RGB color
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public final class TColor implements Cloneable{ 
   public double r,g,b;
   public TColor(){}
   public TColor(double r1,double g1,double b1){
      r=r1; g=g1; b=b1;
   }
   public Object clone(){
      Object o=null;
      try{
         o= super.clone();
      }catch(CloneNotSupportedException e){
         System.out.println("clone error: TColor");
      }
      return o;
   }
   public String toString(){
      return "r="+Utils.round_double(r,2)+
             "  g="+Utils.round_double(g,2)+
             "  b="+Utils.round_double(b,2);
   }
}

