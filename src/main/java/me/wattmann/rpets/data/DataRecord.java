package me.wattmann.rpets.data;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import me.wattmann.rpets.events.PetLevelupEvent;
import me.wattmann.rpets.tuples.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter @Builder public final class DataRecord
{
    private final UUID uuid;
    private final PetData data;

    public static class PetData implements Iterable<PetProfile>
    {
        private final Set<PetProfile> data;

        public PetData() {
            data = new HashSet<>();
        }

        public PetData(@NonNull Map<String, Long> def) {
            this();
            def.forEach((key, val) -> {
                data.add(new PetProfile(ChatColor.stripColor(key), val));
            });
        }
        /**
         * Used to manipulate data of a pet, this may result in a blocking operation when more threads access one record.
         * @param key name of the pet
         * @param val value to be set, xp
         * */
        public void set(@NonNull String key, long val) {
            find(key).ifPresentOrElse((datum) -> {
                if(datum.setVal(val))
                    Bukkit.getPluginManager().callEvent(new PetLevelupEvent(datum));
            }, () -> {
                data.add(new PetProfile(key, val));
            });
        }

        /**
         * Used to manipulate data of a pet, this may result in a blocking operation when more threads access one record.
         * @param key name of the pet
         * @param val value to be added to current xp
         * */
        public void add(@NonNull String key, long val) {
            find(key).ifPresent((datum) -> {
                set(key, datum.getValueOpt().orElse(0L) + val);
            });
        }


        /**
         * Used to search and find desired pet profile.
         * @param key name of the pet
         * @return {@link Optional} of {@link PetProfile}
         * */
        public Optional<PetProfile> find(String key) {
            return data.stream().filter((datum) -> {
                return datum.getKey().equals(ChatColor.stripColor(key));
            }).findFirst();
        }

        @NotNull
        @Override
        public Iterator<PetProfile> iterator() {
           return data.iterator();
        }
    }
}
