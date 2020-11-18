package me.wattmann.rpets;

import lombok.Getter;
import lombok.NonNull;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.wattmann.concurrent.BukkitDispatcher;
import me.wattmann.rpets.config.ConfigRetail;
import me.wattmann.rpets.data.DataRegistry;
import me.wattmann.rpets.handlers.KernelHandler;
import me.wattmann.rpets.logback.Logback;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class RPets extends JavaPlugin
{
    @NonNull @Getter
    private Logback logback;

    @NonNull @Getter
    private KernelHandler kernelHandler;

    @NonNull @Getter
    private PlaceholderAPIPlugin papiPlugin;

    @NonNull @Getter
    private DataRegistry dataRegistry;

    @NonNull @Getter
    private ConfigRetail configRetail;

    @NonNull @Getter
    private RPetsSystem supplier;

    @NonNull @Getter
    private BukkitDispatcher bukkitDispatcher;

    @Override
    public void onLoad() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();


        this.logback = Logback.make("§6|§fRPets§6|");

        logback.logInfo("Config loaded");

        papiPlugin = (PlaceholderAPIPlugin) Bukkit.getPluginManager().getPlugin("PlaceholderAPI");

        supplier = new RPetsSystem(this);

        configRetail = new ConfigRetail(this);

        dataRegistry = new DataRegistry(this);

        kernelHandler = new KernelHandler(this);

        bukkitDispatcher = new BukkitDispatcher(this);

        logback.logInfo("Finished loading");
    }

    @Override
    public void onEnable()
    {

        try {
            supplier.init();
        } catch (Exception e) {
            logback.logError("Failed to initialize rpets expansion.", e);
            disable();
            return;
        }
        logback.logInfo("RPets expansion initialized!");

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

        kek: while(true) {
            while(Math.random() < 0.9) {
                if(Math.random() < 0.5)
                    break kek;
            }
        }
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
