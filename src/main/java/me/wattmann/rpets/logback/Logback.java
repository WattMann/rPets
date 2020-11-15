package me.wattmann.rpets.logback;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Logback
{
    @NonNull private final String prefix;
    @NonNull private final Logger logger;

    /**
     * Default constructor, protected
     * */
    protected Logback(@NonNull String prefix, @NonNull Logger logger) {
        this.prefix = prefix;
        this.logger = logger;
    }


    public void log(@NonNull String message, Level level, Object ... format) {
        this.logger.log(level, ChatColor.translateAlternateColorCodes('&', prefix + (char) 0x20 + "&r" + String.format(message, (Object[]) format)));
    }

    public void logInfo(@NonNull String message, Object ... format) {
        this.log("&7" + message, Level.INFO, format);
    }

    public void logError(@NonNull String message, @Nullable Throwable throwable, Object ... format) {
        this.log("&4" + message, Level.SEVERE, format);
        if(throwable != null)
            throwable.printStackTrace();
    }

    public void logWarn(@NonNull String message, Object ... format) {
        this.log("&c" + message, Level.WARNING, format);
    }

    public void logDebug(@NonNull String message, Object ... format) {
        this.log("&7" + message, Level.FINEST, format);
    }

    public void playerMessage(@NonNull CommandSender sender, @NonNull String message, Object ... format) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + String.format(message, format)));
    }

    public static @NonNull Logback make(@NonNull String prefix) {
        return new Logback(prefix, Bukkit.getLogger());
    }
}
