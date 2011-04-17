import java.awt.*;

/**
 *  RaytracerDFEBox.java
 *
 *  a wireframe box
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public class RaytracerDFEBox extends DisplayFrameElement{
   public T3d center, width, half=new T3d();

   public RaytracerDFEBox(T3d c, T3d d, T3d lookp, Color co){
      super(co);
      center= (T3d)c.clone();
      if(lookp!=null) Utils.tran3d(center, lookp);
      width= (T3d)d.clone();
      half.x=width.x/2; half.y=width.y/2; half.z=width.z/2;
      make_box();
      size=8;
   }
/*
   public RaytracerDFEBox(T3d c, T3d d, T3d lookp, 
         TColor a, TColor d2, TColor s, double c2, double r, double t){
      super(a,d2,s, c2,r,t);
      center= (T3d)c.clone();
      if(lookp!=null) Utils.tran3d(center, lookp);
      width= (T3d)d.clone();
      half.x=width.x/2; half.y=width.y/2; half.z=width.z/2;
      make_box();
   }
*/   
   /**
    *  draw screen points on canvas.
    */
   public void draw(Graphics g, Point[] p){
      int[] tmpx= new int[4], tmpy= new int[4];

      tmpx[0]= p[0].x; tmpx[1]= p[1].x; tmpx[2]= p[2].x; tmpx[3]= p[3].x;
      tmpy[0]= p[0].y; tmpy[1]= p[1].y; tmpy[2]= p[2].y; tmpy[3]= p[3].y;
      g.drawPolygon(tmpx,tmpy, 4);

      tmpx[0]= p[4].x; tmpx[1]= p[5].x; tmpx[2]= p[6].x; tmpx[3]= p[7].x;
      tmpy[0]= p[4].y; tmpy[1]= p[5].y; tmpy[2]= p[6].y; tmpy[3]= p[7].y;
      g.drawPolygon(tmpx,tmpy, 4);

      g.drawLine(p[0].x,p[0].y, p[4].x,p[4].y);
      g.drawLine(p[1].x,p[1].y, p[5].x,p[5].y);
      g.drawLine(p[2].x,p[2].y, p[6].x,p[6].y);
      g.drawLine(p[3].x,p[3].y, p[7].x,p[7].y);
   }  

   public final void make_box(){
      if(tmp_world==null){
         tmp_world= new T3d[8]; 
         size=8;
      }
      half.x=width.x/2; half.y=width.y/2; half.z=width.z/2;
      T3d[] p= tmp_world;
      if(p[0]==null){
         p[0]= new T3d(center.x-half.x, center.y+half.y, center.z+half.z); 
         p[1]= new T3d(center.x+half.x, center.y+half.y, center.z+half.z); 
         p[2]= new T3d(center.x+half.x, center.y-half.y, center.z+half.z); 
         p[3]= new T3d(center.x-half.x, center.y-half.y, center.z+half.z); 
         p[4]= new T3d(center.x-half.x, center.y+half.y, center.z-half.z); 
         p[5]= new T3d(center.x+half.x, center.y+half.y, center.z-half.z); 
         p[6]= new T3d(center.x+half.x, center.y-half.y, center.z-half.z); 
         p[7]= new T3d(center.x-half.x, center.y-half.y, center.z-half.z);
      }else{
         p[0].x=center.x-half.x; p[0].y=center.y+half.y; p[0].z=center.z+half.z;
         p[1].x=center.x+half.x; p[1].y=center.y+half.y; p[1].z=center.z+half.z;
         p[2].x=center.x+half.x; p[2].y=center.y-half.y; p[2].z=center.z+half.z;
         p[3].x=center.x-half.x; p[3].y=center.y-half.y; p[3].z=center.z+half.z;
         p[4].x=center.x-half.x; p[4].y=center.y+half.y; p[4].z=center.z-half.z;
         p[5].x=center.x+half.x; p[5].y=center.y+half.y; p[5].z=center.z-half.z;
         p[6].x=center.x+half.x; p[6].y=center.y-half.y; p[6].z=center.z-half.z;
         p[7].x=center.x-half.x; p[7].y=center.y-half.y; p[7].z=center.z-half.z;
      } 
   }

   public String toString(){
      return super.toString()+
             "RaytracerDFEBox";
   }
}








