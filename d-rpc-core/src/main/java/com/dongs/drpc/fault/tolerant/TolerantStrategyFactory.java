package com.dongs.drpc.fault.tolerant;

import com.dongs.drpc.spi.SpiLoader;


/**
 * 容错策略工厂
 *
 * @author dongs
 */
public class TolerantStrategyFactory {

    static {
        SpiLoader.load(TolerantStrategy.class);
    }

    private static final TolerantStrategy DEFAULT_STRATEGY = new FailFastTolerantStrategy();

    public static TolerantStrategy getTolerantStrategy(String strategy) {
        return SpiLoader.getInstance(TolerantStrategy.class, strategy);
    }
}
