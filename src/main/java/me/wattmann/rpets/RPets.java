package me.wattmann.rpets;

import lombok.Getter;
import lombok.NonNull;
import me.wattmann.rpets.data.DataRegistry;
import me.wattmann.rpets.listeners.KernelListener;
import me.wattmann.rpets.logback.Logback;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class RPets extends JavaPlugin
{
    @NonNull @Getter
    protected Logback logback;

    @NonNull @Getter
    protected KernelListener kernelListener;

    @NonNull @Getter
    private DataRegistry dataRegistry;

    @Override
    public void onLoad() {

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        logback.logInfo("Config loaded");

        this.logback = Logback.make("§6|§fRPets§6|");

        dataRegistry = new DataRegistry(this);

        kernelListener = new KernelListener(this);

        logback.logInfo("Finished loading");
    }

    @Override
    public void onEnable()
    {

        try {
            dataRegistry.init();
        } catch (Exception e) {
            logback.logError("Failed to initialize data registry.", e);
            disable();
            return;
        }
        logback.logInfo("Data registry initialized!");

        try {
            kernelListener.init();
        } catch (Exception e) {
            logback.logError("Failed to initialize kernel listener.", e);
            disable();
            return;
        }
        logback.logInfo("Kernel listener initialized!");
    }

    private void disable() {
        logback.logInfo("Disabling the plugin...");
        Bukkit.getPluginManager().disablePlugin(this);
    }

    @Override
    public void onDisable() {
        try {
            dataRegistry.term();
        } catch (Exception e) {
            logback.logError("Failed to terminate data registry.", e);
        }
    }
}
