package sample;

import akka.actor.ActorSystem;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import static sample.SpringExtension.SpringExtProvider;


/**
 * A main class to start up the application.
 */
public class Main {
  public static void main(String[] args) throws Exception {
    // create a spring context and scan the classes
    AnnotationConfigApplicationContext ctx =
      new AnnotationConfigApplicationContext();
    ctx.scan("sample");
    ctx.refresh();

    // get hold of the actor system
    ActorSystem system = ctx.getBean(ActorSystem.class);
    // use the Spring Extension to create props for a named actor bean
    system.actorOf(SpringExtProvider.get(system).props("CountingActor"), "counter");
    //System.out.println(system.settings());
    try {
      
    } catch (Exception e) {
      System.err.println("Failed getting result: " + e.getMessage());
      throw e;
    } finally {
      //system.shutdown();
      system.awaitTermination();
      ctx.close();
    }
  }
}
