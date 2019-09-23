package runner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ExecutorService;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import core.annotations.AutoRun;
import compiler.annotations.SourceBuild;

@SourceBuild
public class Run {
  public String open = "open";
  public Integer count = 0;
  public static String silence = "silence";

  private String name;

  public Run(String name) {
    this.name = name;
    this.open();
  }

  private void open() {
    System.out.println(this.open);
  }

  @AutoRun
  public String getName() {
    return name;
  }

  /**
   * 使用classLoader加载类，实例化之前不会执行静态代码块
   * @throws Exception
   */
  public static void runClassLoader() throws Exception {
    System.out.println("============ Run ClassLoader ============");
    Run.class.getClassLoader().loadClass("runner.Counter");
    System.out.println("After Load, Without static");
  }

  /**
   * 使用Class.forName加载类，加载的同时执行静态代码块
   * @throws Exception
   */
  public static void runClassForName() throws Exception {
    System.out.println("============ Run ClassForName ============");
    Class CounterClass = Class.forName("runner.Counter");

    System.out.println("After Load, static Running");
    Object counter = CounterClass.newInstance();
    for (Method method : CounterClass.getDeclaredMethods()) {
      System.out.println("getName:" + method.getName());
      System.out.println("invoke:" + method.invoke(counter));
    }
  }

  public static void runThread() throws Exception {
    System.out.println("============ Thread ============");
    Thread thread = new Thread(() -> {
      System.out.println("** Runnable.run **");
    });
    thread.start();
  }

  public static void runThreadPool(Run run) throws Exception {
    System.out.println("============ ThreadPool ============");
    int count = 5;
    AtomicInteger atom = new AtomicInteger();
    CountDownLatch latch = new CountDownLatch(count);
    ExecutorService executorService = Executors.newFixedThreadPool(count);
    while (count-- > 0) {
      final int current = count;
      executorService.execute(new Runnable() {
        public void run() {
          System.out.println("Asynchronous Start:" + current + ":" + atom.intValue());
          try {
            Thread.sleep(100);
          } catch (Exception e) {
          }
          System.out.println("Asynchronous task:" + current + ":" + atom.incrementAndGet() + ":" + run.count++);
          latch.countDown();
        }
      });
    }

    System.out.println("============ CountDownLatch START ============");
    latch.await();
    executorService.shutdown();
    System.out.println("AtomicInteger:" + atom.toString() + ":" + run.count);
    System.out.println("============ CountDownLatch DOWN ============");
  }

  public static void main(String args[]) {
    System.out.println("============ Main ============");
    Run run = new Run("ok");

    // String[] visitMethods = {"getName", "open"};
    try {
      System.out.println(Run.class.getCanonicalName());
      for (Method method : run.getClass().getDeclaredMethods()) {
        boolean hasAutoRun = method.isAnnotationPresent(AutoRun.class);

        if (!hasAutoRun) {
          continue;
        }
        // if (Arrays.binarySearch(visitMethods, method.getName()) < 0 || method.getName().equals("main")) {
        //   continue;
        // }
        System.out.println("getName:" + method.getName());
        System.out.println("invoke:" + method.invoke(run));
        System.out.println("============================");
      }

      Run.runClassLoader();
      Run.runClassForName();
      Run.runThread();
      Run.runThreadPool(run);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
