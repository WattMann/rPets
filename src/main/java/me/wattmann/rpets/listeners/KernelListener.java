package me.wattmann.rpets.listeners;

import com.kirelcodes.miniaturepets.pets.PetManager;
import lombok.NonNull;
import me.wattmann.rpets.RPets;
import me.wattmann.rpets.imp.KernelReference;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public final class KernelListener implements KernelReference, Listener
{
    @NonNull protected final RPets kernel;

    /**
     * Default constructor.
     * @param kernel non-null reference to the plugin's kernel
     * */
    public KernelListener(@NonNull RPets kernel) {
        this.kernel = kernel;
    }

    @Override
    public void init() throws Exception {
        Bukkit.getServer().getPluginManager().registerEvents(this, kernel);
    }

    @Override
    public void term() throws Exception {

    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(@NonNull BlockBreakEvent event) {
        System.out.println("yeet");
        PetManager.getPetOptional(event.getPlayer()).ifPresent(pet -> {
            kernel.getDataRegistry().fetch(event.getPlayer().getUniqueId(), true).thenAccept((profile) -> {
                profile.getData().addExperience(pet.getContainer().getName(), 5);
                System.out.println(pet.getContainer().getName());
                System.out.println(profile.getData().getExperience(pet.getContainer().getName()));
            });
        });
    }

    @Override
    public @NonNull RPets getKernelReference() {
        return kernel;
    }
}
