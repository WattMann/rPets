package me.wattmann.rpets.executors;

import com.kirelcodes.miniaturepets.pets.PetManager;
import lombok.NonNull;
import me.wattmann.rpets.RPets;
import me.wattmann.rpets.imp.RPetsComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;


public final class KernelExecutor implements RPetsComponent {

    @NonNull protected final RPets kernel;

    /**
     * Default constructor
     * @param kernel non-null reference to kernel
     * */
    public KernelExecutor(@NonNull RPets kernel) {
        this.kernel = kernel;

        PluginCommand command;
        if((command = kernel.getCommand("petxp")) != null)
            command.setExecutor(this::onCommand);
         else
            kernel.getLogback().logError("Failed to initialize command executor.", null);

    }

    private boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if(sender instanceof Player) {
            PetManager.getPetOptional(((Player) sender)).ifPresentOrElse(pet -> {
                System.out.println(pet.getID());
            }, () -> {
                System.out.println("no lol");
            });
        }
        return true;
    }

    @Override
    public @NonNull RPets getPetRef() {
        return kernel;
    }
}
