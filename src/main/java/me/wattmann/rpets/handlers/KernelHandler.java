package me.wattmann.rpets.handlers;

import com.kirelcodes.miniaturepets.pets.PetManager;
import lombok.NonNull;
import me.wattmann.rpets.RPets;
import me.wattmann.rpets.data.DataRegistry;
import me.wattmann.rpets.events.PetLevelupEvent;
import me.wattmann.rpets.imp.RPetsComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.HashSet;
import java.util.Set;

public final class KernelHandler implements RPetsComponent, Listener
{
    @NonNull private final RPets kernel;
    @NonNull private Set<Location> latePlaces = new HashSet<>();

    /**
     * Default constructor.
     * @param kernel non-null reference to the plugin's kernel
     * */
    public KernelHandler(@NonNull RPets kernel) {
        this.kernel = kernel;
        //TODO: find a better suiting name
    }

    @Override
    public void init() throws Exception {
        Bukkit.getServer().getPluginManager().registerEvents(this, kernel);

        PluginCommand command;
        if((command = kernel.getCommand("petxp")) != null)
            command.setExecutor(this::onCommand);
        else
            kernel.getLogback().logError("Failed to initialize command executor.", null);
    }

    @Override
    public void term() throws Exception {

    }

    private boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        return true;
    }


    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(@NonNull BlockPlaceEvent event) {
        final var location = event.getBlock().getLocation();
        latePlaces.add(location);

        kernel.getBukkitDispatcher().execute(() -> {
            latePlaces.remove(location);
        }, 20 * 15);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(@NonNull BlockBreakEvent event) {
        if(latePlaces.contains(event.getBlock().getLocation()))
            return;
        final String type = DataRegistry.makeFriendly(event.getBlock().getType().name());
        PetManager.getPetOptional(event.getPlayer()).ifPresent(pet -> {
            kernel.getDataRegistry().fetch(event.getPlayer().getUniqueId(), true).thenAccept(dataRecord -> {
                kernel.getConfigRetail().get(Integer.class, "gain", "blocks", DataRegistry.makeFriendly(pet.getContainer().getName()), type).ifPresentOrElse(exp -> {
                    dataRecord.getData().add(pet.getContainer().getName(), exp);
                }, () -> {
                    kernel.getConfigRetail().get(Integer.class, "gain", "blocks", type).ifPresent((exp) -> {
                        dataRecord.getData().add(pet.getContainer().getName(), exp);
                    });
                });
            });
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(@NonNull EntityDeathEvent event) {
        final Player player;
        final String type = event.getEntity().getType().name();
        if((player = event.getEntity().getKiller()) != null) {
            PetManager.getPetOptional(event.getEntity().getKiller()).ifPresent((pet) -> {
                kernel.getDataRegistry().fetch(player.getUniqueId(), true).thenAccept(dataRecord -> {
                    kernel.getConfigRetail().get(Integer.class, "gain", "entities", DataRegistry.makeFriendly(pet.getContainer().getName()), DataRegistry.makeFriendly(type)).ifPresentOrElse(exp -> {
                        dataRecord.getData().add(pet.getContainer().getName(), exp);
                    }, () -> {
                        kernel.getConfigRetail().get(Integer.class, "gain", "entities", DataRegistry.makeFriendly(type)).ifPresent((exp) -> {
                            dataRecord.getData().add(pet.getContainer().getName(), exp);
                        });
                    });
                });
            });
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(@NonNull PetLevelupEvent event) {
        kernel.getConfigRetail().getReward(event.getProfile().getLevel(), event.getProfile().getName())
                .ifPresent(reward -> reward.accept(event.getPlayer()));
    }

    @Override
    public @NonNull RPets getPetRef() {
        return kernel;
    }
}
