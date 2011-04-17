/**
 *  TLight.java
 *
 *  holds an (x,y,z) and a brightness.
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public final class TLight implements Cloneable{
   public double x,y,z;
   public double bright;
   public TLight(){
      x=0; y=0; z=0; bright=1;
   }
   public TLight(double x1,double y1,double z1, double bright1){
      x=x1; y=y1; z=z1; bright=bright1;
   }

   public Object clone(){
      Object o=null;
      try{
         o= super.clone();
      }catch(CloneNotSupportedException e){
         System.out.println("clone error: TLight");
      }
      return o;
   }
   public String toString(){
      return "x="+Utils.round_double(x,2)+
           "  y="+Utils.round_double(y,2)+
           "  z="+Utils.round_double(z,2)+
           "  bright="+Utils.round_double(bright,2);
   }
}
