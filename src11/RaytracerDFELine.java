import java.awt.*;

/**
 *  RaytracerDFELine.java
 *
 *  a wireframe line
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public class RaytracerDFELine extends DisplayFrameElement{
   public T3d tmp1, tmp2;

   public RaytracerDFELine(T3d p1, T3d p2, T3d lookpt, Color c){
      super(c);
      tmp1= (T3d)p1.clone();
      tmp2= (T3d)p2.clone();
      if(lookpt!=null){
         Utils.tran3d(tmp1, lookpt);
         Utils.tran3d(tmp2, lookpt);
      }
      tmp_world= make_line();
      size=2;
   }
   
   public void draw(Graphics g, Point[] p){
      if((p[0].x==p[1].x)&&(p[0].y==p[1].y))
         g.fillRect(p[0].x-1,p[0].y-1, 3,3);
      else
         if(Math.abs(p[0].x-p[1].x) > Math.abs(p[0].y-p[1].y)){
            g.drawLine(p[0].x,p[0].y, p[1].x,p[1].y);
            g.drawLine(p[0].x,p[0].y-1, p[1].x,p[1].y-1);
            g.drawLine(p[0].x,p[0].y+1, p[1].x,p[1].y+1);
         }else{
            g.drawLine(p[0].x,p[0].y, p[1].x,p[1].y);
            g.drawLine(p[0].x-1,p[0].y, p[1].x-1,p[1].y);
            g.drawLine(p[0].x+1,p[0].y, p[1].x+1,p[1].y);
         }
      
   }

   private final T3d[] make_line(){
      T3d[] p= new T3d[2];
      p[0]= tmp1; 
      p[1]= tmp2; 
      return p;
   }

   public String toString(){
      return super.toString()+
             "\nRaytracerDFELine\n";
   }
}








