package simple;

import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Created by chenhao on 2016/10/24.
 */
public class SimpleClusterListener extends UntypedActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), SimpleClusterListener.class);

    Cluster cluster = Cluster.get(getContext().system());

    @Override
    public void preStart() {
        log.info("prestart");
        cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(),
                ClusterEvent.MemberEvent.class, ClusterEvent.UnreachableMember.class);
    }


    @Override
    public void postStop() {
        cluster.unsubscribe(getSelf());
    }

    @Override
    public void onReceive(Object message) throws Exception {
        log.info("receive message");
        if (message instanceof ClusterEvent.MemberUp) {
            log.info("Member is up");
        } else {
            unhandled(message);
        }
    }
}
