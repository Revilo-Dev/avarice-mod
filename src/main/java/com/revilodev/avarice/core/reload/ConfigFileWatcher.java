package com.revilodev.avarice.core.reload;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public final class ConfigFileWatcher implements AutoCloseable {
    private final WatchService watcher;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final Set<Path> pending = ConcurrentHashMap.newKeySet();
    private final Path directory;
    private final Executor callbackExecutor;
    private final Consumer<Set<Path>> changed;
    private final Thread thread;

    public ConfigFileWatcher(Path directory, Executor callbackExecutor, Consumer<Set<Path>> changed) throws IOException {
        this.directory = directory.toAbsolutePath().normalize();
        this.callbackExecutor = callbackExecutor;
        this.changed = changed;
        watcher = FileSystems.getDefault().newWatchService();
        Files.createDirectories(this.directory);
        this.directory.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
        thread = Thread.ofVirtual().name("avarice-config-watcher").start(this::watch);
    }
    private void watch() {
        while (running.get()) try {
            WatchKey key = watcher.take();
            key.pollEvents().forEach(event -> {
                Object context = event.context();
                if (context instanceof Path path && path.getFileName().toString().endsWith(".json")) pending.add(directory.resolve(path).normalize());
            });
            if (!key.reset()) break;
            Thread.sleep(250);
            if (!pending.isEmpty()) {
                Set<Path> changes = Set.copyOf(pending);
                pending.removeAll(changes);
                callbackExecutor.execute(() -> changed.accept(changes));
            }
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
            break;
        } catch (Exception exception) {
            if (running.get()) System.err.println("Avarice config watcher failed: " + exception.getMessage());
        }
    }
    @Override public void close() throws IOException { running.set(false); if (thread != null) thread.interrupt(); watcher.close(); }
}
