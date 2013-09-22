package sample;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.serialization.Serialization;
import akka.serialization.SerializationExtension;
import akka.util.ByteString;
import akka.util.Timeout;
import org.junit.Test;
import static akka.pattern.Patterns.ask;

import sample.CountingActor.Count;
import sample.CountingActor.Get;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.zeromq.*;
import scala.Option;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.util.Try;

import java.io.Serializable;
import static org.junit.Assert.assertEquals;
public class SpringTestOverZeroMq {
    static final Timeout timeout = new Timeout(Duration.create(500, "milliseconds"));
    static class SendCount {}
	@Test
	public void TestRemoteActor() throws Exception {
		ActorSystem system = ActorSystem.create();
        ActorRef actor = system.actorOf(Props.create(MyUntypedActor.class));
        System.out.println("Sending");
        actor.tell(new SendCount(), null);
        Thread.sleep(500);
        System.out.println("Sending");
        actor.tell(new SendCount(), null);
        Thread.sleep(500);
		Future<Object> result = ask(actor, new Get(), timeout);
		assertEquals(2, Await.result(result, timeout.duration()));
	}
    static class MyUntypedActor extends UntypedActor {
    	int x;
        Serialization ser = SerializationExtension.get(getContext().system());
        ActorRef listener = getSelf();
        ActorRef subSocket = ZeroMQExtension.get(getContext().system()).newReqSocket(
                new SocketOption[] {
                        new Listener(listener),
                        new Connect("tcp://127.0.0.1:1233")
                });
        ZMQMessage msg = ZMQMessage.withFrames(ByteString.fromArray(ser.findSerializerFor(new Count()).toBinary(new Count())));
        @Override
        public void onReceive(Object o) throws Exception {
            if(o instanceof SendCount){
                System.out.println("Sending request");
                subSocket.tell(msg, listener);
            }else if (o instanceof ZMQMessage) {
				System.out.println("Got response!!!");
				x++;
			} else if (o instanceof Get) {
			  getSender().tell(x, getSelf());
			}
		}
    }
}
