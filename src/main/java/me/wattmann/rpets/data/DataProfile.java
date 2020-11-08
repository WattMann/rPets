package me.wattmann.rpets.data;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import me.wattmann.rpets.tuples.Pair;
import org.bukkit.ChatColor;

import java.util.*;
import java.util.stream.Collectors;

@Getter @Builder public final class DataProfile
{
    private final UUID uuid;
    private final PetData data;

    public static class PetData implements Iterable<Map.Entry<String, Integer>>
    {
        private final Map<String, Integer> entries;

        /**
         * Default constructor, constructs empty instance of this object
         * */
        public PetData() {
            entries = Collections.synchronizedMap(new HashMap<>());

        }

        /**
         * Constructs an instance of this object with specified contents
         * @param map {@link Map<String,Integer>} default content
         * */
        public PetData(@NonNull Map<String, Integer> map) {
            this();
            entries.putAll(map);
            entries.keySet().forEach(ChatColor::stripColor);
        }

        /**
         * Returns an optional integer representing experience value for given pet name
         * @param petName name of the pet
         * @return {@link Optional<Integer>} experience integer
         * */
        public Optional<Integer> getExperience(@NonNull String petName) {
            return Optional.ofNullable(entries.get(ChatColor.stripColor(petName)));
        }

        /**
         * Used to set experience level to given pet name
         * @param petName name of the pet
         * @param var experience integer
         * */
        public synchronized void setExperience(@NonNull String petName, int var) {
            entries.put(ChatColor.stripColor(petName), var);
        }

        /**
         * Used to add experience to current experience integer to a given pet name
         * @param petName name of the pet
         * @param var experience integer to add to current experience integer
         * */
        public synchronized void addExperience(@NonNull String petName, int var) {
            setExperience(petName, getExperience(petName).orElse(0) + var);
        }

        /**
         * Returns an unmutable copy of entries saved in the original map
         * */
        @Override
        public @NonNull Iterator<Map.Entry<String, Integer>> iterator() {
            return Map.copyOf(entries).entrySet().iterator();
        }
    }
}
