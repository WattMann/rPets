package me.wattmann.rpets;

import lombok.NonNull;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.wattmann.concurrent.BukkitExecutor;
import me.wattmann.rpets.data.DataProfile;
import me.wattmann.rpets.imp.RPetsComponent;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;


public final class RPetsSupplier extends PlaceholderExpansion implements RPetsComponent {

    @NonNull
    protected RPets kernel;

    private int base;
    private double exponent;

    /**
     * Default constructor
     * */
    public RPetsSupplier(@NonNull RPets kernel) {
        this.kernel = kernel;
    }

    @Override
    public void init() throws Exception {
        this.base = getPetRef().getConfigRetail().get(Integer.class,"experience.base",  1000);
        this.exponent = getPetRef().getConfigRetail().get(Double.class,"experience.exponent", 0.25d);
        kernel.getLogback().logInfo("Hooking into PAPI!");
        this.register();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "petexperience";
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
        if(params.isBlank())
            return "is blank";
        if(player == null)
            return "player null";
        else {
            DataProfile profile = kernel.getDataRegistry().fetch(player.getUniqueId(), false).join();
            if(profile == null)
                return "profile not found";
            return String.valueOf(level(profile.getData().getExperience(params).orElse(0L)));
        }
    }
    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if(player == null)
            return "player null placeholder";
        return this.onRequest(player, params);
    }

    public int level(long xp) {
        int lvl = 0;
        for(int i = 1; experience(i) <= xp; i++)
            lvl += 1;
        return lvl;
    }

    public long experience(long lvl) {
        long required = 0;
        for (int i = 1; i <= lvl; i++)
            required += i * (base * exponent) + base;
        return required;
    }



    @Override
    public @NonNull RPets getPetRef() {
        return kernel;
    }
}
