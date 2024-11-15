package com.example.demo.runtime;

import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.distribution.ValueAtPercentile;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


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
    private final ExecutorService executorService;
    private final List<Callable<Void>> tasks;

    public AbstractPoolApplication(int duration, int concurrent){
        this.duration = duration;
        this.concurrent = concurrent;
        //
        timer = Timer.builder("ObjectPool.timer")
                .publishPercentiles(0.5, 0.9, 0.95, 0.99, 0.99999)
                .publishPercentileHistogram()
                .register(registry);
        //
        if (concurrent > 0){
            executorService = new ThreadPoolExecutor(concurrent, Integer.MAX_VALUE,
                    60L, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>());
//            executorService = Executors.newVirtualThreadPerTaskExecutor();
            tasks = new ArrayList<Callable<Void>>(concurrent);
            for (int i=0; i<concurrent; i++){
                tasks.add(runnable);
            }
        } else {
            executorService = null;
            tasks = null;
        }
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
        this.shutdown();
    }

    Callable<Void> runnable = new Callable<Void>() {
        @Override
        public Void call() throws Exception {
            long currentTime = System.nanoTime();
            poolActions();
            long diff = System.nanoTime() - currentTime;
            timer.record(diff, TimeUnit.NANOSECONDS);
            return null;
        }
    };

    private void startInMultiThread(){
        System.out.println("Start in multi Threads");
        long seconds = 0;
        long nanoTime = 0;
        long endTime = System.nanoTime() + TimeUnit.MINUTES.toNanos(duration);
        //
        do {
            //
            try {
                executorService.invokeAll(tasks);
                // Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //
            nanoTime = System.nanoTime();
            seconds++;
            if (seconds % 1000 == 0) {
                printMetrics(seconds);
            }
            //
        } while (nanoTime <= endTime);
        //
        printMetrics(0);
        //
        executorService.shutdown();
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

    public abstract void shutdown();

}
