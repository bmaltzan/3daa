/**
 *  T2d.java
 *
 *  holds an (x,y) points
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public final class T2d implements Cloneable{
   public double x,y; 
   public T2d(){}
   public T2d(double a,double b){
      x=a;y=b;
   }
   public Object clone(){
      Object o=null;
      try{
         o= super.clone();
      }catch(CloneNotSupportedException e){
         System.out.println("clone error: T2d");
      }
      return o;
   }
   public String toString(){
      return "("+Utils.round_double(x,2)+
             ","+Utils.round_double(y,2)+")";
   }
}
