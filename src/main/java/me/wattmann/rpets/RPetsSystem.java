package me.wattmann.rpets;

import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.NonNull;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.wattmann.rpets.data.DataProfile;
import me.wattmann.rpets.imp.RPetsComponent;
import me.wattmann.rpets.model.config.Reward;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;


public final class RPetsSystem extends PlaceholderExpansion implements RPetsComponent {

    @NonNull
    private final RPets kernel;

    private static int base = 1000;
    private static double exponent = 0.25d;

    /**
     * Default constructor
     * */
    public RPetsSystem(@NonNull RPets kernel) {
        this.kernel = kernel;
    }

    @Override
    public void init() throws Exception {
        base = getPetRef().getConfigRetail().get(Integer.class,"experience.base",  1000);
        exponent = getPetRef().getConfigRetail().get(Double.class,"experience.exponent", 0.25d);
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
            return null;
        if(player == null)
            return null;
        else {
            DataProfile profile = kernel.getDataRegistry().fetch(player.getUniqueId(), false).join();
            if(profile == null)
                return null;
            return String.valueOf(level(profile.getData().getExperience(params).orElse(0L)));
        }
    }
    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if(player == null)
            return null;
        return this.onRequest(player, params);
    }

    public static int level(long xp) {
        int lvl = 0;
        for(int i = 1; experience(i) <= xp; i++)
            lvl += 1;
        return lvl;
    }

    public static long experience(long lvl) {
        long required = 0;
        for (int i = 1; i <= lvl; i++)
            required += i * (base * exponent) + base;
        return required;
    }

    public @NonNull Optional<Reward> getReward(@NonNull int lvl) {
        return getReward(String.valueOf(lvl));
    }

    public @NonNull Optional<Reward> getReward(@NonNull String lvl) {
        return Optional.empty(); //TODO
    }



    @Override
    public @NonNull RPets getPetRef() {
        return kernel;
    }
}