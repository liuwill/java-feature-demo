import java.lang.ClassLoader;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

public class ClassLogger implements ClassFileTransformer {
  @Override
  public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
      ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
    try {
      if (!className.startsWith("java") && !className.startsWith("sun") && !className.startsWith("jdk")) {
        System.out.println("ClassLogger::" + className);
      }

      // Path path = Paths.get("classes/" + className + ".class");
      // Files.write(path, classfileBuffer);
    } catch (Throwable ignored) { // ignored, don’t do this at home kids
    } finally {
      return classfileBuffer;
    }
  }
}
