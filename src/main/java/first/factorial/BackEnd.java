package first.factorial;

import akka.actor.UntypedActor;

import java.math.BigInteger;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static akka.dispatch.Futures.future;

/**
 * Created by chenhao on 2016/10/24.
 */
public class BackEnd extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Integer) {
            final Integer n = (Integer) message;
            scala.concurrent.Future<BigInteger> f = future(new Callable<BigInteger>() {
                public BigInteger call() {
                    return factorial(n);
                }
            }, getContext().dispatcher());
        }
    }

    BigInteger factorial(int n) {
        BigInteger acc = BigInteger.ONE;
        for (int i = 1; i <= n; i++) {
            acc = acc.multiply(BigInteger.valueOf(i));
        }
        return acc;
    }
}
