import java.awt.*;

/**
 *  Utils.java
 *
 *  my un-sorted utilities.
 *
 *  provides:
 *  debugging timers,
 *  thread delays.
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public class Utils{
   private static long[] time= new long[10];
   private static int timer_counter=0;

   /** 
    *  store the current time, can store up to 10 times.
    */
   public final static void set_timer(){
      time[timer_counter]= System.currentTimeMillis();
      timer_counter++;
   }

   /** 
    *  clears all timer values stored
    */
   public final static void reset_timer(){
      timer_counter=0; 
   }

   /** 
    *  returns time difference between times i and j if i,j exist and j>i.
    *  @param i i_th time stored
    *  @param j j_th time stored
    */
   public final static long timer_dif(int i, int j){
      if( (timer_counter>=i) && (timer_counter>=j) && (i<j) ){
         return time[j]-time[i];
      }else{ return -1; }
   }

   /**
    *  halt execution for specified milliseconds.
    *  @param milli milliseconds to suspend program execution
    */
   public final static void pause(int milli){
      try{
         Thread.currentThread().sleep(milli);
      }catch(InterruptedException e){}
   }

   /**
    *  halt execution for specified milliseconds, and
    *  0-999999 additional nanoseconds.
    */
   public final static void pause(int milli, int nano){
      try{
         Thread.currentThread().sleep(milli,nano);
      }catch(InterruptedException e){}
   }

   /** 
    *  return current time in milliseconds.
    */
   public final static long get_time(){
      return System.currentTimeMillis();
   }

   /** 
    *  return -1 or 1, depending on pos or neg value.
    */
   public final static int sign(double d){
      if(d<0) return -1;
      return 1;
   }

   /**
    *  round to n-th place.
    *  @param num number to be rounded.
    *  @param places number of decimals to keep.
    */
   public final static double round_double(double num, int places){
      if(num>3)  return (int)(num+.5);
      if(num<-3) return (int)(num-.5);
      int i=0;
      for(;i<places;i++) num*=10;
      num= (int)(num+.5);
      for(i=0;i<places;i++) num/=10.0;
      return num;
   }

   /**
    *  3d translation
    */
   public final static void tran3d(T3d p1, T3d modifier){
      p1.x-= modifier.x;
      p1.y-= modifier.y;
      p1.z-= modifier.z;
   }

   /**
    *  rotate around x-axis
    */
   public final static TMatrix3d get_rotate3d_x_mat(double angle){
      return new TMatrix3d(1, 0, 0, 0,
                           0,  Math.cos(angle), Math.sin(angle), 0,
                           0, -Math.sin(angle), Math.cos(angle), 0);
   }

   /**
    *  rotate around y-axis
    */
   public final static TMatrix3d get_rotate3d_y_mat(double angle){
      return new TMatrix3d(Math.cos(angle), 0, -Math.sin(angle), 0,
                           0, 1, 0, 0,
                           Math.sin(angle), 0,  Math.cos(angle), 0);
   }

   /**
    *  rotate around z-axis
    */
   public final static TMatrix3d get_rotate3d_z_mat(double angle){
      return new TMatrix3d(Math.cos(angle), Math.sin(angle), 0, 0,
                          -Math.sin(angle), Math.cos(angle), 0, 0,
                           0, 0, 1, 0);
   }

   /**
    *  3d scaling
    */
   public final static T3d[] scale3d(T3d[] points, int n, T3d scale){
      return null;
   }

   /**
    *  
    */
   public final static T3d spherical_to_coord(double rho, double phi, double theta){
      return new T3d(rho * Math.sin(phi) * Math.cos(theta),
                     rho * Math.sin(phi) * Math.sin(theta),
                     rho * Math.cos(phi) );
   }

   /**
    *  convert point to rho, phi, theta. assume for origin of (0,0,0).
    */
   public final static T3d coord_to_spherical(T3d p){
      if(p.x==0 || p.z==0) return new T3d(0,0,0);
      else return new T3d(Math.sqrt(p.x*p.x + p.y*p.y + p.z*p.z),
                          Math.atan(Math.sqrt(p.x*p.x+p.y*p.y)/p.z),
                          Math.atan(p.y/p.x));
   }

   /**
    *   generate world to viewing coordinate transform matrix.
    *<pre>
    *        | -sin(theta)            cos(theta)            0         0   |
    *   Tv = | -cos(phi)*cos(theta)  -cos(phi)*sin(theta)   sin(phi)  0   |
    *        | -sin(phi)*cos(theta)  -sin(phi)*sin(theta)  -cos(phi)  rho |
    *        |  0                     0                     0         1   |
    *</pre>
    */
   public final static TMatrix3d get_view_matrix(double rho, double phi, double theta){
      return new TMatrix3d(-Math.sin(theta), Math.cos(theta), 0, 0,
             Math.cos(phi)*Math.cos(theta), -Math.cos(phi)*Math.sin(theta), Math.sin(phi), 0,
            -Math.sin(phi)*Math.cos(theta), -Math.sin(phi)*Math.sin(theta), -Math.cos(phi), rho);
   }

   /**
    *  return new T3d[], transform points by mat.
    */
   public final static T3d[] do_matrix_tran(T3d[] points, int n, TMatrix3d mat){
      T3d[] p= new T3d[n];
      for(int i=0; i<n; i++){
         p[i]= new T3d(
            mat.mat[0][0]*points[i].x + mat.mat[0][1]*points[i].y + mat.mat[0][2]*points[i].z + mat.mat[0][3],
            mat.mat[1][0]*points[i].x + mat.mat[1][1]*points[i].y + mat.mat[1][2]*points[i].z + mat.mat[1][3],
            mat.mat[2][0]*points[i].x + mat.mat[2][1]*points[i].y + mat.mat[2][2]*points[i].z + mat.mat[2][3]
         );
      }
      return p;
   }

   /**
    *  return new T3d, from points by mat.
    */
   public final static T3d do_matrix_tran_pt(T3d points, TMatrix3d mat){
      return new T3d(
         mat.mat[0][0]*points.x + mat.mat[0][1]*points.y + mat.mat[0][2]*points.z + mat.mat[0][3],
         mat.mat[1][0]*points.x + mat.mat[1][1]*points.y + mat.mat[1][2]*points.z + mat.mat[1][3],
         mat.mat[2][0]*points.x + mat.mat[2][1]*points.y + mat.mat[2][2]*points.z + mat.mat[2][3]
      );
   }

   /**
    *  project 3d coordinates onto 2d plane
    */
   public final static T2d[] do_projection_tran(T3d[] points, int n, double d){
      T2d p[]= new T2d[n];
      for(int i=0; i<n; i++){
         p[i]= new T2d(
            (points[i].z==0) ? 0 : ((points[i].x*d)/points[i].z),
            (points[i].z==0) ? 0 : ((points[i].y*d)/points[i].z)
         );
      }
      return p;
   }

   /**
    *  
    */
   public final static Point[] do_viewport_tran(T2d[] points, int n){
      Point p[]= new Point[n];
      for(int i=0; i<n; i++){
         p[i]= new Point(
            (int)( points[i].x+125 ),
            (int)( points[i].y+125 )
         );
      }
      return p;
   }
}



