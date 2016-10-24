package first.factorial;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.Cluster;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Created by chenhao on 2016/10/24.
 */
public class FrontMain {
    public static void main(String[] args) {
        final int upToN = 200;

        final Config config = ConfigFactory.parseString("akka.cluster.roles = [frontend]").withFallback(
                ConfigFactory.load("factorial"));

        final ActorSystem system = ActorSystem.create("ClusterSystem", config);

        system.log().info("factorials will start when 2 backend member in the cluster");

        Cluster.get(system).registerOnMemberUp(new Runnable() {
            @Override
            public void run() {
                system.actorOf(Props.create(FrontEnd.class, upToN, true), "factorialFrontend");
            }
        });

        Cluster.get(system).registerOnMemberRemoved(new Runnable() {
            @Override
            public void run() {
                final Runnable exit = new Runnable() {
                    @Override
                    public void run() {
                        System.exit(0);
                    }

                };
                system.registerOnTermination(exit);
                system.terminate();

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Await.ready(system.whenTerminated(), Duration.create(10, TimeUnit.SECONDS));
                        } catch (Exception e) {
                            System.exit(-1);
                        }
                    }
                }.start();
            }
        });
    }
}
