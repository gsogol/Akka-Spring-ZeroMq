package sample;

import org.junit.Test;

import sample.CountingActor.Count;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.zeromq.*;

public class SpringTestOverZeroMq {

	@Test
	public void TestRemoteActor() {
		ActorSystem system = ActorSystem.create();
		ActorRef reqSocket = ZeroMQExtension.get(system).newReqSocket(
				new SocketOption[] { 
						  new Connect("tcp://127.0.0.1:1233")
				});
		reqSocket.tell(new Count(), null);		
	}
}
