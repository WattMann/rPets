package me.wattmann.rpets;

import lombok.Getter;
import lombok.NonNull;
import me.wattmann.rpets.data.DataRegistry;
import me.wattmann.rpets.listeners.KernelListener;
import me.wattmann.rpets.logback.KernelFeedback;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class RPets extends JavaPlugin
{
    @NonNull @Getter
    protected KernelFeedback kernelFeedback;

    @NonNull @Getter
    protected KernelListener kernelListener;

    @NonNull @Getter
    private DataRegistry dataRegistry;

    @Override
    public void onLoad() {
        this.kernelFeedback = KernelFeedback.make("§6|§fRPets§6|");

        this.kernelFeedback.logInfo("Hello there!");

        dataRegistry = new DataRegistry(this);

        kernelListener = new KernelListener(this);
    }

    @Override
    public void onEnable()
    {

        try {
            dataRegistry.init();
        } catch (Exception e) {
            kernelFeedback.logError("Failed to initialize data registry.", e);
            disable();
            return;
        }
        kernelFeedback.logInfo("Data registry initialized!");

        try {
            kernelListener.init();
        } catch (Exception e) {
            kernelFeedback.logError("Failed to initialize kernel listener.", e);
            disable();
            return;
        }
        kernelFeedback.logInfo("Kernel listener initialized!");
    }

    private void disable() {
        kernelFeedback.logInfo("Disabling the plugin...");
        Bukkit.getPluginManager().disablePlugin(this);
    }

    @Override
    public void onDisable() {
        try {
            dataRegistry.term();
        } catch (Exception e) {
            kernelFeedback.logError("Failed to terminate data registry.", e);
        }
    }
}
