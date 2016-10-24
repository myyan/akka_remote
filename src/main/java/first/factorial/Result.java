package first.factorial;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by chenhao on 2016/10/24.
 */
public class Result implements Serializable {
    private static final long serialVersionUID = 1L;
    public final int n;
    public final BigInteger factorial;

    public Result(int n, BigInteger factorial) {
        this.n = n;
        this.factorial = factorial;
    }
}
