package sample;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.zeromq.*;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

/**
 * An actor that can count using an injected CountingService.
 *
 * @note The scope here is prototype since we want to create a new actor
 * instance for use of this bean.
 */
@Named("CountingActor")
@Scope("prototype")
class CountingActor extends UntypedActor {
  ActorRef listener = getSelf();
  ActorRef subSocket = ZeroMQExtension.get(getContext().system()).newRepSocket(
		  new SocketOption[] { 
				  new Listener(listener),
				  new Bind("tcp://127.0.0.1:1233")
		  });
  
  public static class Count {}
  public static class Get {}

  // the service that will be automatically injected
  final CountingService countingService;

  @Inject
  public CountingActor(@Named("CountingService") CountingService countingService) {
    this.countingService = countingService;
  }

  private int count = 0;

  @Override
  public void onReceive(Object message) throws Exception {
    if (message instanceof Count) {
      count = countingService.increment(count);
      System.out.println("The count is: " + count);
    } else if (message instanceof Get) {
      getSender().tell(count, getSelf());
    } else {
      unhandled(message);
    }
  }
}
