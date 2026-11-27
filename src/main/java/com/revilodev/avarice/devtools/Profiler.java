package com.revilodev.avarice.devtools;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public final class Profiler {
    private static final AtomicBoolean ENABLED = new AtomicBoolean();
    private static final Map<String, Measurement> MEASUREMENTS = new ConcurrentHashMap<>();

    private Profiler() { }

    public static boolean toggle() {
        while (true) {
            boolean current = ENABLED.get();
            if (ENABLED.compareAndSet(current, !current)) return !current;
        }
    }

    public static boolean enabled() {
        return ENABLED.get();
    }

    public static void record(String metric, long startedNanos) {
        if (!ENABLED.get()) return;
        MEASUREMENTS.computeIfAbsent(metric, ignored -> new Measurement()).record(System.nanoTime() - startedNanos);
    }

    public static Map<String, Snapshot> snapshots() {
        Map<String, Snapshot> result = new LinkedHashMap<>();
        MEASUREMENTS.forEach((name, measurement) -> result.put(name, measurement.snapshot()));
        return Map.copyOf(result);
    }

    public static void clear() {
        MEASUREMENTS.clear();
    }

    public record Snapshot(long count, long totalNanos, long maximumNanos) {
        public double averageMillis() {
            return count == 0 ? 0 : totalNanos / 1_000_000.0 / count;
        }
    }

    private static final class Measurement {
        private long count;
        private long totalNanos;
        private long maximumNanos;

        private synchronized void record(long nanos) {
            count++;
            totalNanos += nanos;
            maximumNanos = Math.max(maximumNanos, nanos);
        }

        private synchronized Snapshot snapshot() {
            return new Snapshot(count, totalNanos, maximumNanos);
        }
    }
}
