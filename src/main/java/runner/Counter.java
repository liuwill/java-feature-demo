package runner;

public class Counter {

  static {
    System.out.println("** StaticCounter **");
  }

  public void run() {
    System.out.println("** Counter **");
  }
}
