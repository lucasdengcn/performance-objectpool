package com.example.demo.runtime;

import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.distribution.ValueAtPercentile;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;
import java.util.function.IntConsumer;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;


public abstract class AbstractPoolApplication {

    private static final long NANOS_PER_SECOND = 1000_000_000L;
    //in minutes
    private final int duration;
    private final int concurrent;
    //
    private final Timer timer;
    SimpleMeterRegistry registry = new SimpleMeterRegistry();
    //
    private final DecimalFormat decimalFormat = new DecimalFormat("##.###");
    private final DecimalFormat decimalFormat2 = new DecimalFormat("###,###.###");

    public AbstractPoolApplication(int duration, int concurrent){
        this.duration = duration;
        this.concurrent = concurrent;
        //
        timer = Timer.builder("ObjectPool.timer")
                .publishPercentiles(0.5, 0.9, 0.95, 0.99, 0.99999)
                .publishPercentileHistogram()
                .register(registry);
    }

    public void start(){
        // init pool
        System.out.println("init pool capacity.");
        this.poolWarmUp(1_000_000);
        System.out.println("init pool capacity Done: 100_000 elements");
        if (0 == concurrent) {
            startInSingleThread();
        } else {
            startInMultiThread();
        }
    }

    IntUnaryOperator intUnaryOperator = new IntUnaryOperator() {
        @Override
        public int applyAsInt(int operand) {
            poolActions();
            return 0;
        }
    };

    private void startInMultiThread(){
        System.out.println("Start in multi Threads");
        System.out.println("Start in single Thread");
        long currentTime = 0;
        long nanoTime = 0;
        long seconds = 0;
        long endTime = System.nanoTime() + TimeUnit.MINUTES.toNanos(duration);
        //
        do {
            currentTime = System.nanoTime();
            //
            IntStream.range(0, concurrent).parallel().map(intUnaryOperator).count();
            //
            nanoTime = System.nanoTime();
            long diff = nanoTime - currentTime;
            //
            timer.record(diff, TimeUnit.NANOSECONDS);
            //
            long currentSecond = ofNanos(nanoTime);
            if (currentSecond - seconds >= 1){
                printMetrics(currentSecond);
            }
            seconds = currentSecond;
            //
        } while (nanoTime <= endTime);
        //
        printMetrics(0);
        //
        System.out.println("End");
    }

    private void startInSingleThread() {
        System.out.println("Start in single Thread");
        long currentTime = 0;
        long nanoTime = 0;
        long seconds = 0;
        long endTime = System.nanoTime() + TimeUnit.MINUTES.toNanos(duration);
        //
        do {
            currentTime = System.nanoTime();
            this.poolActions();
            nanoTime = System.nanoTime();
            long diff = nanoTime - currentTime;
            //
            timer.record(diff, TimeUnit.NANOSECONDS);
            //
            long currentSecond = ofNanos(nanoTime);
            if (currentSecond - seconds >= 1){
                printMetrics(currentSecond);
            }
            seconds = currentSecond;
            //
        } while (nanoTime <= endTime);
        //
        printMetrics(0);
        //
        System.out.println("End");
    }

    public static long ofNanos(long nanos) {
        long secs = nanos / NANOS_PER_SECOND;
        int nos = (int) (nanos % NANOS_PER_SECOND);
        if (nos < 0) {
            secs--;
        }
        return secs;
    }

    //
    private void printMetrics(long currentSecond) {
        StringBuilder s = new StringBuilder(200);
        long size = poolSize();
        s.append("time=").append(currentSecond).append(", ");
        s.append("size=").append(decimalFormat2.format(size)).append(", ")
                .append("count=").append(decimalFormat2.format(timer.count())).append(", ")
                .append("Mean=").append(decimalFormat2.format(timer.mean(TimeUnit.NANOSECONDS))).append(", ")
                .append("Max=").append(decimalFormat2.format(timer.max(TimeUnit.NANOSECONDS))).append(", ");
        //
        ValueAtPercentile[] percentiles = timer.takeSnapshot().percentileValues();
        for (ValueAtPercentile percentile : percentiles) {
            double d = percentile.percentile() * 100;
            s.append(decimalFormat.format(d)).append("thP=").append(percentile.value()).append(", ");
        }
        //
        System.out.println(s);
    }


    public abstract void poolWarmUp(int size);

    public abstract void poolActions();

    public abstract long poolSize();

}
