package de.invesdwin.context.r.optimalf;

import java.util.List;

import javax.annotation.concurrent.NotThreadSafe;

import io.onetapbeyond.renjin.r.executor.RenjinException;

@NotThreadSafe
public class Optimalf {

    private final List<List<Double>> tradesPerStrategy;

    public Optimalf(final List<List<Double>> tradesPerStrategy) {
        this.tradesPerStrategy = tradesPerStrategy;
    }

    public List<Double> getOptimalfPerStrategy() throws RenjinException {
        //        final RenjinTask task = Renjin.R(true, true)
        //                .code(new InputStreamReader(getClass().getResourceAsStream("Optimalf.R")))
        //                .build();
        //        final RenjinResult result = task.execute();
        //        if (!result.success()) {
        //            throw new IllegalStateException("Error: " + result.error(), result.cause());
        //        }
        return null;
    }

}
