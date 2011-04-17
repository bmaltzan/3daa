/**
 *  TMatrix3d.java
 *
 *  hold a 3x4 matrix
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public final class TMatrix3d implements Cloneable{
   public double[][] mat;

   public TMatrix3d(){ mat= new double[3][4]; }
   public TMatrix3d(double a,double b,double c, double d,
              double e,double f,double g, double h,
              double i,double j,double k, double l){
      mat= new double[3][4];
      mat[0][0]=a; mat[0][1]=b; mat[0][2]=c; mat[0][3]=d; 
      mat[1][0]=e; mat[1][1]=f; mat[1][2]=g; mat[1][3]=h; 
      mat[2][0]=i; mat[2][1]=j; mat[2][2]=k; mat[2][3]=l; 
   }
   public Object clone(){
      Object o=null;
      try{
         o= super.clone();
      }catch(CloneNotSupportedException e){
         System.out.println("clone error: Tmatrix3d");
      }
      return o;
   }
   public String toString(){
      return "TMatrix3d:["+
             Utils.round_double(mat[0][0],2)+ ","+
             Utils.round_double(mat[0][1],2)+ ","+
             Utils.round_double(mat[0][2],2)+ ","+
             Utils.round_double(mat[0][3],2)+ "]["+
             Utils.round_double(mat[1][0],2)+ ","+
             Utils.round_double(mat[1][1],2)+ ","+
             Utils.round_double(mat[1][2],2)+ ","+
             Utils.round_double(mat[1][3],2)+ "]["+
             Utils.round_double(mat[2][0],2)+ ","+
             Utils.round_double(mat[2][1],2)+ ","+
             Utils.round_double(mat[2][2],2)+ ","+
             Utils.round_double(mat[2][3],2)+ "]\n";
   }
}

