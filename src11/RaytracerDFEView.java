import java.awt.*;

/**
 *  RaytracerDFEView.java
 *
 *  a wireframe line
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public class RaytracerDFEView extends DisplayFrameElement {
   public T3d camera;
   public double d;
   public double d_percent;
   public double gamma;

   public RaytracerDFEView(){ 
      camera= new T3d(48000,8500,7000);//480,85,70
      d_percent= 1;
      d= (Math.max(camera.x,Math.max(camera.y,camera.z)))*d_percent;
      gamma=1.4;
   }

   public RaytracerDFEView(T3d c, double dp, double g){
      camera= (T3d)c.clone();
      d_percent= dp;
      d= (Math.max(camera.x,Math.max(camera.y,camera.z)))*dp;
      gamma=g;
   }

   public void setPercent(double dp){
      d_percent= dp;
      d= (Math.max(camera.x,Math.max(camera.y,camera.z)))*dp;
   }

   public void draw(Graphics g, Point[] p){}
   public final T3d[] make_view(){return null;}
   public String toString(){
      return super.toString()+
             "RaytracerDFEView";
   }
}








