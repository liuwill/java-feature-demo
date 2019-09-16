package server;

public class Application {
  public static Application run(String[] args) {
    System.out.println("** Application.run **");
    return new Application();
  }
}
