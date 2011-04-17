import java.awt.*;

/**
 *  RaytracerDFETriangle.java
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

public class RaytracerDFETriangle extends DisplayFrameElement{
   protected int[] tmpx= new int[3];
   protected int[] tmpy= new int[3];

   public RaytracerDFETriangle(T3d p1, T3d p2, T3d p3, T3d lookp, Color c){
      super(c);
      tmp_world= new T3d[3];
      tmp_world[0]= (T3d) p1.clone(); 
      tmp_world[1]= (T3d) p2.clone(); 
      tmp_world[2]= (T3d) p3.clone();
      if(lookp!=null){
         Utils.tran3d(tmp_world[0], lookp);
         Utils.tran3d(tmp_world[1], lookp);
         Utils.tran3d(tmp_world[2], lookp);
      }
      size=3;
      //System.out.println("RaytracerDFETriangle, p1:"+ tmp_world[2].toString() );
   }

/*   public RaytracerDFETriangle(T3d p1, T3d p2, T3d p3, T3d lookp,
            TColor a, TColor d, TColor s, double c, double r, double t){
      super(a,d,s, c,r,t);
      tmp_world= new T3d[3];
      tmp_world[0]= (T3d) p1.clone(); 
      tmp_world[1]= (T3d) p2.clone(); 
      tmp_world[2]= (T3d) p3.clone();
      if(lookp!=null){
         Utils.tran3d(tmp_world[0], lookp);
         Utils.tran3d(tmp_world[1], lookp);
         Utils.tran3d(tmp_world[2], lookp);
      }
      size=3;
   }
*/
   /**
    *  draw screen points on canvas.
    */
   public void draw(Graphics g, Point[] p){
      tmpx[0]= p[0].x; tmpx[1]= p[1].x; tmpx[2]= p[2].x;
      tmpy[0]= p[0].y; tmpy[1]= p[1].y; tmpy[2]= p[2].y;
      g.drawPolygon(tmpx,tmpy, 3);
   }

   public String toString(){
      return super.toString()+
             "\nRaytracerDFETriangle\n";
   }
}

