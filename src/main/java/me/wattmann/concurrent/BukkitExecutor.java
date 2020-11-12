package me.wattmann.concurrent;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.*;

@Deprecated public class BukkitExecutor implements Executor {

    @NonNull @Getter
    protected final Plugin plugin;

    public BukkitExecutor(@NonNull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        System.out.println("Executing async bukkit task0");
        new BukkitRunnable() {
            @Override
            public void run() {
                System.out.println("Executing async bukkit task1");
                runnable.run();
                this.cancel();
            }
        }.runTask(plugin);
    }

    public void execute(@NonNull Runnable runnable, long delay) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
                this.cancel();
            }
        }.runTaskLaterAsynchronously(plugin, delay);
    }

    public void executeTicking(@NonNull  Runnable runnable, long delay, long period) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
                this.cancel();
            }
        }.runTaskTimerAsynchronously(plugin, delay, period);
    }

    public static <T> T await(@NonNull CompletableFuture<T> future) {
        while(!future.isDone()){
            System.out.println(future.isDone());
        }
        System.out.println("out");
        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
