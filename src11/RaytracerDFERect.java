import java.awt.*;

/**
 *  RaytracerDFERect.java
 *
 *  a wireframe triangle
 *
 *  DisplayFrameElement has: T3d[] tmp_world; int size=0;
 *  RaytracerDFE has: TColor ambiant,diffuse,specular; double coef,refl,tran;
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public class RaytracerDFERect extends DisplayFrameElement{
   protected int[] tmpx= new int[4];
   protected int[] tmpy= new int[4];

   public RaytracerDFERect(T3d p1, T3d p2, T3d p3, T3d p4, T3d lookp, Color c){
      super(c);
      tmp_world= new T3d[4];
      tmp_world[0]= (T3d) p1.clone(); 
      tmp_world[1]= (T3d) p2.clone(); 
      tmp_world[2]= (T3d) p3.clone();
      tmp_world[3]= (T3d) p4.clone();
      if(lookp!=null){
         Utils.tran3d(tmp_world[0], lookp);
         Utils.tran3d(tmp_world[1], lookp);
         Utils.tran3d(tmp_world[2], lookp);
         Utils.tran3d(tmp_world[3], lookp);
      }
      size=4;
   }

   /**
    *  draw screen points on canvas.
    */
   public void draw(Graphics g, Point[] p){
      tmpx[0]= p[0].x; tmpx[1]= p[1].x; tmpx[2]= p[2].x; tmpx[3]= p[3].x;
      tmpy[0]= p[0].y; tmpy[1]= p[1].y; tmpy[2]= p[2].y; tmpy[3]= p[3].y;
      g.drawPolygon(tmpx,tmpy, 4);
   }

   public String toString(){
      return super.toString()+
             "\nRaytracerDFERect\n";
   }
}

