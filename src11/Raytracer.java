import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 *  Raytracer.java
 *
 *
 *  an implementation of a raytracer
 *
 *  includes methods to:
 *    render a scene,
 *    record an animation of a rendering,
 *    draw frames of an animation.
 *
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public class Raytracer extends Algorithm{

   //-------------------------------  o b j e c t s ----------
   abstract class TObject{
      TColor ambiant= new TColor();
      TColor diffuse= new TColor();
      TColor specular= new TColor();
      double coef; //specular coef
      double refl; //reflection 0-1
      double tran; //transparency 0-1

      public void set_colors(double ar,double ag,double ab, 
                     double dr,double dg,double db, 
                     double sr,double sg,double sb,
                     double c, double r, double t){
         ambiant.r=ar; ambiant.g=ag; ambiant.b=ab;
         diffuse.r=dr; diffuse.g=dg; diffuse.b=db;
         specular.r=sr; specular.g=sg; specular.b=sb;
         coef=c; refl=r; tran=t;
      }
      public abstract double objint(T3d a,T3d b);
      public abstract void objnrm(T3d a,T3d b);
      public String toString(){
         return "ambiant: "+ambiant+
                "diffuse: "+diffuse+
                "specular: "+specular+
                "coef: "+coef+  "\trefl: "+refl+  "\ttran: "+tran+  "\n";
      }
   }
   final class OSphere extends TObject{
      double r, x,y,z;

      public OSphere(double r1, double x1,double y1,double z1){
         r=r1; x=x1; y=y1; z=z1;
      }
      public double objint(T3d pos, T3d ray){
         double b,t,s=0, xadj,yadj,zadj;

         xadj= pos.x-x; //translate ray origin to object's space
         yadj= pos.y-y;
         zadj= pos.z-z;

         b= xadj*ray.x + yadj*ray.y + zadj*ray.z; //solve quadratic
         if(b< -1e50) b= -1e50;
         t= b*b - xadj*xadj - yadj*yadj - zadj*zadj + r*r;
         if(t<0) return 0;

         s= -b - Math.sqrt(t); //try smaller solution
         if(s>0) return s;
         s= -b + Math.sqrt(t); //try larger solution
         if(s>0) return s;
         return 0;          //both solutions <= 0
      }
      public void objnrm(T3d pos,T3d nrm){
         nrm.x= (pos.x-x)/r;
         nrm.y= (pos.y-y)/r;
         nrm.z= (pos.z-z)/r;
      }
      public String toString(){
         return "Sphere:\n"+
                "x: "+x+  "\ty: "+y+  "\tz: "+z+  "\tr: "+r+  "\n"+ 
                super.toString();
      }
   }
   final class OBox extends TObject{
      int sidehit;
      double x,y,z;     //center
      double xs,ys,zs;  //size of sides
      final double FAR_AWAY= 99.99E+20;

      public OBox(double x1,double y1,double z1,
                  double xs1,double ys1,double zs1){
         x=x1; y=y1; z=z1; xs=xs1; ys=ys1; zs=zs1;
      }
      public double objint(T3d pos, T3d ray){
         double s,ss, xhit,yhit,zhit;
         double xadj,yadj,zadj;

         ss= FAR_AWAY;
         xadj= pos.x-x; //translate ray origin to objects space
         yadj= pos.y-y;
         zadj= pos.z-z;

         if(ray.x!=0){      //check x faces
            s= (xs-xadj)/ray.x;
            if((s>0)&&(s<ss)){
               yhit = Math.abs(yadj + s*ray.y);
               zhit = Math.abs(zadj + s*ray.z);
               if((yhit<ys)&&(zhit<zs)){
                  sidehit= 0;
                  ss= s;
            }  }
            s= (-xs-xadj)/ray.x;
            if((s>0)&&(s<ss)){
               yhit= Math.abs(yadj + s*ray.y);
               zhit= Math.abs(zadj + s*ray.z);
               if ((yhit<ys)&&(zhit<zs)){
                  sidehit = 1;
                  ss = s;
         }  }  }
         if(ray.y!=0){      //check y faces
            s= (ys-yadj)/ray.y;
            if((s>0)&&(s<ss)){
               xhit = Math.abs(xadj + s*ray.x);
               zhit = Math.abs(zadj + s*ray.z);
               if((xhit<xs)&&(zhit<zs)){
                  sidehit= 2;
                  ss= s;
            }  }
            s= (-ys-yadj)/ray.y;
            if((s>0)&&(s<ss)){
               xhit= Math.abs(xadj + s*ray.x);
               zhit= Math.abs(zadj + s*ray.z);
               if ((xhit<xs)&&(zhit<zs)){
                  sidehit = 3;
                  ss = s;
         }  }  }
         if(ray.z!=0){      //check z faces
            s= (zs-zadj)/ray.z;
            if((s>0)&&(s<ss)){
               xhit = Math.abs(xadj + s*ray.x);
               yhit = Math.abs(yadj + s*ray.y);
               if((xhit<xs)&&(yhit<ys)){
                  sidehit= 4;
                  ss= s;
            }  }
            s= (-zs-zadj)/ray.z;
            if((s>0)&&(s<ss)){
               xhit= Math.abs(xadj + s*ray.x);
               yhit= Math.abs(yadj + s*ray.y);
               if ((xhit<xs)&&(yhit<ys)){
                  sidehit = 5;
                  ss = s;
         }  }  }
         if (ss==FAR_AWAY) return 0;
         return ss;
      }
      public void objnrm(T3d pos,T3d nrm){
         nrm.x = 0; nrm.y = 0; nrm.z = 0;
         switch (sidehit){
            case(0): nrm.x=  1; break;
            case(1): nrm.x= -1; break;
            case(2): nrm.y=  1; break;
            case(3): nrm.y= -1; break;
            case(4): nrm.z=  1; break;
            case(5): nrm.z= -1; break;
      }  }  
      public String toString(){
         return "box:\n"+
                "x: "+x+  "\ty: "+y+  "\tz: "+z+  "\tsidehit: "+sidehit+  "\n"+ 
                "xs: "+xs+  "\tys: "+ys+  "\tzs: "+zs+  "\n"+ 
                super.toString();
      }
   }
   final class OTriangle extends TObject{
      T3d nrm= new T3d();    
      double d;        //plane constant
      double d1,d2,d3; //plane constants
      T3d e1=new T3d(),e2=new T3d(),e3=new T3d(); //edge vector
      T3d pt1,pt2,pt3;

      public OTriangle(T3d p1, T3d p2, T3d p3){
         pt1=(T3d) p1.clone(); pt2=(T3d) p2.clone(); pt3=(T3d) p3.clone();
         T3d vc1=new T3d(p2.x-p1.x, p2.y-p1.y, p2.z-p1.z),
             vc2=new T3d(p3.x-p2.x, p3.y-p2.y, p3.z-p2.z),
             vc3=new T3d(p1.x-p3.x, p1.y-p3.y, p1.z-p3.z); 

         crossp(nrm,vc1,vc2); //triangle plane
         d= dotp(nrm,p1);
         crossp(e1,nrm,vc1); //edge planes
         d1= dotp(e1,p1);
         crossp(e2,nrm,vc2);
         d2= dotp(e2,p2);
         crossp(e3,nrm,vc3);
         d3= dotp(e3,p3);
      }
      public double objint(T3d pos,T3d ray){
         double s,k;
         T3d point= new T3d();

         k= dotp(nrm,ray); //plane intersection
         if(k==0) return 0;
         s= (d-dotp(nrm,pos))/ k;
         if(s<=0) return 0;

         point.x= pos.x + ray.x*s;
         point.y= pos.y + ray.y*s;
         point.z= pos.z + ray.z*s;

         k= dotp(e1,point) - d1; //edge checks
         if(k<0) return 0;
         k= dotp(e2,point) - d2;
         if(k<0) return 0;
         k= dotp(e3,point) - d3;
         if(k<0) return 0; 
         return s;      
      }
      public void objnrm(T3d pos,T3d normal){
         normal.x= nrm.x;
         normal.y= nrm.y;
         normal.z= nrm.z;
      }
      public String toString(){
         return "triangle:\n"+
                "d: "+d+ "\n"+ 
                "d1: "+d1+  "\td2: "+d2+  "\td3: "+d3+  "\n"+ 
                "nrm: "+nrm+ "e1: "+e1+ "e2: "+e2+ "e3: "+e3+
                super.toString();
      }
   }

   //------------------------------  c l a s s    v a r s  -------
   private final int LIGHTS= 8;
   private final int OBJECTS= 20;
   private final int SCREENWIDTH= 250;
   private final int SCREENHEIGHT= 250;
   private final double ASPECTRATIO= 1.0;
   private final double DEGREETORADIAN= (Math.PI/180); 
   private final double FAR_AWAY= 99.99E+20;

   private double GAMMA= 1.4;

   private int nobject;
   private TObject[] object= new TObject[OBJECTS];

   private int nlight;
   private TLight[] light= new TLight[LIGHTS];

   private int sizex, sizey;       //image sizes
   private T3d eyep, lookp, up;    //viewing
   private double hfov,vfov;       //angular fields of view
   private int level=1,maxlevel=5; //reflection levels
   private TColor background;  

   private int line_y,pixel_x, ri,gi,bi;
   private T3d scrnx=new T3d(),scrny=new T3d(),
               firstray=new T3d(),ray=new T3d();
   private TColor color= new TColor();
   private double dis;

   private double s_litdis;

   //--------------  r a y - t r a c e    c o n s t r u c t o r  -------
   public Raytracer(Sim s){
      super(s);
      scene1();
      viewing(scrnx,scrny,firstray);
      //debug();
   }

   //---------------------------  r a y - t r a c e    s c e n e  --------
   public void trace(int x, int y){
      ray.x= firstray.x + (x * scrnx.x) - (y * scrny.x);
      ray.y= firstray.y + (x * scrnx.y) - (y * scrny.y);
      ray.z= firstray.z + (x * scrnx.z) - (y * scrny.z);
      normalize(ray);
      dis= intersect(-1, eyep, ray, color);
      if(dis>0){
         ri=gammacorrect(color.r);
         gi=gammacorrect(color.g);
         bi=gammacorrect(color.b);
         putpixel(x,y, new Color(ri,gi,bi));
      }else{
         putpixel(x,y,new Color(
            (int)background.r,(int)background.g,(int)background.b));
   }  }

   //----------------------------------------  v i e w i n g  --------
   public void viewing2(){ viewing(scrnx, scrny, firstray); }

   private void viewing(T3d scrnx, T3d scrny, T3d firstray){
      T3d gaze= new T3d();
      double dist, magnitude;

      gaze.x= lookp.x-eyep.x;
      gaze.y= lookp.y-eyep.y;
      gaze.z= lookp.z-eyep.z;
      dist= normalize(gaze);
      crossp(scrnx,gaze,up);
      crossp(scrny,scrnx,gaze);

      dist *=2.0;
      magnitude = dist*Math.tan(hfov*DEGREETORADIAN)/sizex;
      scrnx.x *= magnitude;
      scrnx.y *= magnitude;
      scrnx.z *= magnitude;
      magnitude = dist*Math.tan(vfov*DEGREETORADIAN)/sizey;
      scrny.x *= magnitude;
      scrny.y *= magnitude;
      scrny.z *= magnitude;

      firstray.x = lookp.x - eyep.x;
      firstray.y = lookp.y - eyep.y;
      firstray.z = lookp.z - eyep.z;

      firstray.x += sizey/2*scrny.x - sizex/2*scrnx.x;
      firstray.y += sizey/2*scrny.y - sizex/2*scrnx.y;
      firstray.z += sizey/2*scrny.z - sizex/2*scrnx.z;
   }

   //--------------------------------------  i n t e r s e c t  --------
   private double intersect(int source, T3d pos, T3d ray, TColor color){
      int objhit,objtry;
      double s,ss;
      T3d hit=new T3d(),normal=new T3d();

      objhit=-1;
      ss=FAR_AWAY;
      for(objtry=0; objtry<nobject; objtry++){
         if(objtry!=source){
            s= object[objtry].objint(pos,ray);
            if((s>0.0)&&(s<=ss)){
               objhit=objtry;
               ss=s;
      }  }  }
      if(objhit<0){ return 0; }

      hit.x= pos.x + ss*ray.x;
      hit.y= pos.y + ss*ray.y;
      hit.z= pos.z + ss*ray.z;
      object[objhit].objnrm(hit,normal);
      shade(hit,ray,normal,objhit,color);
      return ss;
   }

   //----------------------------------------  s h a d e  --------
   private void shade(T3d pos,T3d ray,T3d nrm,int obj,TColor color){
      int lnum;
      double k,dis,bright,spec,diffuse;
      T3d refl=new T3d(),ltray=new T3d();
      TColor newcol=new TColor();

      k= -2.0*dotp(ray,nrm); //calculate reflected ray
      refl.x= k*nrm.x + ray.x;
      refl.y= k*nrm.y + ray.y;
      refl.z= k*nrm.z + ray.z;

      color.r= object[obj].ambiant.r; //ambient light contribution
      color.g= object[obj].ambiant.g;
      color.b= object[obj].ambiant.b;

      for (lnum=0; lnum<nlight; lnum++){ //get ray to light
         lightray(lnum,pos,ltray);
         diffuse= dotp(nrm,ltray);
         if(diffuse>0){ //object faces light, add diffuse
            bright= brightness(obj,lnum,pos,ltray);
            diffuse*= bright;
            color.r+= object[obj].diffuse.r * diffuse;
            color.g+= object[obj].diffuse.g * diffuse;
            color.b+= object[obj].diffuse.b * diffuse;

            spec= dotp(refl,ltray);
            if(spec>0){ //highlight is here, add specular
               spec = bright * Math.pow(spec,object[obj].coef);
               color.r+= object[obj].specular.r * spec;
               color.g+= object[obj].specular.g * spec;
               color.b+= object[obj].specular.b * spec;
      }  }  }
      k= object[obj].refl; //reflection
      if((k>0)&&(level<maxlevel)){
         level++;
         dis= intersect(obj,pos,refl,newcol);
         if (dis>0){
            color.r+= newcol.r*k;
            color.g+= newcol.g*k;
            color.b+= newcol.b*k;
         }else{
            color.r+= background.r*k;
            color.g+= background.g*k;
            color.b+= background.b*k;
         }
         level--;
      }
      k= object[obj].tran; //transparency
      if(k>0){
         color.r*= (1-k);
         color.g*= (1-k);
         color.b*= (1-k);
         dis = intersect(obj,pos,ray,newcol);
         if (dis > 0){
            color.r+= newcol.r*k;
            color.g+= newcol.g*k;
            color.b+= newcol.b*k;
         }else{
            color.r+= background.r*k;
            color.g+= background.g*k;
            color.b+= background.b*k;
   }  }  }

   //---------------------------------------  l i g h t i n g  --------
   private void lightray(int lnum,T3d pos,T3d lray){
      lray.x= light[lnum].x-pos.x;
      lray.y= light[lnum].y-pos.y;
      lray.z= light[lnum].z-pos.z;
      s_litdis= normalize(lray);
   }
   private double brightness(int source,int lnum,T3d pos,T3d ray){
      int objtry;
      double s;

      for(objtry=0; objtry<nobject; objtry++){
         if(objtry!=source){ //don't try source
            s= object[objtry].objint(pos,ray);
            if((s>0.0)&&(s<=s_litdis)){
               return(0); //object in shadow
      }  }  }
      return(light[lnum].bright); //object not in shadow
   }
   private int gammacorrect(double intensity){ 
      int ival;
      double dval;

      dval= intensity/255.0;
      if(dval>1.0) dval=1.0;
      if(dval<0.0) dval=0.0;

      dval= Math.exp(Math.log(dval)/GAMMA);
      dval*=255.0;
      ival=(int)(dval+0.5);
      return ival;
   }

   //--------------------------------------  m a t h  --------
   private final double normalize(T3d t){
      double d= Math.sqrt(t.x*t.x + t.y*t.y + t.z*t.z);
      t.x/=d; t.y/=d; t.z/=d;
      return d;
   }
   private final double dotp(T3d a,T3d b){
      return ((a.x*b.x) + (a.y*b.y) + (a.z*b.z));
   }
   private final void crossp(T3d o,T3d a,T3d b){
      double d;
      o.x= a.y*b.z - a.z*b.y;
      o.y= a.z*b.x - a.x*b.z;
      o.z= a.x*b.y - a.y*b.x;
      d= Math.sqrt(o.x*o.x + o.y*o.y + o.z*o.z);
      o.x/= d;
      o.y/= d;
      o.z/= d;
   }

   //----------------------------------------------------
   //  not in code window
   //----------------------------------------------------

   //-------------------  c r e a t e    s c e n e  -----
   public void scene2(){
      eyep=new T3d(500,85,70); lookp=new T3d(0,0,0);
      up=new T3d(0,1,0);
      hfov=20; vfov=20;
      maxlevel=5;
      GAMMA= 1.4;
      sizex=250; sizey=250;
      viewing(scrnx,scrny,firstray);
      
      int i;
      T3d p1=new T3d(),p2=new T3d(),p3=new T3d();

      background= new TColor(0,0,0);  
      nlight=0; nobject=0;
      light[nlight]= new TLight(300,-80,100, .5);
      nlight++;
      light[nlight]= new TLight(300,80,10, 1);
      nlight++;
      for(i=nlight; i<LIGHTS; i++){ light[i]=null; }

      object[nobject]= new OBox(100,0,-50, 5,100,50);
      object[nobject].set_colors
         (0,50,0, 0,100,0, 0,0,0, 0,0,0); //green
      nobject++;

      p1.x=-100; p1.y= 0;   p1.z= 0;
      p2.x= 100; p2.y= 300; p2.z= 150;
      p3.x= 100; p3.y=-400; p3.z= 150;
      object[nobject]= new OTriangle(p1,p2,p3);
      object[nobject].set_colors
         (30,30,30, 0,0,0, 0,0,0, 2,.9,0); //gray, reflective
      nobject++;

      object[nobject]= new OSphere(30, 90,-15,50);
      object[nobject].set_colors
         (250,0,0, 250,0,0, 250,250,250, 10,0,0); //red specular
      nobject++;

      object[nobject]= new OSphere(70, 200,80,0);
      object[nobject].set_colors
         (60,60,0, 200,200,0, 250,250,250, 14,0.1,0.9); //yellow transparent
      nobject++;

      object[nobject]= new OSphere(65, 50,50,-50);
      object[nobject].set_colors
         (0,120,200, 100,100,100, 230,230,230, 10,0.2,0); //grey transparent
      nobject++;

      for(i=nobject; i<OBJECTS; i++){ object[i]=null; }
   }

   public void scene1(){
      eyep=new T3d(480,85,70); lookp=new T3d(0,0,0);//480,85,70  500,85,-30
      up=new T3d(0,1,0);
      hfov=10; vfov=10;
      maxlevel=5;
      GAMMA= 1.4;
      sizex=250; sizey=250;
      viewing(scrnx,scrny,firstray);
      
      int i;
      T3d p1=new T3d(),p2=new T3d(),p3=new T3d();

      background= new TColor(0,0,0);  
      nlight=0; nobject=0;

      light[nlight]= new TLight(100,400,-100, 1);
      nlight++;
      light[nlight]= new TLight(300,20,1, 0.6);
      nlight++;
      light[nlight]= new TLight(-100,20,-100, 1);
      nlight++;
      light[nlight]= new TLight(300,-20,10, 0.6);
      nlight++;

      for(i=nlight; i<LIGHTS; i++){ light[i]=null; }

      object[nobject]= new OBox(170,10,0, 7,10,7);
      object[nobject].set_colors
         (0,50,0, 0,100,0, 30,60,0, 6,0,0.2); //green
      nobject++;

      p1.x=  220; p1.y= 0; p1.z= 0; //top yellow
      p2.x= -130; p2.y= 0; p2.z= -200;
      p3.x= -130; p3.y= 0; p3.z= 200;
      object[nobject]= new OTriangle(p1,p2,p3);
      object[nobject].set_colors
         (80,80,0, 150,120,0, 90,80,20, 2,0.0,0.0); 
      nobject++;

      p1.x=  220; p1.y= 0;    p1.z= 0; // left bottom yellow
      p2.x= -130; p2.y= 0;    p2.z= -200;
      p3.x= -130; p3.y= -200; p3.z= -50;
      object[nobject]= new OTriangle(p1,p2,p3);
      object[nobject].set_colors
         (80,80,0, 150,120,0, 90,80,20, 2,0,0); 
      nobject++;

      p1.x= -130; p1.y= 120; p1.z= 0; // back mirror
      p2.x= -130; p2.y= 0;   p2.z= -200;
      p3.x= -130; p3.y= 0;   p3.z= 200;
      object[nobject]= new OTriangle(p1,p3,p2);
      object[nobject].set_colors
         //(8,38,88, 8,38,88, 8,38,88, 2,0.8,0);
         (0,0,0, 0,0,0, 0,0,0, 2,0.8,0);
      nobject++;

      p1.x= 220;  p1.y= 0;    p1.z= 0; // right bottom yellow
      p2.x= -130; p2.y= 0;    p2.z= 200;
      p3.x= -130; p3.y= -200; p3.z= -50;
      object[nobject]= new OTriangle(p1,p2,p3);
      object[nobject].set_colors
         (80,80,0, 150,120,0, 90,80,20, 2,0,0); 
      nobject++;

      object[nobject]= new OSphere(15, -22,25,60);
      object[nobject].set_colors
         (255,0,0, 0,255,0, 0,0,255, 15,0.8,0); //transparent
         //(90,90,90, 230,230,230, 110,110,110, 15,0.8,0); //transparent
      nobject++;

      object[nobject]= new OSphere(25, 30,55,0);
      object[nobject].set_colors
         (90,90,90, 100,100,130, 230,230,230, 10,0,0.9); //grey reflective
      nobject++;

      object[nobject]= new OSphere(13, 45,20,-60);
      object[nobject].set_colors
         (70,5,10, 100,10,20, 70,20,40, 6,0,0.2); //red
      nobject++;

      for(i=nobject; i<OBJECTS; i++){ object[i]=null; }
   }

   public void execute(){
      Graphics g= sim.get_display1_canvas().get_graphics();
      int tf_counter=250*250;
      for(line_y=0;line_y<sizey;line_y++){
         for(pixel_x=0;pixel_x<sizex;pixel_x++){
            if( ((tf_counter--)%500)==1) sim.draw_text(tf_counter+"");
            ray.x= firstray.x + (pixel_x * scrnx.x) - (line_y * scrny.x);
            ray.y= firstray.y + (pixel_x * scrnx.y) - (line_y * scrny.y);
            ray.z= firstray.z + (pixel_x * scrnx.z) - (line_y * scrny.z);
            normalize(ray);
            dis= intersect(-1, eyep, ray, color);
            if(dis>0){
               ri=gammacorrect(color.r);
               gi=gammacorrect(color.g);
               bi=gammacorrect(color.b);
               g.setColor(new Color(ri,gi,bi));
               g.drawLine(pixel_x,line_y, pixel_x,line_y);
            }else{
               g.setColor(new Color((int)background.r,(int)background.g,(int)background.b));
               g.drawLine(pixel_x,line_y, pixel_x,line_y);
            }
         //if(line_y%500==0) sim.get_display1_canvas().repaint();
      }  }
      sim.get_display1_canvas().repaint();
   }

   private void putpixel(int x,int y,Color c){
      Graphics g= sim.get_display1_canvas().get_graphics();
      g.setColor(c);
      g.drawLine(x,y, x,y);
      sim.get_display1_canvas().repaint();
   }

   //----------------------------------------------------
   //  vcr interface
   //----------------------------------------------------

   public void draw_frame(AlgFrame frame){
      if(frame==null){}
      else{
         sim.draw_text(frame.get_text());

         int code_line= frame.get_code_line();
         if(code_line>=0){ sim.get_code_panel().draw_code(code_line); }

         DisplayCanvasWireFrame canvas= (DisplayCanvasWireFrame) sim.get_display2_canvas();
         RaytracerFrame rf=(RaytracerFrame)frame;
         DisplayFrame display_static= rf.get_static_frame2();
         DisplayFrame display2= rf.get_display_frame2();
         DisplayFrameElement dfe;
         if( display2!=null || display_static!=null ){
            canvas.reset();
            canvas.set_view_pt(rf.get_view_pt());
            canvas.set_view_d(rf.get_view_d());
         }

         if(display_static!=null){ //draw static scene data first.
            canvas.get_graphics().setColor(Color.white);
            display_static.first(); //reset for reverse & replay
            dfe= display_static.next();
            while(dfe!=null){
               if(dfe.color==null) canvas.get_graphics().setColor(Color.white);
               else                canvas.get_graphics().setColor(dfe.color);
               canvas.draw(dfe);
               dfe= display_static.next();
         }  }

         if(display2!=null){ //draw interesting data over static data
            display2.first();
            dfe= display2.next();
            while(dfe!=null){
               if(dfe.color==null) canvas.get_graphics().setColor(Color.white);
               else                canvas.get_graphics().setColor(dfe.color);
               canvas.draw(dfe);
               dfe= display2.next();
         }  }  
         canvas.repaint();
   }  }

   private AlgTape recording;
   private T3d view_pt;
   private double view_d; // 4th parameter of viewing transform (rho,phi,theta,d)
   public AlgTape record_scene(Point p){
      recording= new AlgTape();
      return recording;
   }

   public AlgTape record(Point p){
      recording= new AlgTape();
      add_scene();
      current_lookp= (T3d)lookp.clone();
      view_pt= (T3d)eyep.clone();
      view_pt.x*=100;view_pt.y*=100;view_pt.z*=100;
      view_d= (Math.max(view_pt.x,Math.max(view_pt.y,view_pt.z)))*.5;
      trace2(p.x, p.y);
      return recording;
   }

   /**
    *  bounding box check. used to make sure vectors that do not intersect
    *    an object are sufficiently extended past all objects in the scene.
    */
   private void bounds_check(T3d neg, T3d pos, T3d pt){     
      if(pt.x<neg.x) neg.x=pt.x;
      if(pt.y<neg.y) neg.y=pt.y;
      if(pt.z<neg.z) neg.z=pt.z;
      if(pt.x>pos.x) pos.x=pt.x;
      if(pt.y>pos.y) pos.y=pt.y;
      if(pt.z>pos.z) pos.z=pt.z;
   }

   //private T3d scene_pos_bounds= new T3d(), // positive and negative bounds of the current
   //            scene_neg_bounds= new T3d(); //   object in the world. for recording vector line length. 
   private DisplayFrame static_scene_data; // data in every frame of the recordings... store once.
   private void add_scene(){
      static_scene_data= new DisplayFrame();
      for(int i=0; i<nobject; i++){
         if(object[i] instanceof OTriangle){
            //System.out.println("raytracer702, triangle added to static");
            OTriangle t= (OTriangle)object[i];
            //System.out.println("raytracer702, t.p1:"+ t.pt3.toString() );
            static_scene_data.append(new RaytracerDFETriangle(t.pt1,t.pt2,t.pt3, lookp,null));
            //bounds_check(scene_neg_bounds,scene_pos_bounds,t.pt1);
            //bounds_check(scene_neg_bounds,scene_pos_bounds,t.pt2);
            //bounds_check(scene_neg_bounds,scene_pos_bounds,t.pt3);
         }
         if(object[i] instanceof OBox){
            //System.out.println("raytracer702, box added to static");
            OBox b= (OBox)object[i];
            static_scene_data.append(new RaytracerDFEBox(new T3d(b.x,b.y,b.z), new T3d(b.xs,b.ys,b.zs), lookp,null));
            //T3d tmp= new T3d(b.x+(b.xs/2), b.y+(b.ys/2), b.z+(b.zs/2));
            //bounds_check(scene_neg_bounds,scene_pos_bounds,tmp);
            //tmp.x= b.x-(b.xs/2); tmp.y= b.y+(b.ys/2); tmp.z= b.z+(b.zs/2);
            //bounds_check(scene_neg_bounds,scene_pos_bounds,tmp);
         }
         if(object[i] instanceof OSphere){
            //System.out.println("raytracer702, sphere added to static");
            OSphere s= (OSphere)object[i];
            static_scene_data.append(new RaytracerDFESphere(new T3d(s.x,s.y,s.z), s.r, lookp,null));
            //T3d tmp= new T3d(s.x+s.r, s.y+s.r, s.z+s.r);
            //bounds_check(scene_neg_bounds,scene_pos_bounds,tmp);
            //tmp.x= s.x-s.r; tmp.y= s.y-s.r; tmp.z= s.z-s.r;
            //bounds_check(scene_neg_bounds,scene_pos_bounds,tmp);
         }
      }
      for(int i=0; i<nlight; i++){
         T3d l= new T3d(light[i].x, light[i].y, light[i].z); 
         static_scene_data.append(new RaytracerDFELine(l, l, lookp,LIGHT_COLOR));
      }

      // firstray is top-left vector. get other vectors from this. 
      T3d r1= (T3d) firstray.clone();
      T3d r2= new T3d(firstray.x + (249*scrnx.x),
                      firstray.y + (249*scrnx.y),
                      firstray.z + (249*scrnx.z));
      T3d r3= new T3d(firstray.x + (249*scrnx.x) - (249*scrny.x),
                      firstray.y + (249*scrnx.y) - (249*scrny.y),
                      firstray.z + (249*scrnx.z) - (249*scrny.z));
      T3d r4= new T3d(firstray.x                 - (249*scrny.x),
                      firstray.y                 - (249*scrny.y),
                      firstray.z                 - (249*scrny.z));
      normalize(r1); normalize(r2); normalize(r3); normalize(r4);
      double percent_eye= (Math.max(eyep.x, Math.max(eyep.y,eyep.z) ))* .3;
      r1.x*=percent_eye; r1.y*=percent_eye; r1.z*=percent_eye;
      r2.x*=percent_eye; r2.y*=percent_eye; r2.z*=percent_eye;
      r3.x*=percent_eye; r3.y*=percent_eye; r3.z*=percent_eye;
      r4.x*=percent_eye; r4.y*=percent_eye; r4.z*=percent_eye;
      r1.x= eyep.x+r1.x; r1.y= eyep.y+r1.y; r1.z= eyep.z+r1.z;
      r2.x= eyep.x+r2.x; r2.y= eyep.y+r2.y; r2.z= eyep.z+r2.z;
      r3.x= eyep.x+r3.x; r3.y= eyep.y+r3.y; r3.z= eyep.z+r3.z;
      r4.x= eyep.x+r4.x; r4.y= eyep.y+r4.y; r4.z= eyep.z+r4.z;
      static_scene_data.append(new RaytracerDFELine(eyep, eyep,  lookp,SCREEN_IMAGE));
      static_scene_data.append(new RaytracerDFERect(r1,r2,r3,r4, lookp,SCREEN_IMAGE));
   }

   private T3d current_lookp;
   private DisplayFrame dynamic_scene_data;
   /**
    *  hacked.
    *    in the wireframe canvas, when a ray intersects an object,
    *    the focus should be the object, not the origin. the
    *    static_scene_data will be set to null, and the objects
    *    will be rellocated to the new origin. 
    */
   private void add_scene2(T3d origin){
      for(int i=0; i<nlight; i++){
         T3d l= new T3d(light[i].x, light[i].y, light[i].z); 
         dynamic_scene_data.append(new RaytracerDFELine(l, l, origin,LIGHT_COLOR));
      }

      // firstray is top-left vector. get other vectors from this. 
      T3d r1= (T3d) firstray.clone();
      T3d r2= new T3d(firstray.x + (249*scrnx.x),
                      firstray.y + (249*scrnx.y),
                      firstray.z + (249*scrnx.z));
      T3d r3= new T3d(firstray.x + (249*scrnx.x) - (249*scrny.x),
                      firstray.y + (249*scrnx.y) - (249*scrny.y),
                      firstray.z + (249*scrnx.z) - (249*scrny.z));
      T3d r4= new T3d(firstray.x                 - (249*scrny.x),
                      firstray.y                 - (249*scrny.y),
                      firstray.z                 - (249*scrny.z));
      normalize(r1); normalize(r2); normalize(r3); normalize(r4);
      double percent_eye= (Math.max(eyep.x, Math.max(eyep.y,eyep.z) ))* .3;
      r1.x*=percent_eye; r1.y*=percent_eye; r1.z*=percent_eye;
      r2.x*=percent_eye; r2.y*=percent_eye; r2.z*=percent_eye;
      r3.x*=percent_eye; r3.y*=percent_eye; r3.z*=percent_eye;
      r4.x*=percent_eye; r4.y*=percent_eye; r4.z*=percent_eye;
      r1.x= eyep.x+r1.x; r1.y= eyep.y+r1.y; r1.z= eyep.z+r1.z;
      r2.x= eyep.x+r2.x; r2.y= eyep.y+r2.y; r2.z= eyep.z+r2.z;
      r3.x= eyep.x+r3.x; r3.y= eyep.y+r3.y; r3.z= eyep.z+r3.z;
      r4.x= eyep.x+r4.x; r4.y= eyep.y+r4.y; r4.z= eyep.z+r4.z;
      dynamic_scene_data.append(new RaytracerDFELine(eyep, eyep,  lookp,SCREEN_IMAGE));
      dynamic_scene_data.append(new RaytracerDFERect(r1,r2,r3,r4, lookp,SCREEN_IMAGE));


      for(int i=0; i<nobject; i++){
         if(object[i] instanceof OTriangle){
            OTriangle t= (OTriangle)object[i];
            dynamic_scene_data.append(new RaytracerDFETriangle(t.pt1,t.pt2,t.pt3, origin,null));
         }
         if(object[i] instanceof OBox){
            OBox b= (OBox)object[i];
            dynamic_scene_data.append(new RaytracerDFEBox(new T3d(b.x,b.y,b.z), new T3d(b.xs,b.ys,b.zs), origin,null));
         }
         if(object[i] instanceof OSphere){
            OSphere s= (OSphere)object[i];
            dynamic_scene_data.append(new RaytracerDFESphere(new T3d(s.x,s.y,s.z), s.r, origin,null));
   }  }  }

   //----------------------------------------------------
   //  functions repeated for recording data
   //----------------------------------------------------

   private Color RAY_INIT=      Color.red;
   private Color RAY_REFL=      Color.green;
   private Color RAY_TRAN=      Color.cyan;
   private Color RAY_NO_SHADOW= Color.yellow;
   private Color RAY_SHADOW=    Color.yellow;
   private Color SCREEN_IMAGE=  Color.magenta ;
   private Color LIGHT_COLOR=   Color.yellow;

   //-------------------------------  o b j e c t s ----------
   abstract class TObject2{
      TColor ambiant= new TColor();
      TColor diffuse= new TColor();
      TColor specular= new TColor();
      double coef; //specular coef
      double refl; //reflection 0-1
      double tran; //transparency 0-1

      public void set_colors(double ar,double ag,double ab, 
                     double dr,double dg,double db, 
                     double sr,double sg,double sb,
                     double c, double r, double t){
      recording.append(new RaytracerFrame(
            32, null, null,null,null,0));

         ambiant.r=ar; ambiant.g=ag; ambiant.b=ab;
         recording.append(new RaytracerFrame(
               36, "ambiant="+ambiant.toString(), null,null,null,0));

         diffuse.r=dr; diffuse.g=dg; diffuse.b=db;
         recording.append(new RaytracerFrame(
               37, "diffuse="+diffuse.toString(), null,null,null,0));

         specular.r=sr; specular.g=sg; specular.b=sb;
         recording.append(new RaytracerFrame(
               38, "specular="+specular.toString(), null,null,null,0));

         coef=c; refl=r; tran=t;
         recording.append(new RaytracerFrame(
               39, "coef="+coef+", refl="+refl+", tran="+tran, null,null,null,0));
      }
      public abstract double objint(T3d a,T3d b);
      public abstract void objnrm(T3d a,T3d b);
   }
   final class OSphere2 extends TObject2{
      double r, x,y,z;

      public OSphere2(double r1, double x1,double y1,double z1){
      recording.append(new RaytracerFrame(
            47, null, null,null,null,0));

         r=r1; x=x1; y=y1; z=z1;
         recording.append(new RaytracerFrame(
               48, null, null,null,null,0));
      }
      public double objint(T3d pos, T3d ray){
      recording.append(new RaytracerFrame(
            50, null, null,null,null,0));

         double b,t,s=0, xadj,yadj,zadj;
         recording.append(new RaytracerFrame(
               51, null, null,null,null,0));

         xadj= pos.x-x; //translate ray origin to object's space
         recording.append(new RaytracerFrame(
               53, "xadj="+xadj, null,null,null,0));

         yadj= pos.y-y;
         recording.append(new RaytracerFrame(
               54, "yadj="+yadj, null,null,null,0));

         zadj= pos.z-z;
         recording.append(new RaytracerFrame(
               55, "zadj="+zadj, null,null,null,0));
                  
         b= xadj*ray.x + yadj*ray.y + zadj*ray.z; //solve quadratic
         recording.append(new RaytracerFrame(
               57, "b="+b, null,null,null,0));

         if(b< -1e50) b= -1e50;
         recording.append(new RaytracerFrame(
               58, "b="+b, null,null,null,0));

         t= b*b - xadj*xadj - yadj*yadj - zadj*zadj + r*r;
         recording.append(new RaytracerFrame(
               59, "t="+t, null,null,null,0));

         if(t<0) return 0;
         recording.append(new RaytracerFrame(
               60, "t="+t, null,null,null,0));
            
         s= -b - Math.sqrt(t); //try smaller solution
         recording.append(new RaytracerFrame(
               62, "s="+s, null,null,null,0));

         if(s>0) return s;
         recording.append(new RaytracerFrame(
               63, "s="+s, null,null,null,0));

         s= -b + Math.sqrt(t); //try larger solution
         recording.append(new RaytracerFrame(
               64, "s="+s, null,null,null,0));

         if(s>0) return s;
         recording.append(new RaytracerFrame(
               65, "s="+s, null,null,null,0));

         recording.append(new RaytracerFrame(
               66, null, null,null,null,0));
         return 0;          //both solutions <= 0

      }
      public void objnrm(T3d pos,T3d nrm){
      recording.append(new RaytracerFrame(
            68, null, null,null,null,0));

         nrm.x= (pos.x-x)/r;
         recording.append(new RaytracerFrame(
               69, "nrm.x="+nrm.x, null,null,null,0));

         nrm.y= (pos.y-y)/r;
         recording.append(new RaytracerFrame(
               70, "nrm.y="+nrm.y, null,null,null,0));

         nrm.z= (pos.z-z)/r;
         recording.append(new RaytracerFrame(
               71, "nrm.z="+nrm.z, null,null,null,0));

      }
   }
   final class OBox2 extends TObject2{
      int sidehit;
      double x,y,z;     //center
      double xs,ys,zs;  //size of sides
      final double FAR_AWAY= 99.99E+20;

      public OBox2(double x1,double y1,double z1,
                  double xs1,double ys1,double zs1){
         recording.append(new RaytracerFrame(
               79, null, null,null,null,0));
                  
         x=x1; y=y1; z=z1; xs=xs1; ys=ys1; zs=zs1;
         recording.append(new RaytracerFrame(
               81, null, null,null,null,0));

      }
      public double objint(T3d pos, T3d ray){
      recording.append(new RaytracerFrame(
            83, null, null,null,null,0));

         double s,ss, xhit,yhit,zhit;
         recording.append(new RaytracerFrame(
               84, null, null,null,null,0));

         double xadj,yadj,zadj;
         recording.append(new RaytracerFrame(
               85, null, null,null,null,0));


         ss= FAR_AWAY;
         recording.append(new RaytracerFrame(
               87, "ss="+ss, null,null,null,0));

         xadj= pos.x-x; //translate ray origin to objects space
         recording.append(new RaytracerFrame(
               88, "xadj="+xadj, null,null,null,0));

         yadj= pos.y-y;
         recording.append(new RaytracerFrame(
               89, "yadj="+yadj, null,null,null,0));

         zadj= pos.z-z;
         recording.append(new RaytracerFrame(
               90, "zadj="+zadj, null,null,null,0));


         if(ray.x!=0){      //check x faces
         recording.append(new RaytracerFrame(
               92, "ray.x="+ray.x, null,null,null,0));

            s= (xs-xadj)/ray.x;
            recording.append(new RaytracerFrame(
                  93, "s="+s, null,null,null,0));

            if((s>0)&&(s<ss)){
            recording.append(new RaytracerFrame(
                  94, "s="+s, null,null,null,0));

               yhit = Math.abs(yadj + s*ray.y);
               recording.append(new RaytracerFrame(
                     95, "yhit="+yhit, null,null,null,0));

               zhit = Math.abs(zadj + s*ray.z);
               recording.append(new RaytracerFrame(
                     96, "zhit="+zhit, null,null,null,0));

               if((yhit<ys)&&(zhit<zs)){
               recording.append(new RaytracerFrame(
                     97, null, null,null,null,0));

                  sidehit= 0;
                  recording.append(new RaytracerFrame(
                        98, "sidehit="+sidehit, null,null,null,0));

                  ss= s;
                  recording.append(new RaytracerFrame(
                        99, "ss="+ss, null,null,null,0));

            }  }
            s= (-xs-xadj)/ray.x;
            recording.append(new RaytracerFrame(
                  101, "s="+s, null,null,null,0));

            if((s>0)&&(s<ss)){
            recording.append(new RaytracerFrame(
                  102, "s="+s, null,null,null,0));

               yhit= Math.abs(yadj + s*ray.y);
               recording.append(new RaytracerFrame(
                     103, "yhit="+yhit, null,null,null,0));

               zhit= Math.abs(zadj + s*ray.z);
               recording.append(new RaytracerFrame(
                     104, "zhit="+zhit, null,null,null,0));

               if ((yhit<ys)&&(zhit<zs)){
               recording.append(new RaytracerFrame(
                     105, null, null,null,null,0));

                  sidehit = 1;
                  recording.append(new RaytracerFrame(
                        106, "sidehit="+sidehit, null,null,null,0));

                  ss = s;
                  recording.append(new RaytracerFrame(
                        107, "ss="+ss, null,null,null,0));

         }  }  }
         if(ray.y!=0){      //check y faces
         recording.append(new RaytracerFrame(
               109, "ray.y="+ray.y, null,null,null,0));

            s= (ys-yadj)/ray.y;
            recording.append(new RaytracerFrame(
                  110, "s="+s, null,null,null,0));
                         
            if((s>0)&&(s<ss)){
            recording.append(new RaytracerFrame(
                  111, "s="+s, null,null,null,0));

               xhit = Math.abs(xadj + s*ray.x);
               recording.append(new RaytracerFrame(
                     112, "xhit="+xhit, null,null,null,0));

               zhit = Math.abs(zadj + s*ray.z);
               recording.append(new RaytracerFrame(
                     113, "zhit="+zhit, null,null,null,0));

               if((xhit<xs)&&(zhit<zs)){
               recording.append(new RaytracerFrame(
                     114, null, null,null,null,0));

                  sidehit= 2;
                  recording.append(new RaytracerFrame(
                        115, "sidehit="+sidehit, null,null,null,0));

                  ss= s;
                  recording.append(new RaytracerFrame(
                        116, "ss="+ss, null,null,null,0));

            }  }
            s= (-ys-yadj)/ray.y;
            recording.append(new RaytracerFrame(
                  118, "s="+s, null,null,null,0));

            if((s>0)&&(s<ss)){
            recording.append(new RaytracerFrame(
                  119, null, null,null,null,0));

               xhit= Math.abs(xadj + s*ray.x);
               recording.append(new RaytracerFrame(
                     120, "xhit="+xhit, null,null,null,0));

               zhit= Math.abs(zadj + s*ray.z);
               recording.append(new RaytracerFrame(
                     121, "zhit="+zhit, null,null,null,0));

               if ((xhit<xs)&&(zhit<zs)){
               recording.append(new RaytracerFrame(
                     122, null, null,null,null,0));

                  sidehit = 3;
                  recording.append(new RaytracerFrame(
                        123, "sidehit="+sidehit, null,null,null,0));

                  ss = s;
                  recording.append(new RaytracerFrame(
                        124, "ss="+ss, null,null,null,0));

         }  }  }
         if(ray.z!=0){      //check z faces
         recording.append(new RaytracerFrame(
               126, "ray.z="+ray.z, null,null,null,0));

            s= (zs-zadj)/ray.z;
            recording.append(new RaytracerFrame(
                  127, "s="+s, null,null,null,0));

            if((s>0)&&(s<ss)){
            recording.append(new RaytracerFrame(
                  128, null, null,null,null,0));

               xhit = Math.abs(xadj + s*ray.x);
               recording.append(new RaytracerFrame(
                     129, "xhit="+xhit, null,null,null,0));

               yhit = Math.abs(yadj + s*ray.y);
               recording.append(new RaytracerFrame(
                     130, "yhit="+yhit, null,null,null,0));

               if((xhit<xs)&&(yhit<ys)){
               recording.append(new RaytracerFrame(
                     131, null, null,null,null,0));

                  sidehit= 0;
                  recording.append(new RaytracerFrame(
                        132, "sidehit="+sidehit, null,null,null,0));

                  ss= s;
                  recording.append(new RaytracerFrame(
                        133, "ss="+ss, null,null,null,0));

            }  }
            s= (-zs-zadj)/ray.z;
            recording.append(new RaytracerFrame(
                  135, "s="+s, null,null,null,0));

            if((s>0)&&(s<ss)){
            recording.append(new RaytracerFrame(
                  136, null, null,null,null,0));

               xhit= Math.abs(xadj + s*ray.x);
               recording.append(new RaytracerFrame(
                     137, "xhit="+xhit, null,null,null,0));

               yhit= Math.abs(yadj + s*ray.y);
               recording.append(new RaytracerFrame(
                     138, "yhit="+yhit, null,null,null,0));

               if ((xhit<xs)&&(yhit<ys)){
               recording.append(new RaytracerFrame(
                     139, null, null,null,null,0));

                  sidehit = 5;
                  recording.append(new RaytracerFrame(
                        140, "sidehit="+sidehit, null,null,null,0));

                  ss = s;
                  recording.append(new RaytracerFrame(
                        141, "ss="+ss, null,null,null,0));

         }  }  }
         if (ss==FAR_AWAY) return 0;
         recording.append(new RaytracerFrame(
               143, null, null,null,null,0));

         recording.append(new RaytracerFrame(
               144, "ss="+ss, null,null,null,0));
         return ss;
      }
      public void objnrm(T3d pos,T3d nrm){
         recording.append(new RaytracerFrame(
               146, null, null,null,null,0));

         nrm.x = 0.0; nrm.y = 0.0; nrm.z = 0.0;
         recording.append(new RaytracerFrame(
               147, null, null,null,null,0));

         recording.append(new RaytracerFrame(
               148, null, null,null,null,0));
         switch (sidehit){
            case(0): nrm.x=  1.0;
            recording.append(new RaytracerFrame(
                  149, null, null,null,null,0));
                     break;

            case(1): nrm.x= -1.0;
            recording.append(new RaytracerFrame(
                  150, null, null,null,null,0));
                     break;

            case(2): nrm.y=  1.0;
            recording.append(new RaytracerFrame(
                  151, null, null,null,null,0));
                     break;

            case(3): nrm.y= -1.0;
            recording.append(new RaytracerFrame(
                  152, null, null,null,null,0));
                     break;

            case(4): nrm.z=  1.0;
            recording.append(new RaytracerFrame(
                  153, null, null,null,null,0));
                     break;

            case(5): nrm.z= -1.0;
            recording.append(new RaytracerFrame(
                  154, null, null,null,null,0));
                     break;
      }  }  
   }

   final class OTriangle2 extends TObject2{
      T3d nrm= new T3d();    
      double d;        //plane constant
      double d1,d2,d3; //plane constants
      T3d e1=new T3d(),e2=new T3d(),e3=new T3d(); //edge vector

      public OTriangle2(T3d p1, T3d p2, T3d p3){
         recording.append(new RaytracerFrame(
               162, null, null,null,null,0));

         T3d vc1=new T3d(p2.x-p1.x, p2.y-p1.y, p2.z-p1.z);
         recording.append(new RaytracerFrame(
               163, "vc1="+vc1.toString(), null,null,null,0));

         T3d vc2=new T3d(p3.x-p2.x, p3.y-p2.y, p3.z-p2.z);
         recording.append(new RaytracerFrame(
               164, "vc2="+vc2.toString(), null,null,null,0));

         T3d vc3=new T3d(p1.x-p3.x, p1.y-p3.y, p1.z-p3.z); 
         recording.append(new RaytracerFrame(
               165, "vc3="+vc3.toString(), null,null,null,0));

         crossp(nrm,vc1,vc2); //triangle plane
         recording.append(new RaytracerFrame(
               167, "nrm="+nrm.toString(), null,null,null,0));

         d= dotp(nrm,p1);
         recording.append(new RaytracerFrame(
               168, "d="+d, null,null,null,0));

         crossp(e1,nrm,vc1); //edge planes
         recording.append(new RaytracerFrame(
               169, "e1="+e1.toString(), null,null,null,0));

         d1= dotp(e1,p1);
         recording.append(new RaytracerFrame(
               170, "d1="+d1, null,null,null,0));

         crossp(e2,nrm,vc2);
         recording.append(new RaytracerFrame(
               171, "e2="+e2.toString(), null,null,null,0));

         d2= dotp(e2,p2);
         recording.append(new RaytracerFrame(
               172, "d2="+d2, null,null,null,0));

         crossp(e3,nrm,vc3);
         recording.append(new RaytracerFrame(
               173, "e3="+e3.toString(), null,null,null,0));

         d3= dotp(e3,p3);
         recording.append(new RaytracerFrame(
               174, "d3="+d3, null,null,null,0));

      }
      public double objint(T3d pos,T3d ray){
      recording.append(new RaytracerFrame(
            176, null, null,null,null,0));

         double s,k;
         recording.append(new RaytracerFrame(
               177, null, null,null,null,0));

         T3d point= new T3d();
         recording.append(new RaytracerFrame(
               178, null, null,null,null,0));

         k= dotp(nrm,ray); //plane intersection
         recording.append(new RaytracerFrame(
               180, "k="+k, null,null,null,0));

         if(k==0) return 0;
         recording.append(new RaytracerFrame(
               181, "k="+k, null,null,null,0));

         s= (d-dotp(nrm,pos))/ k;
         recording.append(new RaytracerFrame(
               182, "s="+s, null,null,null,0));

         if(s<=0) return 0;
         recording.append(new RaytracerFrame(
               183, "s="+s, null,null,null,0));

         point.x= pos.x + ray.x*s;
         recording.append(new RaytracerFrame(
               185, "point.x="+point.x, null,null,null,0));

         point.y= pos.y + ray.y*s;
         recording.append(new RaytracerFrame(
               186, "point.y="+point.y, null,null,null,0));

         point.z= pos.z + ray.z*s;
         recording.append(new RaytracerFrame(
               187, "point.z="+point.z, null,null,null,0));

         k= dotp(e1,point) - d1; //edge checks
         recording.append(new RaytracerFrame(
               189, "k="+k, null,null,null,0));

         if(k<0) return 0;
         recording.append(new RaytracerFrame(
               190, "k="+k, null,null,null,0));

         k= dotp(e2,point) - d2;
         recording.append(new RaytracerFrame(
               191, "k="+k, null,null,null,0));

         if(k<0) return 0;
         recording.append(new RaytracerFrame(
               192, "k="+k, null,null,null,0));

         k= dotp(e3,point) - d3;
         recording.append(new RaytracerFrame(
               193, "k="+k, null,null,null,0));

         if(k<0) return 0; 
         recording.append(new RaytracerFrame(
               194, "k="+k, null,null,null,0));

         recording.append(new RaytracerFrame(
               195, "s="+s, null,null,null,0));
         return s;      
      }
      public void objnrm(T3d pos,T3d normal){
      recording.append(new RaytracerFrame(
            197, null, null,null,null,0));

         normal.x= nrm.x;
         recording.append(new RaytracerFrame(
               198, "normal.x="+normal.x, null,null,null,0));

         normal.y= nrm.y;
         recording.append(new RaytracerFrame(
               199, "normal.y="+normal.y, null,null,null,0));

         normal.z= nrm.z;
         recording.append(new RaytracerFrame(
               200, "normal.z="+normal.z, null,null,null,0));

      }
   }

   //--------------  r a y - t r a c e    c o n s t r u c t o r  -------
   public void Raytracer2(){
   recording.append(new RaytracerFrame(
         236, null, null,null,null,0));

      //applet= ap;
      recording.append(new RaytracerFrame(
            237, null, null,null,null,0));

      scene1();
      recording.append(new RaytracerFrame(
            238, null, null,null,null,0));

      viewing2(scrnx,scrny,firstray);
      recording.append(new RaytracerFrame(
            239, null, null,null,null,0));
   }

   //---------------------------  r a y - t r a c e    s c e n e  --------
   public void trace2(int x, int y){
   recording.append(new RaytracerFrame(
         260, "x="+x+",  y="+y, null,static_scene_data,view_pt,view_d));

      ray.x= firstray.x + (x * scrnx.x) - (y * scrny.x);
      recording.append(new RaytracerFrame(
            261,"ray.x="+ Utils.round_double(ray.x,2), null,static_scene_data,view_pt,view_d));

      ray.y= firstray.y + (x * scrnx.y) - (y * scrny.y);
      recording.append(new RaytracerFrame(
            262,"ray.y="+ Utils.round_double(ray.y,2), null,static_scene_data,view_pt,view_d));

      ray.z= firstray.z + (x * scrnx.z) - (y * scrny.z);
      recording.append(new RaytracerFrame(
            263,"ray.z="+ Utils.round_double(ray.z,2), null,static_scene_data,view_pt,view_d));

      normalize2(ray);
      recording.append(new RaytracerFrame(
            264, ray.toString(), null,static_scene_data,view_pt,view_d));

      dis= intersect2(-1, eyep, ray, color, RAY_INIT);
      recording.append(new RaytracerFrame(
            265,"dis="+ Utils.round_double(dis,2), null,static_scene_data,view_pt,view_d));

      if(dis>0){
      recording.append(new RaytracerFrame(
            266,"dis="+ Utils.round_double(dis,2), null,static_scene_data,view_pt,view_d));

         ri=gammacorrect2(color.r);
         recording.append(new RaytracerFrame(
               267,"ri="+ ri, null,static_scene_data,view_pt,view_d));

         gi=gammacorrect2(color.g);
         recording.append(new RaytracerFrame(
               268,"gi="+ gi, null,static_scene_data,view_pt,view_d));

         bi=gammacorrect2(color.b);
         recording.append(new RaytracerFrame(
               269,"bi="+ bi, null,static_scene_data,view_pt,view_d));

         Color tmp_color=new Color(ri,gi,bi);
         putpixel(x,y, tmp_color);
         recording.append(new RaytracerFrame(
               270,tmp_color.toString(), null,static_scene_data,view_pt,view_d));

      }else{
      recording.append(new RaytracerFrame(
            271,null, null,static_scene_data,view_pt,view_d));

         Color tmp_color=new Color((int)background.r,(int)background.g,(int)background.b);
         putpixel(x,y,tmp_color);
         recording.append(new RaytracerFrame(
               272,tmp_color.toString(), null,static_scene_data,view_pt,view_d));
   }  }

   //----------------------------------------  v i e w i n g  --------
   private void viewing2(T3d scrnx, T3d scrny, T3d firstray){
   recording.append(new RaytracerFrame(
         277,null, null,null,view_pt,view_d));
            
      T3d gaze= new T3d();
      recording.append(new RaytracerFrame(
            278,null, null,null,view_pt,view_d));


      double dist,magnitude;
      recording.append(new RaytracerFrame(
            279,null, null,null,view_pt,view_d));

      gaze.x= lookp.x-eyep.x;
      recording.append(new RaytracerFrame(
            281,"gaze.x="+gaze.x, null,null,view_pt,view_d));

      gaze.y= lookp.y-eyep.y;
      recording.append(new RaytracerFrame(
            282,"gaze.y="+gaze.y, null,null,view_pt,view_d));

      gaze.z= lookp.z-eyep.z;
      recording.append(new RaytracerFrame(
            283,"gaze.z="+gaze.z, null,null,view_pt,view_d));

      dist= normalize2(gaze);
      recording.append(new RaytracerFrame(
            284,"dist="+dist, null,null,view_pt,view_d));

      crossp2(scrnx,gaze,up);
      recording.append(new RaytracerFrame(
            285,"scrnx="+scrnx.toString(), null,null,view_pt,view_d));

      crossp2(scrny,scrnx,gaze);
      recording.append(new RaytracerFrame(
            286,"scrny="+scrny.toString(), null,null,view_pt,view_d));

      dist *=2.0;
      recording.append(new RaytracerFrame(
            288,"dist="+dist, null,null,view_pt,view_d));

      magnitude = dist*Math.tan(hfov*DEGREETORADIAN)/sizex;
      recording.append(new RaytracerFrame(
            289,"magnitude="+magnitude, null,null,view_pt,view_d));

      scrnx.x *= magnitude;
      recording.append(new RaytracerFrame(
            290,"scrnx.x="+scrnx.x, null,null,view_pt,view_d));

      scrnx.y *= magnitude;
      recording.append(new RaytracerFrame(
            291,"scrnx.y="+scrnx.y, null,null,view_pt,view_d));

      scrnx.z *= magnitude;
      recording.append(new RaytracerFrame(
            292,"scrnx.z="+scrnx.z, null,null,view_pt,view_d));

      magnitude = dist*Math.tan(vfov*DEGREETORADIAN)/sizey;
      recording.append(new RaytracerFrame(
            293,"magnitude="+magnitude, null,null,view_pt,view_d));

      scrny.x *= magnitude;
      recording.append(new RaytracerFrame(
            294,"scrny.x="+scrny.x, null,null,view_pt,view_d));

      scrny.y *= magnitude;
      recording.append(new RaytracerFrame(
            295,"scrny.y="+scrny.y, null,null,view_pt,view_d));

      scrny.z *= magnitude;
      recording.append(new RaytracerFrame(
            296,"scrny.z="+scrny.z, null,null,view_pt,view_d));


      firstray.x = lookp.x - eyep.x;
      recording.append(new RaytracerFrame(
            298,"firstray.x="+firstray.x, null,null,view_pt,view_d));

      firstray.y = lookp.y - eyep.y;
      recording.append(new RaytracerFrame(
            299,"firstray.y="+firstray.y, null,null,view_pt,view_d));

      firstray.z = lookp.z - eyep.z;
      recording.append(new RaytracerFrame(
            300,"firstray.z="+firstray.z, null,null,view_pt,view_d));


      firstray.x += sizey/2*scrny.x - sizex/2*scrnx.x;
      recording.append(new RaytracerFrame(
            302,"firstray.x="+firstray.x, null,null,view_pt,view_d));

      firstray.y += sizey/2*scrny.y - sizex/2*scrnx.y;
      recording.append(new RaytracerFrame(
            303,"firstray.y="+firstray.y, null,null,view_pt,view_d));

      firstray.z += sizey/2*scrny.z - sizex/2*scrnx.z;
      recording.append(new RaytracerFrame(
            304,"firstray.z="+firstray.z, null,null,view_pt,view_d));
   }

   //--------------------------------------  i n t e r s e c t  --------
   private double intersect2(int source, T3d pos, T3d ray, TColor color, Color ray_color){ 
   recording.append(new RaytracerFrame(
         308,null, null,static_scene_data,view_pt,view_d));

      int objhit,objtry;
      recording.append(new RaytracerFrame(
            309,null, null,static_scene_data,view_pt,view_d));

      double s,ss;
      recording.append(new RaytracerFrame(
            310,null, null,static_scene_data,view_pt,view_d));

      T3d hit=new T3d(),normal=new T3d();
      recording.append(new RaytracerFrame(
            311,null, null,static_scene_data,view_pt,view_d));
             
      objhit=-1;
      recording.append(new RaytracerFrame(
            313,null, null,static_scene_data,view_pt,view_d));

      ss=FAR_AWAY;
      recording.append(new RaytracerFrame(
            314,"ss="+ss, null,static_scene_data,view_pt,view_d));

      for(objtry=0; objtry<nobject; objtry++){
      recording.append(new RaytracerFrame(
            315,"objtry="+objtry+", nobject="+nobject, null,static_scene_data,view_pt,view_d));
        
         if(objtry!=source){
         recording.append(new RaytracerFrame(
               316,"objtry="+objtry+", source="+source, null,static_scene_data,view_pt,view_d));

            s= object[objtry].objint(pos,ray);
            recording.append(new RaytracerFrame(
                  317,"s="+s, null,static_scene_data,view_pt,view_d));

            if((s>0.0)&&(s<=ss)){
            recording.append(new RaytracerFrame(
                  318,"s="+s+", ss="+ss, null,static_scene_data,view_pt,view_d));

               objhit=objtry;
               recording.append(new RaytracerFrame(
                     319,"objhit="+objhit, null,static_scene_data,view_pt,view_d));

               ss=s;
               recording.append(new RaytracerFrame(
                     320,"ss="+ss, null,static_scene_data,view_pt,view_d));

      }  }  }
      if(objhit<0){
      recording.append(new RaytracerFrame(
            322,"objhit="+objhit, null,static_scene_data,view_pt,view_d));

         T3d sph2= new T3d();
         crossp(sph2,up,ray); //switched
         T3d spherical= DisplayCanvasWireFrame.coord_to_spherical(view_pt);
         T3d spherical_inc= new T3d(0,0,(sph2.z-spherical.z)*.1);
         T3d tmp_line1= (T3d) pos.clone(),
             tmp_line2= (T3d) pos.clone(),
             line_inc=  (T3d) ray.clone();
         normalize(line_inc); 
         line_inc.x*= 100; line_inc.y*= 100; line_inc.z*= 100;
         for(int i=1;i<=10;i++){
            tmp_line2.x+= line_inc.x; 
            tmp_line2.y+= line_inc.y; 
            tmp_line2.z+= line_inc.z;
            spherical.x+= spherical_inc.x;
            spherical.y+= spherical_inc.y;
            spherical.z+= spherical_inc.z;
            view_pt= DisplayCanvasWireFrame.spherical_to_coord(spherical.x,spherical.y,spherical.z);
            dynamic_scene_data= new DisplayFrame();
            dynamic_scene_data.append(new RaytracerDFELine(tmp_line1, tmp_line2, null,ray_color));
            dynamic_scene_data.append(new RaytracerDFELine(eyep, eyep, null,SCREEN_IMAGE));
            recording.append(new RaytracerFrame(
                  322,"No object intersection, frame "+i+"/10", dynamic_scene_data,static_scene_data, view_pt,view_d));
         }
         return 0; 
      }

      hit.x= pos.x + ss*ray.x;
      recording.append(new RaytracerFrame(
            324,"hit.x="+hit.x, null,static_scene_data,view_pt,view_d));

      hit.y= pos.y + ss*ray.y;
      recording.append(new RaytracerFrame(
            325,"hit.y="+hit.y, null,static_scene_data,view_pt,view_d));

      hit.z= pos.z + ss*ray.z;
      recording.append(new RaytracerFrame(
            326,"hit.z="+hit.z, null,static_scene_data,view_pt,view_d));
      //System.out.println("raytracer:1620, hit:"+ hit.toString() );
      //System.out.println("raytracer:1621, pos:"+ pos.toString() );

      T3d sph2= new T3d();
      crossp(sph2,up,ray);//switched
      T3d spherical= DisplayCanvasWireFrame.coord_to_spherical(view_pt);
      T3d spherical_inc= new T3d(0,0,(sph2.z-spherical.z)*.1);
      T3d tmp_line1= (T3d) pos.clone(),
          tmp_line2= (T3d) pos.clone(),
          line_inc=  new T3d((hit.x-pos.x)*.1,(hit.y-pos.y)*.1,(hit.z-pos.z)*.1),
          focus= (T3d) current_lookp.clone(),
          focus_inc= new T3d((hit.x-focus.x)*.1, (hit.y-focus.y)*.1, (hit.z-focus.z)*.1);
      for(int i=1;i<=10;i++){
         tmp_line2.x+= line_inc.x; 
         tmp_line2.y+= line_inc.y; 
         tmp_line2.z+= line_inc.z;
         //System.out.println("raytracer:1652, tmp_line2:"+ tmp_line2.toString() );
         focus.x+= focus_inc.x;
         focus.y+= focus_inc.y;
         focus.z+= focus_inc.z;
         //System.out.println("raytracer:1656, focus:"+ focus.toString() );
         spherical.x+= spherical_inc.x;
         spherical.y+= spherical_inc.y;
         spherical.z+= spherical_inc.z;
         view_pt= DisplayCanvasWireFrame.spherical_to_coord(spherical.x,spherical.y,spherical.z);
         dynamic_scene_data= new DisplayFrame();
         add_scene2(focus);
         dynamic_scene_data.append(new RaytracerDFELine(tmp_line1, tmp_line2, focus,ray_color));
         dynamic_scene_data.append(new RaytracerDFELine(eyep, eyep,  focus,SCREEN_IMAGE));
         recording.append(new RaytracerFrame(
               326,"Object intersection, frame "+i+"/5", dynamic_scene_data,null, view_pt,view_d));
      }
      focus_inc.x*=-2; focus_inc.y*=-2; focus_inc.z*=-2; //move look point back to origin. must b/c static scene calc'd for origin.
      for(int i=1;i<=5;i++){
         focus.x+= focus_inc.x; 
         focus.y+= focus_inc.y; 
         focus.z+= focus_inc.z;
         dynamic_scene_data= new DisplayFrame();
         add_scene2(focus);
         recording.append(new RaytracerFrame(
               326,"look to origin, frame "+i+"/5", dynamic_scene_data,null, view_pt,view_d));
      }
      
      object[objhit].objnrm(hit,normal);
      recording.append(new RaytracerFrame(
            327,null, null,static_scene_data,view_pt,view_d));

      shade2(hit,ray,normal,objhit,color);
      recording.append(new RaytracerFrame(
            328,null, null,static_scene_data,view_pt,view_d));

      recording.append(new RaytracerFrame(
            329,"ss="+ss, null,static_scene_data,view_pt,view_d));
      return ss;

   }

   //----------------------------------------  s h a d e  --------
   private void shade2(T3d pos,T3d ray,T3d nrm,int obj,TColor color){
   recording.append(new RaytracerFrame(
         333,null, null,static_scene_data,view_pt,view_d));

      int lnum;
      recording.append(new RaytracerFrame(
            334,null, null,static_scene_data,view_pt,view_d));

      double k,dis,bright,spec,diffuse;
      recording.append(new RaytracerFrame(
            335,null, null,static_scene_data,view_pt,view_d));

      T3d refl=new T3d(),ltray=new T3d();
      recording.append(new RaytracerFrame(
            336,null, null,static_scene_data,view_pt,view_d));

      TColor newcol=new TColor();
      recording.append(new RaytracerFrame(
            337,null, null,static_scene_data,view_pt,view_d));


      k= -2.0*dotp2(ray,nrm);
      recording.append(new RaytracerFrame(
            339,"k="+k, null,static_scene_data,view_pt,view_d));

      refl.x= k*nrm.x + ray.x;
      recording.append(new RaytracerFrame(
            340,"refl.x="+refl.x, null,static_scene_data,view_pt,view_d));

      refl.y= k*nrm.y + ray.y;
      recording.append(new RaytracerFrame(
            341,"refl.y="+refl.y, null,static_scene_data,view_pt,view_d));

      refl.z= k*nrm.z + ray.z;
      recording.append(new RaytracerFrame(
            342,"refl.z="+refl.z, null,static_scene_data,view_pt,view_d));


      color.r= object[obj].ambiant.r; //ambient light contribution
      recording.append(new RaytracerFrame(
            344,"color.r="+color.r, null,static_scene_data,view_pt,view_d));

      color.g= object[obj].ambiant.g;
      recording.append(new RaytracerFrame(
            345,"color.g="+color.g, null,static_scene_data,view_pt,view_d));

      color.b= object[obj].ambiant.b;
      recording.append(new RaytracerFrame(
            346,"color.b="+color.b, null,static_scene_data,view_pt,view_d));

      for (lnum=0; lnum<nlight; lnum++){ //get ray to light
      recording.append(new RaytracerFrame(
            348,"lnum="+lnum+", nlight="+nlight, null,static_scene_data,view_pt,view_d));

         lightray2(lnum,pos,ltray);
         recording.append(new RaytracerFrame(
               349,null, null,static_scene_data,view_pt,view_d));

         diffuse= dotp2(nrm,ltray);
         recording.append(new RaytracerFrame(
               350,"diffuse="+diffuse, null,static_scene_data,view_pt,view_d));

         if(diffuse>0){ //object faces light, add diffuse
         recording.append(new RaytracerFrame(
               351,"diffuse="+diffuse, null,static_scene_data,view_pt,view_d));

            bright= brightness2(obj,lnum,pos,ltray);
            recording.append(new RaytracerFrame(
                  352,"bright="+bright, null,static_scene_data,view_pt,view_d));

            diffuse*= bright;
            recording.append(new RaytracerFrame(
                  353,"diffuse="+diffuse, null,static_scene_data,view_pt,view_d));

            color.r+= object[obj].diffuse.r * diffuse;
            recording.append(new RaytracerFrame(
                  354,"color.r="+color.r, null,static_scene_data,view_pt,view_d));

            color.g+= object[obj].diffuse.g * diffuse;
            recording.append(new RaytracerFrame(
                  355,"color.g="+color.g, null,static_scene_data,view_pt,view_d));

            color.b+= object[obj].diffuse.b * diffuse;
            recording.append(new RaytracerFrame(
                  356,"color.b="+color.b, null,static_scene_data,view_pt,view_d));

            spec= dotp2(refl,ltray);
            recording.append(new RaytracerFrame(
                  358,"spec="+spec, null,static_scene_data,view_pt,view_d));

            if(spec>0){ //highlight is here, add specular
            recording.append(new RaytracerFrame(
                  359,"spec="+spec, null,static_scene_data,view_pt,view_d));

               spec = bright * Math.pow(spec,object[obj].coef);
               recording.append(new RaytracerFrame(
                     360,"spec="+spec, null,static_scene_data,view_pt,view_d));

               color.r+= object[obj].specular.r * spec;
               recording.append(new RaytracerFrame(
                     361,"color.r="+color.r, null,static_scene_data,view_pt,view_d));

               color.g+= object[obj].specular.g * spec;
               recording.append(new RaytracerFrame(
                     362,"color.g="+color.g, null,static_scene_data,view_pt,view_d));

               color.b+= object[obj].specular.b * spec;
               recording.append(new RaytracerFrame(
                     363,"color.b="+color.b, null,static_scene_data,view_pt,view_d));

      }  }  }
      k= object[obj].refl; //reflection
      recording.append(new RaytracerFrame(
            365,"k="+k, null,static_scene_data,view_pt,view_d));

      if((k>0)&&(level<maxlevel)){
      recording.append(new RaytracerFrame(
            366,"k="+k+", level="+level+", maxlevel="+maxlevel, null,static_scene_data,view_pt,view_d));

         level++;
         recording.append(new RaytracerFrame(
               367,"level="+level, null,static_scene_data,view_pt,view_d));

         dis= intersect2(obj,pos,refl,newcol,RAY_REFL);
         recording.append(new RaytracerFrame(
               368,"dis="+dis, null,static_scene_data,view_pt,view_d));

         if (dis>0){
         recording.append(new RaytracerFrame(
               369,"dis="+dis, null,static_scene_data,view_pt,view_d));
                  
            color.r+= newcol.r*k;
            recording.append(new RaytracerFrame(
                  370,"color.r="+color.r, null,static_scene_data,view_pt,view_d));

            color.g+= newcol.g*k;
            recording.append(new RaytracerFrame(
                  371,"color.g="+color.g, null,static_scene_data,view_pt,view_d));

            color.b+= newcol.b*k;
            recording.append(new RaytracerFrame(
                  372,"color.b="+color.b, null,static_scene_data,view_pt,view_d));

         }else{
         recording.append(new RaytracerFrame(
               373,null, null,static_scene_data,view_pt,view_d));

            color.r+= background.r*k;
            recording.append(new RaytracerFrame(
                  374,"color.r="+color.r, null,static_scene_data,view_pt,view_d));

            color.g+= background.g*k;
            recording.append(new RaytracerFrame(
                  375,"color.g="+color.g, null,static_scene_data,view_pt,view_d));

            color.b+= background.b*k;
            recording.append(new RaytracerFrame(
                  376,"color.b="+color.b, null,static_scene_data,view_pt,view_d));

         }
         level--;
         recording.append(new RaytracerFrame(
               378,"level="+level, null,static_scene_data,view_pt,view_d));

      }
      k= object[obj].tran; //transparency
      recording.append(new RaytracerFrame(
            380,"k="+k, null,static_scene_data,view_pt,view_d));

      if(k>0){
      recording.append(new RaytracerFrame(
            381,"k="+k, null,static_scene_data,view_pt,view_d));

         color.r*= (1-k);
         recording.append(new RaytracerFrame(
               382,"color.r="+color.r, null,static_scene_data,view_pt,view_d));

         color.g*= (1-k);
         recording.append(new RaytracerFrame(
               383,"color.g="+color.g, null,static_scene_data,view_pt,view_d));

         color.b*= (1-k);
         recording.append(new RaytracerFrame(
               384,"color.b="+color.b, null,static_scene_data,view_pt,view_d));

         dis = intersect2(obj,pos,ray,newcol, RAY_TRAN);
         recording.append(new RaytracerFrame(
               385,"dis="+dis, null,static_scene_data,view_pt,view_d));

         if (dis > 0){
         recording.append(new RaytracerFrame(
               386,"dis="+dis, null,static_scene_data,view_pt,view_d));

            color.r+= newcol.r*k;
            recording.append(new RaytracerFrame(
                  387,"color.r="+color.r, null,static_scene_data,view_pt,view_d));

            color.g+= newcol.g*k;
            recording.append(new RaytracerFrame(
                  388,"color.g="+color.g, null,static_scene_data,view_pt,view_d));

            color.b+= newcol.b*k;
            recording.append(new RaytracerFrame(
                  389,"color.b="+color.b, null,static_scene_data,view_pt,view_d));

         }else{
         recording.append(new RaytracerFrame(
               390,null, null,static_scene_data,view_pt,view_d));

            color.r+= background.r*k;
            recording.append(new RaytracerFrame(
                  391,"color.r="+color.r, null,static_scene_data,view_pt,view_d));

            color.g+= background.g*k;
            recording.append(new RaytracerFrame(
                  392,"color.g="+color.g, null,static_scene_data,view_pt,view_d));

            color.b+= background.b*k;
            recording.append(new RaytracerFrame(
                  393,"color.b="+color.b, null,static_scene_data,view_pt,view_d));

   }  }  }

   //---------------------------------------  l i g h t i n g  --------
   private void lightray2(int lnum,T3d pos,T3d lray){
   recording.append(new RaytracerFrame(
         397,null, null,static_scene_data,view_pt,view_d));

      lray.x= light[lnum].x-pos.x;
      recording.append(new RaytracerFrame(
            398,"lray.x="+lray.x, null,static_scene_data,view_pt,view_d));

      lray.y= light[lnum].y-pos.y;
      recording.append(new RaytracerFrame(
            399,"lray.y="+lray.y, null,static_scene_data,view_pt,view_d));

      lray.z= light[lnum].z-pos.z;
      recording.append(new RaytracerFrame(
            400,"lray.z="+lray.z, null,static_scene_data,view_pt,view_d));

      s_litdis= normalize2(lray);
      recording.append(new RaytracerFrame(
            401,"s_litdis="+s_litdis, null,static_scene_data,view_pt,view_d));

   }
   private double brightness2(int source,int lnum,T3d pos,T3d ray){
   recording.append(new RaytracerFrame(
         403,null, null,static_scene_data,view_pt,view_d));

      int objtry, objhit;
      recording.append(new RaytracerFrame(
            404,null, null,static_scene_data,view_pt,view_d));

      double s,ss;
      recording.append(new RaytracerFrame(
            405,null, null,static_scene_data,view_pt,view_d));

      objhit=-1;
      for(objtry=0; objtry<nobject; objtry++){
      if(objhit!=-1)break;
      recording.append(new RaytracerFrame(
            407,"objtry="+objtry+", nobject="+nobject, null,static_scene_data,view_pt,view_d));

         if(objtry!=source){ //don't try source
         recording.append(new RaytracerFrame(
               408,"objtry="+objtry+", source="+source, null,static_scene_data,view_pt,view_d));

            s= object[objtry].objint(pos,ray);
            recording.append(new RaytracerFrame(
                  409,"s="+s, null,static_scene_data,view_pt,view_d));

            if((s>0.0)&&(s<=s_litdis)){
            recording.append(new RaytracerFrame(
                  410,"s="+s+"s_litdis="+s_litdis, null,static_scene_data,view_pt,view_d));

               objhit=0;
               recording.append(new RaytracerFrame(
                  411,null, null,static_scene_data,view_pt,view_d));

      }  }  }
      
      if(objhit==0){ //in shadow, find obj intersection point for vector.

         ss=s_litdis;
         for(objtry=0; objtry<nobject; objtry++){
            if(objtry!=source){
               s= object[objtry].objint(pos,ray);
               if((s>0.0)&&(s<=ss)){
                  objhit=objtry;
                  ss=s;
         }  }  }
         
         T3d hit= new T3d(pos.x+ss*ray.x, pos.y+ss*ray.y, pos.z+ss*ray.z);

         T3d sph2= new T3d();
         crossp(sph2,up,ray);//switched
         T3d spherical= DisplayCanvasWireFrame.coord_to_spherical(view_pt);
         T3d spherical_inc= new T3d(0,0,(sph2.z-spherical.z)*.1);
         T3d tmp_line1= (T3d) pos.clone(),
             tmp_line2= (T3d) pos.clone(),
             line_inc=  new T3d((hit.x-pos.x)*.1,(hit.y-pos.y)*.1,(hit.z-pos.z)*.1),
             focus= (T3d) current_lookp.clone(),
             focus_inc= new T3d((hit.x-focus.x)*.1, (hit.y-focus.y)*.1, (hit.z-focus.z)*.1);
         for(int i=1;i<=10;i++){
            tmp_line2.x+= line_inc.x; 
            tmp_line2.y+= line_inc.y; 
            tmp_line2.z+= line_inc.z;
            focus.x+= focus_inc.x;
            focus.y+= focus_inc.y;
            focus.z+= focus_inc.z;
            spherical.x+= spherical_inc.x;
            spherical.y+= spherical_inc.y;
            spherical.z+= spherical_inc.z;
            view_pt= DisplayCanvasWireFrame.spherical_to_coord(spherical.x,spherical.y,spherical.z);
            dynamic_scene_data= new DisplayFrame();
            add_scene2(focus);
            dynamic_scene_data.append(new RaytracerDFELine(tmp_line1, tmp_line2, focus,RAY_SHADOW));
            dynamic_scene_data.append(new RaytracerDFELine(eyep, eyep,  focus,SCREEN_IMAGE));
            recording.append(new RaytracerFrame(
                  326,"Object intersection, frame "+i+"/5", dynamic_scene_data,null, view_pt,view_d));
         }
         focus_inc.x*=-2; focus_inc.y*=-2; focus_inc.z*=-2; //move look point back to origin. must b/c static scene calc'd for origin.
         for(int i=1;i<=5;i++){
            focus.x+= focus_inc.x; 
            focus.y+= focus_inc.y; 
            focus.z+= focus_inc.z;
            dynamic_scene_data= new DisplayFrame();
            add_scene2(focus);
            recording.append(new RaytracerFrame(
                  326,"look to origin, frame "+i+"/5", dynamic_scene_data,null, view_pt,view_d));
         }
         return(light[lnum].bright); //not in shadow, add light from source

      }else{ //in light path, no shadow.
         T3d hit= new T3d(light[lnum].x, light[lnum].y, light[lnum].z);

         T3d sph2= new T3d();
         crossp(sph2,up,ray);//switched
         T3d spherical= DisplayCanvasWireFrame.coord_to_spherical(view_pt);
         T3d spherical_inc= new T3d(0,0,(sph2.z-spherical.z)*.1);
         T3d tmp_line1= (T3d) pos.clone(),
             tmp_line2= (T3d) pos.clone(),
             line_inc=  new T3d((hit.x-pos.x)*.1,(hit.y-pos.y)*.1,(hit.z-pos.z)*.1),
             focus= (T3d) current_lookp.clone(),
             focus_inc= new T3d((hit.x-focus.x)*.1, (hit.y-focus.y)*.1, (hit.z-focus.z)*.1);
         for(int i=1;i<=10;i++){
            tmp_line2.x+= line_inc.x; 
            tmp_line2.y+= line_inc.y; 
            tmp_line2.z+= line_inc.z;
            focus.x+= focus_inc.x;
            focus.y+= focus_inc.y;
            focus.z+= focus_inc.z;
            spherical.x+= spherical_inc.x;
            spherical.y+= spherical_inc.y;
            spherical.z+= spherical_inc.z;
            view_pt= DisplayCanvasWireFrame.spherical_to_coord(spherical.x,spherical.y,spherical.z);
            dynamic_scene_data= new DisplayFrame();
            add_scene2(focus);
            dynamic_scene_data.append(new RaytracerDFELine(tmp_line1, tmp_line2, focus,RAY_NO_SHADOW));
            dynamic_scene_data.append(new RaytracerDFELine(eyep, eyep,  focus,SCREEN_IMAGE));
            recording.append(new RaytracerFrame(
                  326,"Object intersection, frame "+i+"/5", dynamic_scene_data,null, view_pt,view_d));
         }
         focus_inc.x*=-2; focus_inc.y*=-2; focus_inc.z*=-2; //move look point back to origin. must b/c static scene calc'd for origin.
         for(int i=1;i<=5;i++){
            focus.x+= focus_inc.x; 
            focus.y+= focus_inc.y; 
            focus.z+= focus_inc.z;
            dynamic_scene_data= new DisplayFrame();
            add_scene2(focus);
            recording.append(new RaytracerFrame(
                  326,"look to origin, frame "+i+"/5", dynamic_scene_data,null, view_pt,view_d));
         }
         recording.append(new RaytracerFrame(
               413,"light[lnum].bright="+light[lnum].bright, null,static_scene_data,view_pt,view_d));
         return(0); //
      }
   }
   
   private int gammacorrect2(double intensity){ 
   recording.append(new RaytracerFrame(
         415,"intensity"+intensity, null,static_scene_data,view_pt,view_d));

      int ival;
      recording.append(new RaytracerFrame(
            416,null, null,static_scene_data,view_pt,view_d));

      double dval;
      recording.append(new RaytracerFrame(
            417,null, null,static_scene_data,view_pt,view_d));


      dval= intensity/255.0;
      recording.append(new RaytracerFrame(
            419,"dval"+dval, null,static_scene_data,view_pt,view_d));

      if(dval>1.0){ dval=1.0; }
      recording.append(new RaytracerFrame(
            420,"dval"+dval, null,static_scene_data,view_pt,view_d));

      if(dval<0.0) dval=0.0;
      recording.append(new RaytracerFrame(
            421,"dval"+dval, null,static_scene_data,view_pt,view_d));

         
      dval= Math.exp(Math.log(dval)/GAMMA);
      recording.append(new RaytracerFrame(
            423,"dval"+dval, null,static_scene_data,view_pt,view_d));

      dval*=255.0;
      recording.append(new RaytracerFrame(
            424,"dval"+dval, null,static_scene_data,view_pt,view_d));

      ival=(int)(dval+0.5);
      recording.append(new RaytracerFrame(
            425,"ival"+ival, null,static_scene_data,view_pt,view_d));

      recording.append(new RaytracerFrame(
            426,"ival"+ival, null,static_scene_data,view_pt,view_d));
      return ival;

   }

   //--------------------------------------  m a t h  --------
   private final double normalize2(T3d t){
   recording.append(new RaytracerFrame(
         430,null, null,static_scene_data,view_pt,view_d));

      double d= Math.sqrt(t.x*t.x + t.y*t.y + t.z*t.z);
      recording.append(new RaytracerFrame(
            431,"d"+d, null,static_scene_data,view_pt,view_d));

      t.x/=d; t.y/=d; t.z/=d;
      recording.append(new RaytracerFrame(
            432,"t="+t.toString(), null,static_scene_data,view_pt,view_d));

      recording.append(new RaytracerFrame(
            433,"d"+d, null,static_scene_data,view_pt,view_d));
      return d;
   }
   private final double dotp2(T3d a,T3d b){
   recording.append(new RaytracerFrame(
         435,null, null,static_scene_data,view_pt,view_d));

      recording.append(new RaytracerFrame(
            436,"dotp="+((a.x*b.x) + (a.y*b.y) + (a.z*b.z)), null,static_scene_data,view_pt,view_d));
      return ((a.x*b.x) + (a.y*b.y) + (a.z*b.z));

   }
   private final void crossp2(T3d o,T3d a,T3d b){
   recording.append(new RaytracerFrame(
         438,null, null,static_scene_data,view_pt,view_d));

      double d;
      recording.append(new RaytracerFrame(
            439,null, null,static_scene_data,view_pt,view_d));

      o.x= a.y*b.z - a.z*b.y;
      recording.append(new RaytracerFrame(
            440,"o.x"+o.x, null,static_scene_data,view_pt,view_d));

      o.y= a.z*b.x - a.x*b.z;
      recording.append(new RaytracerFrame(
            441,"o.y"+o.y, null,static_scene_data,view_pt,view_d));

      o.z= a.x*b.y - a.y*b.x;
      recording.append(new RaytracerFrame(
            442,"o.z"+o.z, null,static_scene_data,view_pt,view_d));

      d= Math.sqrt(o.x*o.x + o.y*o.y + o.z*o.z);
      recording.append(new RaytracerFrame(
            443,"d"+d, null,static_scene_data,view_pt,view_d));

      o.x/= d;
      recording.append(new RaytracerFrame(
            444,"o.x"+o.x, null,static_scene_data,view_pt,view_d));

      o.y/= d;
      recording.append(new RaytracerFrame(
            445,"o.y"+o.y, null,static_scene_data,view_pt,view_d));

      o.z/= d;
      recording.append(new RaytracerFrame(
            446,"o.z"+o.z, null,static_scene_data,view_pt,view_d));

   }

   //----------------------------------------------------
   //  methods for setup card panel.
   //----------------------------------------------------

   /**
    *  send current object data to setup card panel.
    *  send light points to rtsb.
    *  send viewing data.
    *
    *  @param r object to use to send scene data back.
    *  
    */
   public void get_scene(RaytracerSetupButtons r){
      for(int i=0;i<nobject;i++){ 
         if(object[i] instanceof OTriangle){
            OTriangle t= (OTriangle)object[i];
            r.add_object(new RaytracerDFETriangle(t.pt1,t.pt2,t.pt3, lookp,null), 
                        (TColor)t.ambiant.clone(), (TColor)t.diffuse.clone(), 
                        (TColor)t.specular.clone(), new T3d(t.coef,t.refl,t.tran));
         }
         if(object[i] instanceof OBox){
            OBox b= (OBox)object[i];
            r.add_object(new RaytracerDFEBox(new T3d(b.x,b.y,b.z), new T3d(b.xs,b.ys,b.zs), lookp,null),
                        (TColor)b.ambiant.clone(), (TColor)b.diffuse.clone(), 
                        (TColor)b.specular.clone(), new T3d(b.coef,b.refl,b.tran));
         }
         if(object[i] instanceof OSphere){
            OSphere s= (OSphere)object[i];
            r.add_object(new RaytracerDFESphere(new T3d(s.x,s.y,s.z), s.r, lookp,null),
                        (TColor)s.ambiant.clone(), (TColor)s.diffuse.clone(), 
                        (TColor)s.specular.clone(), new T3d(s.coef,s.refl,s.tran));
         }
      }
      for(int i=0;i<nlight;i++){
         r.add_light(new TLight(
            light[i].x, light[i].y, light[i].z, light[i].bright));
      }
      r.set_view(get_view_pt2(), .5);
   }

   /**
    *  get current view point (camera location).
    */
   public T3d get_view_pt(){ 
      return (T3d) eyep.clone();
   }

   /**
    *  get current view point (camera location) minus the look-at-point.
    */
   public T3d get_relative_view_pt(){ 
      return new T3d(eyep.x-lookp.x, eyep.y-lookp.y, eyep.z-lookp.z);
   }

   /**
    *  get current viewing 'd' (rho,phi,theta,d)
    */
   public double get_view_d(){ 
      return (vfov>hfov) ? (sizey/2)/Math.tan(vfov/2) : (sizey/2)/Math.tan(hfov/2);
   }


   /**
    *  trial-n-error: get current viewing 'd' (rho,phi,theta,d)
    */
   public T3d get_view_pt2(){ 
      return new T3d((eyep.x-lookp.x)*100, (eyep.y-lookp.y)*100, (eyep.z-lookp.z)*100);
   }
   /**
    *  trial-n-error: get current viewing 'd' (rho,phi,theta,d)
    */
   public double get_view_d2(){
      T3d pt= get_view_pt2(); 
      return (Math.max(pt.x,Math.max(pt.y,pt.z)))*.5;
   }

   //----------------------------------------------------
   //  methods passign data from setup panel to raytracer.
   //----------------------------------------------------

   /**
    *
    */
   public void set_objects(DisplayFrameElement[] r, TColor[] a, TColor[] d, 
                           TColor[] s, T3d[] attrib, int num_objects){
      AlgSource as= sim.get_alg_source();
      int i=0;
      for(; i<num_objects; i++){
         if(r[i] instanceof RaytracerDFETriangle){
            RaytracerDFETriangle t= (RaytracerDFETriangle) r[i];
            object[i]= new OTriangle((T3d)t.tmp_world[0].clone(), 
                  (T3d)t.tmp_world[1].clone(), (T3d)t.tmp_world[2].clone());
            object[i].set_colors(a[i].r,a[i].g,a[i].b, d[i].r,d[i].g,d[i].b, 
                  s[i].r,s[i].g,s[i].b, attrib[i].x,attrib[i].y,attrib[i].z);
            as.add_code("      p1.x="+ t.tmp_world[0].x+ "; p1.y="+ t.tmp_world[0].y+
                        " ; p1.z="+ t.tmp_world[0].z+ ";");
            as.add_code("      p2.x="+ t.tmp_world[1].x+ "; p2.y="+ t.tmp_world[1].y+
                        " ; p2.z="+ t.tmp_world[1].z+ ";");
            as.add_code("      p3.x="+ t.tmp_world[2].x+ "; p3.y="+ t.tmp_world[2].y+
                        " ; p3.z="+ t.tmp_world[2].z+ ";");
            as.add_code("      object[nobject]= new OTriangle(p1,p2,p3);");
            as.add_code("      object[nobject].set_colors");
            as.add_code("         ("+ a[i].r+ ","+ a[i].g+ ","+ a[i].b+ ", "+
                                   d[i].r+ ","+ d[i].g+ ","+ d[i].b+ ", "+
                                   s[i].r+ ","+ s[i].g+ ","+ s[i].b+ ", "+
                                   attrib[i].x+ ","+ attrib[i].y+ ","+ attrib[i].z+ ");");
            as.add_code("      nobject++;");
            as.add_code("");
         }
         if(r[i] instanceof RaytracerDFEBox){
            RaytracerDFEBox b= (RaytracerDFEBox) r[i];
            object[i]= new OBox(b.center.x, b.center.y, b.center.z,
                  b.width.x,b.width.y,b.width.z);
            object[i].set_colors(a[i].r,a[i].g,a[i].b, d[i].r,d[i].g,d[i].b, 
                  s[i].r,s[i].g,s[i].b, attrib[i].x,attrib[i].y,attrib[i].z);
            as.add_code("      object[nobject]= new OBox("+
                               b.center.x+ ","+ b.center.y+ ","+ b.center.z+ ", "+
                               b.width.x+ ","+ b.width.y+ ","+ b.width.z+ ");");
            as.add_code("      object[nobject].set_colors");
            as.add_code("         ("+ a[i].r+ ","+ a[i].g+ ","+ a[i].b+ ", "+
                                   d[i].r+ ","+ d[i].g+ ","+ d[i].b+ ", "+
                                   s[i].r+ ","+ s[i].g+ ","+ s[i].b+ ", "+
                                   attrib[i].x+ ","+ attrib[i].y+ ","+ attrib[i].z+ ");");
            as.add_code("      nobject++;");
            as.add_code("");
         }
         if(r[i] instanceof RaytracerDFESphere){
            RaytracerDFESphere sph= (RaytracerDFESphere) r[i];
            object[i]= new OSphere(sph.radius, sph.center.x, sph.center.y, sph.center.z);
            object[i].set_colors(a[i].r,a[i].g,a[i].b, d[i].r,d[i].g,d[i].b, 
                  s[i].r,s[i].g,s[i].b, attrib[i].x,attrib[i].y,attrib[i].z);
            as.add_code("      object[nobject]= new OSphere("+
                               sph.radius+ ", "+ sph.center.x+ ","+ sph.center.y+ ","+ sph.center.z+ ");");
            as.add_code("      object[nobject].set_colors");
            as.add_code("         ("+ a[i].r+ ","+ a[i].g+ ","+ a[i].b+ ", "+
                                   d[i].r+ ","+ d[i].g+ ","+ d[i].b+ ", "+
                                   s[i].r+ ","+ s[i].g+ ","+ s[i].b+ ", "+
                                   attrib[i].x+ ","+ attrib[i].y+ ","+ attrib[i].z+ ");");
            as.add_code("      nobject++;");
            as.add_code("");
         }
      }
      for(; i<OBJECTS; i++){ object[i]= null; }
      nobject= num_objects;
      as.add_code(""); 
      as.add_code("      for(i=nobject; i<OBJECTS; i++){ object[i]=null; }"); 
      as.add_code(""); 

   }

   /**
    *  store light additions from the setup panel.
    *  @param r DFE object to turn into a TObject.
    */
   public void set_lights(TLight[] l, int n){
      AlgSource as= sim.get_alg_source();
      int i=0;
      for(; i<n; i++){
         light[i]= (TLight) l[i].clone();
         as.add_code("      light[nlight]= new TLight("+ light[i].x+ ","+
             light[i].y+ ","+ light[i].z+ ","+ light[i].bright+ ","+  ");");
         as.add_code("      nlight++"); 
      }
      for(; i<LIGHTS; i++){ light[i]= null; }
      nlight= n;
      as.add_code(""); 
      as.add_code("      for(i=nlight; i<LIGHTS; i++){ light[i]=null; }"); 
      as.add_code(""); 
   }

   /**
    *  store view camera data from the setup panel.
    *  @param v holds camera location.
    */
   public void set_view(RaytracerDFEView v){
      eyep= (T3d) v.camera.clone();
      eyep.x/=100; eyep.y/=100; eyep.z/=100;
      GAMMA= v.gamma;
      
      AlgSource as= sim.get_alg_source();
      as.add_code("      eyep=new T3d("+ eyep.x+ ","+ eyep.y+ ","+ eyep.z+ ","+ 
                  "); lookp=new T3d(0,0,0);");
      as.add_code("      up=new T3d(0,1,0);");
      as.add_code("      hfov=10; vfov=10;");
      as.add_code("      maxlevel=5;");
      as.add_code("      GAMMA= "+ v.gamma+ ";");
      as.add_code("      sizex=250; sizey=250;");
      as.add_code("      sizex=250; sizey=250;");
      as.add_code("      viewing(scrnx,scrny,firstray);");
      as.add_code("");
      as.add_code("      int i;");
      as.add_code("      T3d p1=new T3d(),p2=new T3d(),p3=new T3d();");
      as.add_code("");
      as.add_code("      background= new TColor(0,0,0);");
      as.add_code("      nlight=0; nobject=0;");
      as.add_code("");
   }

//----------------------------------------------------
} //class raytracer

