package simple;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.Arrays;

/**
 * Created by chenhao on 2016/10/24.
 */
public class SimpleClusterApp {
    public static void main(String[] args) {
        startUp(new String[]{"2551", "2552"});
    }

    public static void startUp(String[] ports) {
        Arrays.stream(ports).forEach(port -> {
                    Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(ConfigFactory.load());
                    //create an akka system
                    ActorSystem system = ActorSystem.create("Clustersystem", config);

                    system.actorOf(Props.create(SimpleClusterListener.class), "clusterListener");
                }
        );

    }
}
