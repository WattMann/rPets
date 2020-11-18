package me.wattmann.rpets.events;

import lombok.Getter;
import lombok.NonNull;
import me.wattmann.rpets.data.PetProfile;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PetLevelupEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    @NonNull @Getter private final PetProfile profile;

    public PetLevelupEvent(@NonNull PetProfile profile) {
        this.profile = profile;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static @NonNull HandlerList getHandlerList() {
        return handlers;
    }

}
