package me.wattmann.concurrent;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class BukkitExecutor implements Executor {

    @NonNull @Getter
    protected final Plugin plugin;

    @NonNull
    private Set<BukkitRunnable> running = new HashSet<>();

    public BukkitExecutor(@NonNull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskAsynchronously(plugin);
    }

    public void execute(@NonNull Runnable runnable, long delay) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskLaterAsynchronously(plugin, delay);
    }

    public void executeTicking(@NonNull  Runnable runnable, long delay, long period) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskTimerAsynchronously(plugin, delay, period);
    }

  /*  public @NonNull Collection<BukkitRunnable> getRunning() {
        return (running).stream().filter((running) -> !running.isCancelled()).collect(Collectors.toCollection(HashSet::new));
    }

    private class BukkitExecutable extends BukkitRunnable
    {
        private Runnable runnable;

        public BukkitExecutable(@NonNull Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        public void run() {
            runnable.run();
            running.add(this);
        }

        @Override
        public synchronized void cancel() throws IllegalStateException {
            super.cancel();
        }
    }
    */
}
