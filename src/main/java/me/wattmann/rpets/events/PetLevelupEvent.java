package me.wattmann.rpets.events;

import lombok.Getter;
import lombok.NonNull;
import me.wattmann.rpets.data.PetProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PetLevelupEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    @NonNull @Getter private final PetProfile profile;
    @NotNull @Getter private final Player player;

    public PetLevelupEvent(@NonNull PetProfile profile, @NotNull Player player) {
        super(true);
        this.profile = profile;
        this.player = player;
    }


    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static @NonNull HandlerList getHandlerList() {
        return handlers;
    }

}
