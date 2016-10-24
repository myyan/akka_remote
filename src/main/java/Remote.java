import akka.actor.UntypedActor;

/**
 * Created by chenhao on 2016/10/24.
 */
public class Remote extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            System.out.println(message);
            getSender().tell("remote actor message", getSelf());
        }
    }
}
