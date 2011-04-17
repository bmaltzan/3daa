import java.awt.*;

/**
 *  RaytracerDFELight.java
 *
 *  a wireframe line
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public class RaytracerDFELight extends DisplayFrameElement {
   public T3d center;
   public double bright;

   public RaytracerDFELight(){ center= new T3d(); }

   public RaytracerDFELight(T3d p1, double b, T3d lookp){
      center= (T3d)p1.clone();
      bright=b;
      if(lookp!=null){
         Utils.tran3d(center, lookp);
      }
      tmp_world= make_light();
      size=1;
   }

   public void draw(Graphics g, Point[] p){
      if(p==null) System.out.println("RaytracerDFELight:31");
      System.out.println("RaytracerDFELight:30");
      if(p[0]==null) System.out.println("RaytracerDFELight:31");
      System.out.println("RaytracerDFELight:32");
      g.drawLine(p[0].x,p[0].y, p[0].x,p[0].y);
      System.out.println("RaytracerDFELight:33");
   }

   public final T3d[] make_light(){
      T3d[] p= new T3d[1];
      p[0]= center; 
      return p;
   }

   public String toString(){
      return super.toString()+
             "\nRaytracerDFELight\n";
   }
}








