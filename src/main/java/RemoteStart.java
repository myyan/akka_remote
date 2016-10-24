import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

/**
 * Created by chenhao on 2016/10/24.
 */
public class RemoteStart {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("RemoteSystem", ConfigFactory.load());
        ActorRef actor = system.actorOf(Props.create(Remote.class),"RemoteActor");
        actor.tell("remote is active",ActorRef.noSender());
    }
}
