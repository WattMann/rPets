package me.wattmann.rpets;

import lombok.NonNull;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.wattmann.rpets.data.DataRecord;
import me.wattmann.rpets.data.PetProfile;
import me.wattmann.rpets.imp.RPetsComponent;
import me.wattmann.rpets.model.Reward;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
        if(params.isBlank())
            return null;

        String[] args = params.split("_");

        if(args[0].equalsIgnoreCase("requiredxp")) {
            if(args.length >= 2) {
                try {
                    int lvl = Integer.parseInt(args[1]);
                    return String.valueOf(RPetsSystem.experience(lvl));
                } catch (NumberFormatException e) {
                    return null;
                }
            } else
                return null;
        }

        if(player == null)
            return null;

        DataRecord profile = kernel.getDataRegistry().fetch(player.getUniqueId(), false).join();
        if(profile == null)
            return null;

        Optional<PetProfile> pet;

        if(args.length == 1)
            pet = profile.getData().find(args[0]);
        else
            pet = profile.getData().find(args[1]);

        if(pet.isEmpty())
            return null;
        else if(args.length >= 2)
            if(args[0].equalsIgnoreCase("xp"))
                return String.valueOf(pet.get().getValue());

        return String.valueOf(pet.get().getLevel());
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
