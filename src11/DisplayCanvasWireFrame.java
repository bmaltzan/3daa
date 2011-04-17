import java.awt.*;
import java.awt.event.*;

/**
 *  DisplayCanvasWireFrame.java
 *
 *  uses a 4 parameter viewing system to display wireframe
 *  images on a canvas.
 *
 *  has static methods which are public for use in
 *  world transforms.
 *
 *  this class is passed a <code>DisplayFrameElement</code>,
 *  it transforms the world points to screen <code>Point</code>s,
 *  and calls the dfe's draw function, which understands
 *  the way to connect the points.
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public class DisplayCanvasWireFrame extends DisplayCanvas{
   protected final double DEGREETORADIAN= (Math.PI/180);
   private T3d view_pt= new T3d(400,400,-400);
   private double view_d=10;
   private double rho,phi,theta;

   private T3d[] tmp_world;
   private TMatrix3d tmp_mat;
   private T3d[] tmp_3d;
   private T2d[] tmp_2d;
   private Point[] tmp_point;

   /**
    *  
    */
   public DisplayCanvasWireFrame(Sim s, String initial_string, int display_instance){
      super(s,initial_string,display_instance);
   }

   /**
    *  transform from world to screen coordinates. call elements's
    *  draw method. draw's on the 2nd buffer.
    *  @param dfe one element of a frame's scene to draw.
    */
   public void draw(DisplayFrameElement dfe){
      tmp_world= dfe.get_world_points();
      tmp_3d=tmp_world;
      
      tmp_mat= new TMatrix3d(-1,0,0,0, 0,1,0,0, 0,0,-1,0);
      tmp_3d=  DisplayCanvasWireFrame.do_matrix_tran(tmp_3d, dfe.get_num_points(), tmp_mat);

      tmp_mat= DisplayCanvasWireFrame.get_rotate3d_x_mat(90*DEGREETORADIAN);
      tmp_3d=  DisplayCanvasWireFrame.do_matrix_tran(tmp_3d, dfe.get_num_points(), tmp_mat);

      //tmp_mat= get_rotate3d_y_mat(-90*DEGREETORADIAN);
      //tmp_3d=  DisplayCanvasWireFrame.do_matrix_tran(tmp_3d, dfe.get_num_points(), tmp_mat);

      tmp_mat= get_view_matrix(rho,phi,theta);
      tmp_3d= do_matrix_tran(tmp_3d, dfe.get_num_points(), tmp_mat);
      tmp_2d= do_projection_tran(tmp_3d, dfe.get_num_points(), view_d);
      tmp_point= do_viewport_tran(tmp_2d, dfe.get_num_points());
      dfe.draw(buffer_g, tmp_point);

      //tmp_world= dfe.get_world_points();
      ////tmp_mat= DisplayCanvasWireFrame.get_rotate3d_x_mat(90*DEGREETORADIAN);
      ////tmp_3d=  DisplayCanvasWireFrame.do_matrix_tran(tmp_world, dfe.get_num_points(), tmp_mat);
      //tmp_mat= get_view_matrix(rho,phi,theta);
      //tmp_3d= do_matrix_tran(tmp_world, dfe.get_num_points(), tmp_mat);
      //tmp_2d= do_projection_tran(tmp_3d, dfe.get_num_points(), view_d);
      //tmp_point= do_viewport_tran(tmp_2d, dfe.get_num_points());
      //dfe.draw(buffer_g, tmp_point);
   }

   /**
    *  set camera (x,y,z) location in world coordinates.
    */
   public void set_view_pt(T3d v){ 
      view_pt= (T3d)v.clone();
      T3d tmp= DisplayCanvasWireFrame.coord_to_spherical(v);
      rho= tmp.x;
      phi= tmp.y;
      theta= tmp.z;
   }

   /**
    *  set camera (x,y,z) location in spherical coordinates.
    */
   public void set_view_sph(T3d v){ 
      rho= v.x;
      phi= v.y;
      theta= v.z;
      view_pt= DisplayCanvasWireFrame.spherical_to_coord(v.x,v.y,v.z);
   }

   /**
    *  set viewing 'd'.
    */
   public void set_view_d(double d){  view_d= d; }



   /**
    *   generate world to viewing coordinate transform matrix.
    *<pre>
    *        | -sin(theta)            cos(theta)            0         0   |
    *   Tv = | -cos(phi)*cos(theta)  -cos(phi)*sin(theta)   sin(phi)  0   |
    *        | -sin(phi)*cos(theta)  -sin(phi)*sin(theta)  -cos(phi)  rho |
    *        |  0                     0                     0         1   |
    *</pre>
    *  @param rho distance from world (0,0,0).
    *  @param phi angle down from z-axis.
    *  @param theta rotation about z-axis, rotate right / left.
    */
   private final TMatrix3d get_view_matrix(double rho, double phi, double theta){
      return new TMatrix3d(
            -Math.sin(theta),                Math.cos(theta), 0, 0,
            -Math.cos(phi)*Math.cos(theta), -Math.cos(phi)*Math.sin(theta), Math.sin(phi), 0,
            -Math.sin(phi)*Math.cos(theta), -Math.sin(phi)*Math.sin(theta), -Math.cos(phi), rho);
   }


   /**
    *  project 3d coordinates onto 2d plane
    */
   private final T2d[] do_projection_tran(T3d[] points, int n, double d){
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
    *  translate 2d projected viewing points, to screen coordinates.
    */
   private final Point[] do_viewport_tran(T2d[] points, int n){
      Point p[]= new Point[n];
      for(int i=0; i<n; i++){
         p[i]= new Point(
            (int)( points[i].x+125 ),
            (int)( points[i].y+125 )
         );
      }
      return p;
   }

   //----------------------------------------------------
   //  these are public, only projection methods are not.
   //    if methods can be used for world transforms, they
   //    should be available
   //----------------------------------------------------

   /**
    *  rotate around x-axis.
    *  @param angle angle of rotation in radians.
    */
   public final static TMatrix3d get_rotate3d_x_mat(double angle){
      return new TMatrix3d(1, 0, 0, 0,
                           0,  Math.cos(angle), Math.sin(angle), 0,
                           0, -Math.sin(angle), Math.cos(angle), 0);
   }

   public final static TMatrix3d get_rotate3d_y_mat(double angle){
      return new TMatrix3d(Math.cos(angle), 0, -Math.sin(angle), 0,
                           0,               1, 0,                0,
                           Math.sin(angle), 0, Math.cos(angle),  0);
   }

   public final static TMatrix3d get_rotate3d_z_mat(double angle){
      return new TMatrix3d(Math.cos(angle),  Math.sin(angle), 0, 0,
                           -Math.sin(angle), Math.cos(angle), 0, 0,
                           0,                0,               1, 0);
   }


   /**
    *  scaling.
    */
   public final static TMatrix3d get_scale3d_mat(double x, double y, double z){
      return new TMatrix3d(x, 0, 0, 0,
                           0, y, 0, 0,
                           0, 0, z, 0);
   }

   /**
    *  translation.
    */
   public final static TMatrix3d get_tran3d_mat(double x, double y, double z){
      return new TMatrix3d(1, 0, 0, x,
                           0, 1, 0, y,
                           0, 0, 1, z);
   }


   /**
    *  
    */
   public static final T3d spherical_to_coord(double rho, double phi, double theta){
      return new T3d(rho * Math.sin(phi) * Math.cos(theta),
                     rho * Math.sin(phi) * Math.sin(theta),
                     rho * Math.cos(phi) );
   }

   /**
    *  convert point to rho, phi, theta. assume for origin of (0,0,0).
    */
   public static final T3d coord_to_spherical(T3d p){
      if(p.x==0 || p.z==0) return new T3d(0,0,0);
      else return new T3d(Math.sqrt(p.x*p.x + p.y*p.y + p.z*p.z),
                          Math.atan(Math.sqrt(p.x*p.x+p.y*p.y)/p.z),
                          Math.atan(p.y/p.x));
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

   
}

