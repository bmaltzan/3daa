/**
 *  AlgSource.java
 *
 *  abstract class for the sourcecode of the algorithm.
 *  extend and initialize code in the constructor.
 *
 *  @author  Brian Maltzan
 *  @version 99.3.1
 *  @since   JDK1.1.7A
 *  @serial
 */

public abstract class AlgSource{
   /** holds code for algorithm.  */
   protected String[] code= new String[600];

   /** holds current end of code, modified when setup adds code.  */
   protected int i;

   /** holds end of code, not including setup.  */
   protected int static_i;

   /** 
    *  deletes all code from the user's setup of the algorithm.
    */
   public void reset(){
      for(int index=static_i; index<i; index++){
         code[index]=null;
      }
      i=static_i;
   }

   /**
    *  adds a line of the algorithms setup code.
    */
   public void add_code(String s){ code[i]=s; i++; }

   /**
    *  get the specified line of code.  
    */
   public String get_code(int index){ 
      if(0<=index && index<= i){ return code[index]; }
      return "";
   }

   /**
    *  returns the current number of lines of code, including setup.
    */
   public int get_lines_of_code(){ return i; }
}




