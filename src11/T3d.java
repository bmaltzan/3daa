/**
 *  T3d.java
 *
 *  holds a (x,y,z)
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public final class T3d implements Cloneable{
   public double x,y,z; 
   public T3d(){}
   public T3d(double a,double b,double c){
      x=a;y=b;z=c;
   }
   public Object clone(){
      Object o=null;
      try{
         o= super.clone();
      }catch(CloneNotSupportedException e){
         System.out.println("clone error: T3d");
      }
      return o;
   }
   public String toString(){
      return "("+Utils.round_double(x,2)+
             ","+Utils.round_double(y,2)+
             ","+Utils.round_double(z,2)+")";
   }
}
