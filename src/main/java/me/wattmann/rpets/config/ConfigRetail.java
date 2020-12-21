package me.wattmann.rpets.config;

import lombok.NonNull;
import me.wattmann.rpets.RPets;
import me.wattmann.rpets.imp.RPetsComponent;
import me.wattmann.rpets.model.Reward;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConfigRetail implements RPetsComponent {

    @NonNull
    private final RPets kernel;

    @NonNull
    private final FileConfiguration configuration;

    public ConfigRetail(@NonNull RPets kernel) {
        this.kernel = kernel;
        this.configuration = kernel.getConfig();
    }

    public <T> Optional<T> get(Class<T> type, @NotNull String... paths) {
        String path = String.join(".", paths);
        if(configuration.contains(path))
            return Optional.ofNullable(configuration.getObject(path, type));
        else
            return Optional.empty();
    }

    public @NonNull Optional<Reward> getReward(@NonNull int lvl, @NotNull String pet) {
        return getReward(String.valueOf(lvl), pet);
    }

    public @NonNull Optional<Reward> getReward(@NonNull String lvl, @NotNull String pet) {
        try {
            List<String> commands = kernel.getConfig().getStringList("rewards." + pet + "." + lvl);

            if (commands.isEmpty())
                commands = kernel.getConfig().getStringList("rewards." + lvl);
            if (commands.isEmpty())
                commands = new ArrayList<>();

            List<String> general = kernel.getConfig().getStringList("rewards.general");

            if (general.isEmpty())
                return Optional.empty();

            commands.addAll(general);

            var reward = new Reward(commands, kernel.getBukkitDispatcher(), lvl);
            return Optional.of(reward);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public @NonNull RPets getPetRef() {
        return kernel;
    }
}
