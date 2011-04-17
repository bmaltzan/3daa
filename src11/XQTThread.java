/**
 *  XQTThread.java
 *  @serial
 */

public abstract class XQTThread extends Thread{
   public abstract void run();
   public abstract void reset();
   public abstract void forwardStep();
   public abstract void backwardStep();
   public abstract void forward(int speed);
   public abstract void backward(int speed);
   public abstract void die();
   public abstract void pause();

   public abstract void runForward();
   public abstract void runBackward();
}

