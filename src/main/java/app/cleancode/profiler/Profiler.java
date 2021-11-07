package app.cleancode.profiler;

import java.lang.Thread.State;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Profiler implements Runnable {
    private final Map<String, Statistics> samples = new HashMap<>();

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(25);
                Thread.getAllStackTraces().forEach((thread, stackTrace) -> {
                    if (!samples.containsKey(thread.getName())) {
                        samples.put(thread.getName(), new Statistics());
                    }
                    Statistics threadStatistics = samples.get(thread.getName());
                    System.out.println("Thread " + thread.getName());
                    System.out.println("Stack depth: " + stackTrace.length);
                    for (StackTraceElement stackTraceElement : stackTrace) {
                        String className = stackTraceElement.getClassName();
                        String methodName = stackTraceElement.getMethodName();
                        System.out.printf("Detected in method %s of class %s\n", methodName,
                                className);
                        Sample sample = new Sample(className, methodName);
                        if (!threadStatistics.samples.contains(sample)) {
                            threadStatistics.samples.add(sample);
                        } else {
                            sample = threadStatistics.samples
                                    .get(threadStatistics.samples.indexOf(sample));
                        }
                        sample.occurrences++;
                    }
                    if (thread.getState().equals(State.RUNNABLE)) {
                        threadStatistics.running++;
                    } else {
                        threadStatistics.blocking++;
                    }
                });
                samples.values().forEach(stats -> Collections.sort(stats.samples));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dump() {
        System.out.println();
        System.out.println("Profiler results:");
        samples.forEach((thread, threadStatistics) -> {
            System.out.println("For thread: " + thread);
            System.out.printf("Spent %.2f%% actually running\n", threadStatistics.running
                    / (double) (threadStatistics.running + threadStatistics.blocking) * 100);
            Collections.reverse(threadStatistics.samples);
            for (Sample sample : threadStatistics.samples) {
                System.out.println(sample.toString());
            }
        });
    }

}
