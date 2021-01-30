package me.wattmann.rpets;

import com.kirelcodes.miniaturepets.pets.PetManager;
import lombok.NonNull;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.wattmann.rpets.data.DataRecord;
import me.wattmann.rpets.data.PetProfile;
import me.wattmann.rpets.imp.RPetsComponent;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;


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

    private final HashMap<ItemStack, Integer> drops = new HashMap<>();

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        // experience [petname] (format) : [0] petname or "current", pet exp; [1] format {exp, lvl} default lvl
        // required [petname]: [0] petname or "current", required exp for next level
        if (!params.isBlank()) {
            String[] args = params.split("_");

            if (args.length >= 1) {
                String action = args[0];
                    if (args.length >= 2) {
                        String petname = args[1];
                        if (petname.equalsIgnoreCase("current"))
                            if (player.isOnline())
                                petname = PetManager.getPet(player.getPlayer()).getContainer().getName();
                            else
                                return null;

                        String format = null;
                        if (args.length >= 3)
                            format = args[2];

                        try {
                            DataRecord record = kernel.getDataRegistry().fetch(player.getUniqueId(), false).get();
                            PetProfile profile = record.getData().findOrCreate(petname);
                            if (action.equalsIgnoreCase("experience")) {
                                if (format == null || format.equalsIgnoreCase("lvl")) {
                                    return String.valueOf(profile.getLevel());
                                } else {
                                    return String.valueOf(profile.getExperience());
                                }
                            } else if (action.equalsIgnoreCase("required")) {
                                return String.valueOf(profile.experience(profile.getLevel() + 1));
                            }
                        } catch (InterruptedException | ExecutionException e) {
                            kernel.getLogback().logError("Failed to fetch data for player %s[%s]", e, player.getName(), player.getUniqueId());
                        }
                    }
            }
        }
        return null;
    }

    public @NotNull ItemStack getDrop() {
        int size = 0;
        for (Integer value : drops.values())
            size += value;

        Random random = new Random();
        int rnd = random.nextInt(size);


        int buffer = 0;
        for (Map.Entry<ItemStack, Integer> itemStackIntegerEntry : drops.entrySet()) {
            buffer += itemStackIntegerEntry.getValue();
            if (buffer >= rnd) {
                return itemStackIntegerEntry.getKey();
            }
        }

        return getDrop();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null)
            return null;
        return this.onRequest(player, params);
    }

    @Override
    public @NonNull RPets getPetRef() {
        return kernel;
    }
}