package me.wattmann.rpets.data;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import me.clip.placeholderapi.PlaceholderAPI;
import me.wattmann.concurrent.BukkitExecutor;
import me.wattmann.rpets.tuples.Pair;
import org.bukkit.ChatColor;

import java.util.*;
import java.util.stream.Collectors;

@Getter @Builder public final class DataProfile
{
    private final UUID uuid;
    private final PetData data;

    public static class PetData implements Iterable<Map.Entry<String, Long>>
    {
        private final Map<String, Long> entries;

        /**
         * Default constructor, constructs empty instance of this object
         * */
        public PetData() {
            entries = new HashMap<>();
        }

        /**
         * Constructs an instance of this object with specified contents
         * @param map {@link Map<String,Long>} default content
         * */
        public PetData(@NonNull Map<String, Long> map) {
            this();
            entries.putAll(map);
            entries.keySet().forEach(ChatColor::stripColor);
        }

        /**
         * Returns an optional integer representing experience value for given pet name
         * @param petName name of the pet
         * @return {@link Optional<Integer>} experience value
         * */
        public Optional<Long> getExperience(@NonNull String petName) {
            return Optional.ofNullable(entries.get(ChatColor.stripColor(petName)));
        }

        /**
         * Used to set experience level to given pet name, blocks until mutex frees
         * @param petName name of the pet
         * @param var experience value
         * */
        public synchronized void setExperience(@NonNull String petName, long var) {
            entries.put(ChatColor.stripColor(petName), var);
        }

        /**
         * Used to add experience to current experience value to a given pet name, blocks until mutex frees
         * @param petName name of the pet
         * @param var experience integer to add to current experience value
         * */
        public void addExperience(@NonNull String petName, long var) {
            setExperience(petName, getExperience(petName).orElse(0L) + var);
        }

        /**
         * Returns an unmutable copy of entries saved in the original map
         * */
        @Override
        public @NonNull Iterator<Map.Entry<String, Long>> iterator() {
            return Map.copyOf(entries).entrySet().iterator();
        }
    }
}
