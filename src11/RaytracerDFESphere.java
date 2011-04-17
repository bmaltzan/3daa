import java.awt.*;

/**
 *  RaytracerDFESphere.java
 *
 *  a wireframe sphere
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public class RaytracerDFESphere extends DisplayFrameElement{
   private final double DEGREETORADIAN= (Math.PI/180);
   private final int ANGLE= 45;
   private final int POINTS= 26;

   public T3d center;
   public double radius;

   private int[] tmpx= new int[8], tmpy= new int[8];

   public RaytracerDFESphere(T3d c, double r, T3d lookp, Color co){
      super(co);
      center= (T3d)c.clone();
      if(lookp!=null) Utils.tran3d(center, lookp);
      radius= r;
      make_sphere();
      size=POINTS;
  }

/*   public RaytracerDFESphere(T3d c, double r, T3d lookp,
            TColor a, TColor d, TColor s, double c2, double r2, double t){
      super(a,d,s, c2,r2,t);
      center= (T3d)c.clone();
      if(lookp!=null) Utils.tran3d(center, lookp);
      radius= r;
      make_sphere();
      size=POINTS;
  }
*/
   /**
    *  draw screen points on canvas.
    */
   public void draw(Graphics g, Point p[]){
      for(int i=0; i<3; i++){ // draw horizontal rings
         for(int j=0; j<8; j++){
            tmpx[j]= p[i*8+2+j].x;
            tmpy[j]= p[i*8+2+j].y;
         }
         g.drawPolygon(tmpx,tmpy, 8);
      }

      int count, sub=24;
      tmpx[0]= p[0].x;
      tmpy[0]= p[0].y;
      tmpx[4]= p[1].x;
      tmpy[4]= p[1].y;
      for(int i=2; i<6; i++){ // draw vertical rings
         count=1;
         for(int j=0; j<3; j++){
            //System.out.println(count+"\t"+ (j*8+i)+"  "+ (24-(j*8+i))+"\t");
            tmpx[count]=   p[       j*8 +i ].x;
            tmpy[count]=   p[       j*8 +i ].y;
            tmpx[count+4]= p[sub - (j*8 +i)].x;
            tmpy[count+4]= p[sub - (j*8 +i)].y;
            count++; 
         }
         g.drawPolygon(tmpx,tmpy, 8);
         sub+=2;
      }
   }

   public void make_sphere(){ 
      if(tmp_world==null){
         tmp_world= new T3d[POINTS]; 
      }
      T3d[] p= tmp_world;

/*
      p[0]= new T3d(center.x, center.y+radius, center.z);
      p[1]= new T3d(center.x, center.y-radius, center.z);
      double y, radius_xz; 
      int index= 2;
      for(int i=1; i<4; i++){ //vertical
         y=         Math.cos((i*ANGLE)*DEGREETORADIAN) * radius;
         radius_xz= Math.sin((i*ANGLE)*DEGREETORADIAN) * radius;
         for(int j=0; j<360; j+=ANGLE){
            p[index]= new T3d(center.x + Math.cos(j*DEGREETORADIAN) * radius_xz,
                              center.y + y,
                              center.z + Math.sin(j*DEGREETORADIAN) * radius_xz);
            index++;
         }
      } */

/**/
      if(p[0]==null){
         p[0]= new T3d(center.x, center.y+radius, center.z);
         p[1]= new T3d(center.x, center.y-radius, center.z);
         double y, radius_xz; 
         int index= 2;
         for(int i=1; i<4; i++){ //vertical
            y=         Math.cos((i*ANGLE)*DEGREETORADIAN) * radius;
            radius_xz= Math.sin((i*ANGLE)*DEGREETORADIAN) * radius;
            for(int j=0; j<360; j+=ANGLE){
               p[index]= new T3d(center.x + Math.cos(j*DEGREETORADIAN) * radius_xz,
                                 center.y + y,
                                 center.z + Math.sin(j*DEGREETORADIAN) * radius_xz);
               index++;
            }
         }
      }else{
         p[0].x=center.x; p[0].y=center.y+radius; p[0].z=center.z;
         p[1].x=center.x; p[1].y=center.y-radius; p[1].z=center.z;
         double y, radius_xz; 
         int index= 2;
         for(int i=1; i<4; i++){ //vertical
            y=         Math.cos((i*ANGLE)*DEGREETORADIAN) * radius;
            radius_xz= Math.sin((i*ANGLE)*DEGREETORADIAN) * radius;
            for(int j=0; j<360; j+=ANGLE){
               p[index].x= center.x + Math.cos(j*DEGREETORADIAN) * radius_xz;
               p[index].y= center.y + y;
               p[index].z= center.z + Math.sin(j*DEGREETORADIAN) * radius_xz;
               index++;
            }
         }
      }/**/
   }

   public String toString(){
      return super.toString()+
             "RaytracerDFESphere";
   }
}



