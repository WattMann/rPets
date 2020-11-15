package me.wattmann.rpets.config;

import lombok.NonNull;
import me.wattmann.rpets.RPets;
import me.wattmann.rpets.imp.RPetsComponent;
import org.bukkit.configuration.file.FileConfiguration;


public class ConfigRetail implements RPetsComponent {

    @NonNull
    private final RPets kernel;

    @NonNull
    private final FileConfiguration configuration;

    public ConfigRetail(@NonNull RPets kernel) {
        this.kernel = kernel;
        this.configuration = kernel.getConfig();
    }

    /**
     * Used to retrieve datum from configuration, logs errors.
     * @param type class of the type to return
     * @param key lowercase key to where the datum is located
     * @param def default value in case not found or not matching
     * */
    public <T> @NonNull T get(Class<T> type, @NonNull String key,  T def) {
        if (configuration.contains(key.toLowerCase())) {
            var result = configuration.getObject(key.toLowerCase(), type);
            if(result == null) {
                kernel.getLogback().logWarn("Key %s is not of %s type, using default value", key.toLowerCase(), type.getTypeName());
                return def;
            } else
                return result;
        }
        else {
            kernel.getLogback().logWarn("Key %s is not present in config, using default value", key.toLowerCase());
            return def;
        }
    }

    /**
     * Used to retrieve datum from configuration, does not log errors.
     * @param type class of the type to return
     * @param key lowercase key to where the datum is located
     * @param def default value in case not found or not matching
     * */
    public <T> @NonNull T gets(Class<T> type, @NonNull String key,  T def) {
        if (configuration.contains(key.toLowerCase())) {
            var result = configuration.getObject(key.toLowerCase(), type);
            if(result == null)
                return def;
            else
                return result;
        }
        else
            return def;

    }

    @Override
    public @NonNull RPets getPetRef() {
        return kernel;
    }
}
