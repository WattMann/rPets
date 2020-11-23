package me.wattmann.rpets.data;

import io.netty.buffer.UnpooledUnsafeDirectByteBuf;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import me.wattmann.rpets.events.PetLevelupEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter public final class DataRecord
{

    private final UUID uuid;
    private final PetData data;

    private DataRecord(UUID uuid, PetData data) {
        this.uuid = uuid;
        this.data = data;
    }

    public static DataRecord make(@NotNull UUID player, @NotNull Map<String, Long> def) {
        return new DataRecord(player, new PetData(player, def));
    }
    public static DataRecord make(@NotNull UUID player) {
        return new DataRecord(player, new PetData(player));
    }

    public static class PetData implements Iterable<PetProfile>
    {
        @NotNull private final Set<PetProfile> data;
        @NotNull private final UUID player;

        public PetData(@NotNull UUID player) {
            this.player = player;
            data = new HashSet<>();
        }

        public PetData(@NotNull UUID player, @NonNull Map<String, Long> def) {
            this(player);
            def.forEach((key, val) -> {
                if(find(key).isEmpty())
                    data.add(new PetProfile(DataRegistry.makeFriendly(key), val));
            });
        }

        /**
         * Used to manipulate data of a pet, this may result in a blocking operation when more threads access one record.
         * @param key name of the pet
         * @param val value to be set, xp
         * */
        public void set(@NonNull String key, long val) {
            find(key).ifPresentOrElse((datum) -> {
                if(datum.setVal(val)) {
                    var player = Bukkit.getPlayer(this.player);
                    if(player != null)
                        Bukkit.getPluginManager().callEvent(new PetLevelupEvent(datum, player));
                }
            }, () -> {
                data.add(new PetProfile(DataRegistry.makeFriendly(key), val));
            });
        }

        /**
         * Used to manipulate data of a pet, this may result in a blocking operation when more threads access one record.
         * @param key name of the pet
         * @param val value to be added to current xp
         * */
        public void add(@NonNull String key, long val) {
            find(key).ifPresentOrElse((datum) -> {
                set(ChatColor.stripColor(key).toLowerCase(), datum.getValueOpt().orElse(0L) + val);
            }, () -> {
                data.add(new PetProfile(DataRegistry.makeFriendly(key), val));
            });
        }


        /**
         * Used to search and find desired pet profile.
         * @param key name of the pet
         * @return {@link Optional} of {@link PetProfile}
         * */
        public Optional<PetProfile> find(@NonNull String key) {
            return data.stream().filter((datum) -> {
                return datum.getKey().equals(DataRegistry.makeFriendly(key));
            }).findFirst();
        }

        @NotNull
        @Override
        public Iterator<PetProfile> iterator() {
           return data.iterator();
        }
    }
}
