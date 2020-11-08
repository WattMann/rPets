package me.wattmann.rpets.logback;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KernelFeedback
{
    @NonNull protected final String prefix;
    @NonNull protected final Logger logger;

    /**
     * Default constructor, protected
     * */
    protected KernelFeedback(@NonNull String prefix, @NonNull Logger logger) {
        this.prefix = prefix;
        this.logger = logger;
    }


    public void log(@NonNull String message, Level level, Object ... format) {
        this.logger.log(level, ChatColor.translateAlternateColorCodes('&', prefix + (char) 0x20 +  String.format("&f" + message, (Object[]) format)));
    }

    public void logInfo(@NonNull String message, Object ... format) {
        this.log(message, Level.INFO, format);
    }

    public void logError(@NonNull String message, @Nullable Throwable throwable, Object ... format) {
        this.log(message, Level.SEVERE, format);
        if(throwable != null)
            throwable.printStackTrace();
    }

    public void logWarn(@NonNull String message, Object ... format) {
        this.log(message, Level.WARNING, format);
    }

    public void logDebug(@NonNull String message, Object ... format) {
        this.log(message, Level.FINEST, format);
    }

    public void playerMessage(@NonNull CommandSender sender, @NonNull String message, Object ... format) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + String.format(message, format)));
    }

    public static @NonNull KernelFeedback make(@NonNull String prefix) {
        return new KernelFeedback(prefix, Bukkit.getLogger());
    }
}
