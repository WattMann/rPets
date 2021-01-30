package me.wattmann.rpets.data;

import com.kirelcodes.miniaturepets.pets.Pet;
import lombok.Getter;
import lombok.NonNull;
import me.wattmann.rpets.RPets;
import me.wattmann.rpets.events.PetLevelupEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Period;
import java.util.*;

@Getter public final class DataRecord
{

    private final UUID uuid;
    private final PetData data;

    private DataRecord(UUID uuid, PetData data) {
        this.uuid = uuid;
        this.data = data;
    }

    /**
     * Used to create DataRecord for specified player with default values
     * @param player {@link UUID} owning player's UUID
     * @param def {@link Map<String, Long>} default values
     * */
    public static DataRecord make(@NotNull UUID player, @NotNull Map<String, Long> def, @NotNull RPets ref) {
        return new DataRecord(player, new PetData(ref, player, def));
    }
    /**
     * Used to create a empty DataRecord for specified player
     * @param player {@link UUID} owning player's UUID
     * */
    public static DataRecord make(@NotNull UUID player, @NotNull RPets ref) {
        return make(player, new HashMap<>(), ref);
    }

    public static class PetData implements Iterable<PetProfile>
    {
        @NotNull private final Set<PetProfile> data;
        @NotNull private final UUID player;
        @NotNull private final RPets rpets;


        public PetData(@NotNull RPets rpets, @NotNull UUID player) {
            this.player = player;
            data = new HashSet<>();
            this.rpets = rpets;
        }

        public PetData(@NotNull RPets rpets, @NotNull UUID player, @NonNull Map<String, Long> def) {
            this(rpets, player);
            def.forEach((key, val) -> {
                if(find(key).isEmpty())
                    create(key, val);
            });
        }

        /**
         * Used to manipulate data of a pet, this may result in a blocking operation when more threads access one record.
         * @param key name of the pet
         * @param val value to be set, xp
         * */
        public void set(@NonNull String key, long val) {
            find(DataRegistry.makeFriendly(key)).ifPresentOrElse((datum) -> {
                if(datum.setVal(val)) {
                    var player = Bukkit.getPlayer(this.player);
                    if(player != null)
                        Bukkit.getPluginManager().callEvent(new PetLevelupEvent(datum, player));
                }
            }, () -> create(DataRegistry.makeFriendly(key), val));
        }

        /**
         * Used to manipulate data of a pet, this may result in a blocking operation when more threads access one record.
         * @param key name of the pet
         * @param val value to be added to current xp
         * */
        public void add(@NonNull String key, long val) {
            find(DataRegistry.makeFriendly(key)).ifPresentOrElse((datum) -> {
                set(key, datum.getExperience() + val);
            }, () -> create(DataRegistry.makeFriendly(key), val));
        }


        private @NotNull PetProfile create(@NonNull String key, long xp) {
            final var builder = PetProfile.builder();
            builder.name(key);
            builder.experience(xp);
            this.rpets.getConfigRetail().get(Integer.class, "experience", key, "base").ifPresentOrElse(builder::base, () -> {
                this.rpets.getConfigRetail().get(Integer.class, "experience", "base").ifPresentOrElse(builder::base, () -> {
                    builder.base(1000);
                });
            });
            this.rpets.getConfigRetail().get(Double.class, "experience", key, "exponent").ifPresentOrElse(builder::exponent, () -> {
                this.rpets.getConfigRetail().get(Double.class, "experience", "exponent").ifPresentOrElse(builder::exponent, () -> {
                    builder.exponent(0.25D);
                });
            });
            var pet = builder.build();
            pet.calc();
            data.add(pet);
            return pet;
        }


        /**
         * Used to search and find desired pet profile.
         * @param key name of the pet
         * @return {@link Optional} of {@link PetProfile}
         * */
        public Optional<PetProfile> find(@NonNull String key) {
            return data.stream().filter((datum) -> {
                return datum.getName().equals(DataRegistry.makeFriendly(key));
            }).findFirst();
        }
        /**
         * Used to search and find desired pet profile. And create a new one if not found
         * @param key name of the pet
         * @return {@link PetProfile}
         * */
        public @NotNull PetProfile findOrCreate(@NonNull String key) {
            return data.stream().filter((datum) -> {
                return datum.getName().equals(DataRegistry.makeFriendly(key));
            }).findFirst().orElseGet(() -> {
                return this.create(key,0);
            });
        }

        @NotNull
        @Override
        public Iterator<PetProfile> iterator() {
           return data.iterator();
        }
    }
}
