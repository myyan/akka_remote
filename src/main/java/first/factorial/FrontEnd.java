package first.factorial;

import akka.actor.ActorRef;
import akka.actor.ReceiveTimeout;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.FromConfig;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Created by chenhao on 2016/10/24.
 */
public class FrontEnd extends UntypedActor {

    final int upToN;
    final boolean repeat;

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    ActorRef backend = getContext().actorOf(FromConfig.getInstance().props(), "backendRouter");

    public FrontEnd(int upToN, boolean repeat) {
        this.upToN = upToN;
        this.repeat = repeat;
    }


    @Override
    public void preStart() {
        sendJobs();
        getContext().setReceiveTimeout(Duration.create(10, TimeUnit.SECONDS));
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Result) {
            Result result = (Result) message;
            if (result.n == upToN) {
                log.debug("{} != {}", result.n, result.factorial);
                if (repeat) {
                    sendJobs();
                } else {
                    getContext().stop(getSelf());
                }
            }
        } else if (message instanceof ReceiveTimeout) {
            log.info("timeout");
            sendJobs();
        } else {
            unhandled(message);
        }
    }

    public void sendJobs() {
        log.info("starting batch of back end to {}", upToN);
        for (int n = 1; n <= upToN; n++) {
            backend.tell(n, getSelf());
        }
    }
}
