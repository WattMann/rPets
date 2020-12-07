package me.wattmann.rpets;

import lombok.NonNull;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.wattmann.rpets.imp.RPetsComponent;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public final class RPetsPlaceholders extends PlaceholderExpansion implements RPetsComponent {

    @NonNull
    private final RPets kernel;

    /**
     * Default constructor
     */
    public RPetsPlaceholders(@NonNull RPets kernel) {
        this.kernel = kernel;
    }

    @Override
    public void init() throws Exception {
        kernel.getLogback().logInfo("Hooking into PAPI!");
        this.register();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "rpets";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", kernel.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return kernel.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        //TODO
        return null;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if(player == null)
            return null;
        return this.onRequest(player, params);
    }

    @Override
    public @NonNull RPets getPetRef() {
        return kernel;
    }
}