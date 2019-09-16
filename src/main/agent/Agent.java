import java.lang.instrument.Instrumentation;

public class Agent {

  public static void premain(String agentOps, Instrumentation instrumentation) {
    System.out.println("====premain 方法执行");
    System.out.println(agentOps);

    ClassLogger transformer = new ClassLogger();
    instrumentation.addTransformer(transformer);
  }

  public static void premain(String agentOps) {
    System.out.println("====premain方法执行2====");
    System.out.println(agentOps);
  }
}
