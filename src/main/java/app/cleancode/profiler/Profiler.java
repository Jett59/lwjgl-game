package app.cleancode.profiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Profiler implements Runnable {
    private final List<Sample> samples = new ArrayList<>();

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(50);
                Thread.getAllStackTraces().forEach((thread, stackTrace) -> {
                    System.out.println("Thread " + thread.getName());
                    System.out.println("Stack depth: " + stackTrace.length);
                    if (stackTrace.length >= 1) {
                        String className = stackTrace[0].getClassName();
                        String methodName = stackTrace[0].getMethodName();
                        System.out.printf("Detected in method %s of class %s\n", methodName,
                                className);
                        Sample sample = new Sample(className, methodName);
                        if (!samples.contains(sample)) {
                            samples.add(sample);
                        } else {
                            sample = samples.get(samples.indexOf(sample));
                        }
                        sample.occurrences++;
                    }
                });
                Collections.sort(samples);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dump() {
        Collections.reverse(samples);
        System.out.println();
        System.out.println("Profiler results:");
        for (Sample sample : samples) {
            System.out.println(sample.toString());
        }
    }

}
