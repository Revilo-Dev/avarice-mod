package com.revilodev.avarice.core.reload;

import java.util.LinkedHashMap;
import java.util.Map;

/** Serializes domain reloads so parsing/validation completes before an apply occurs. */
public final class ReloadManager {
    private final Map<String, ReloadableDomain<?>> domains = new LinkedHashMap<>();
    public synchronized void register(ReloadableDomain<?> domain) { if (domains.putIfAbsent(domain.id(), domain) != null) throw new IllegalArgumentException("Duplicate reload domain: " + domain.id()); }
    public synchronized ReloadDiagnostic reload(String id) {
        ReloadableDomain<?> domain = domains.get(id); if (domain == null) return ReloadDiagnostic.failure(id, "Unknown reload domain", 0);
        long start = System.nanoTime();
        try { reloadUnchecked(domain); return ReloadDiagnostic.success(id, elapsed(start)); }
        catch (Exception exception) { return ReloadDiagnostic.failure(id, exception.getMessage(), elapsed(start)); }
    }
    @SuppressWarnings("unchecked") private static <T> void reloadUnchecked(ReloadableDomain<?> untyped) throws Exception { ReloadableDomain<T> domain = (ReloadableDomain<T>) untyped; T candidate = domain.load(); domain.validate(candidate); domain.apply(candidate); }
    private static long elapsed(long start) { return (System.nanoTime() - start) / 1_000_000; }
    public record ReloadDiagnostic(String id, boolean success, String message, long elapsedMs) { static ReloadDiagnostic success(String id, long elapsed) { return new ReloadDiagnostic(id, true, "Reloaded", elapsed); } static ReloadDiagnostic failure(String id, String message, long elapsed) { return new ReloadDiagnostic(id, false, message == null ? "Reload failed" : message, elapsed); } }
}
