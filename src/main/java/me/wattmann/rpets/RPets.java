package me.wattmann.rpets;

import lombok.Getter;
import lombok.NonNull;
import me.wattmann.rpets.config.ConfigRetail;
import me.wattmann.rpets.data.DataRegistry;
import me.wattmann.rpets.handlers.KernelHandler;
import me.wattmann.rpets.logback.Logback;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class RPets extends JavaPlugin
{
    @NonNull @Getter
    protected Logback logback;

    @NonNull @Getter
    protected KernelHandler kernelHandler;

    @NonNull @Getter
    protected DataRegistry dataRegistry;

    @NonNull @Getter
    protected ConfigRetail configRetail;

    @Override
    public void onLoad() {

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        this.logback = Logback.make("§6|§fRPets§6|");

        logback.logInfo("Config loaded");

        configRetail = new ConfigRetail(this);

        dataRegistry = new DataRegistry(this);

        kernelHandler = new KernelHandler(this);

        logback.logInfo("Finished loading");
    }

    @Override
    public void onEnable()
    {
        try {
            configRetail.init();
        } catch (Exception e) {
            logback.logError("Failed to initialize config retail.", e);
            disable();
            return;
        }
        logback.logInfo("Config retail initialized!");

        try {
            dataRegistry.init();
        } catch (Exception e) {
            logback.logError("Failed to initialize data registry.", e);
            disable();
            return;
        }
        logback.logInfo("Data registry initialized!");

        try {
            kernelHandler.init();
        } catch (Exception e) {
            logback.logError("Failed to initialize kernel handler.", e);
            disable();
            return;
        }
        logback.logInfo("Kernel handler initialized!");
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
