package me.wattmann.concurrent;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.*;

public class BukkitExecutor implements Executor {

    @NonNull @Getter
    protected final Plugin plugin;

    public BukkitExecutor(@NonNull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Runnable runnable) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskAsynchronously(plugin);
    }

    public void execute(Runnable runnable, long delay) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskLaterAsynchronously(plugin, delay);
    }

    public void executeTicking(Runnable runnable, long delay, long period) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskTimerAsynchronously(plugin, delay, period);
    }
}
