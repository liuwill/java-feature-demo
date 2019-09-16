package compiler;

import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import compiler.annotations.SourceBuild;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("server.*")
public class AnnotationProcessor extends AbstractProcessor {
  /**
   * 用于在编译器打印消息的组件
   */
  Messager messager;

  public AnnotationProcessor() {
    super();
  }

  @Override
  public final synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    this.messager = processingEnv.getMessager();
    // this.trees = JavacTrees.instance(processingEnv);
    // Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
    // this.treeMaker = TreeMaker.instance(context);
    // this.names = Names.instance(context);
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    for (Element elem : roundEnv.getElementsAnnotatedWith(SourceBuild.class)) {
      SourceBuild annotation = elem.getAnnotation(SourceBuild.class);
      String message = "annotation found in " + elem.getSimpleName() + " with  " + annotation.value();
      // addToString(elem);
      this.messager.printMessage(Diagnostic.Kind.NOTE, message);
    }
    return true; // no further processing of this annotation type
  }
}
