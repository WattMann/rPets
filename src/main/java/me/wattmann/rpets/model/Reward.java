package me.wattmann.rpets.model;

import me.wattmann.rpets.logback.Logback;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reward implements Consumer<Player> {

    public static final String COMMAND_PATTERN = "/(..*):(..*)/";
    private Set<Consumer<Player>> commands;


    public Reward(@NotNull final List<String> commands, @NotNull Logback logback) {
        for (String command : commands) {
            Matcher m = Pattern.compile(COMMAND_PATTERN).matcher(command);
            if (!m.matches()) {
                logback.logError("Failed to compile command " + command + " not matching pattern", null);
                continue;
            }
            if (m.groupCount() < 2) {
                logback.logError("Failed to compile command " + command + " groupcount is less that 2", null);
                continue;
            }
            String label = m.group(0);
            String[] strings = m.group(1).split(";");
            String body = strings[0];
            String[] params;
            if (strings.length > 1)
                params = strings[1].split(",");
            else params = new String[0];

            if (label.equalsIgnoreCase("cmd")) {
                this.commands.add(player -> {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), body.replaceAll("\\{PLAYER}", ChatColor.stripColor(player.getDisplayName())));
                });
                return;
            }

            if (label.equalsIgnoreCase("message")) {
                this.commands.add(player -> {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', body));
                });
                return;
            }

            if (label.equalsIgnoreCase("sound")) {
                this.commands.add(player -> {
                    float volume = 1f;
                    float pitch = 1f;
                    if (params.length >= 2)
                        try {
                            volume = Float.parseFloat(params[0]);
                            pitch = Float.parseFloat(params[1]);
                        } catch (NumberFormatException e) {
                            logback.logWarn("Failed to parse parameters for sounds, expected two floats got %s and %s", params[0], params[1]);
                        }
                    player.getLocation().getWorld().playSound(player.getLocation(), Sound.valueOf(body.toUpperCase()), volume, pitch);
                });
            }
        }
    }

    @Override
    public void accept(Player player) {
        Set.copyOf(commands).forEach(playerConsumer -> playerConsumer.accept(player));
    }
}
