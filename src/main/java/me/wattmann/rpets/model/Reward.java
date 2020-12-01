package me.wattmann.rpets.model;

import me.wattmann.concurrent.BukkitDispatcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reward implements Consumer<Player> {

    public static final String COMMAND_PATTERN = "(..*);(..*)";
    private Set<Consumer<Player>> commands = new HashSet<>();

    public Reward(@NotNull final List<String> commands, @NotNull BukkitDispatcher dispatcher)
    {
        for (String command : commands) {
            Matcher matcher = Pattern.compile(COMMAND_PATTERN).matcher(command);
            if(!matcher.matches())
                throw new IllegalArgumentException("Invalid command " + command);

            String label = matcher.group(1);
            String body = matcher.group(2);

            String[] params;

            if(label.equals("msg")) {
                this.commands.add((player) -> {
                    dispatcher.execute(() -> {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', body));
                    }, false);
                });
            }

            if(label.equals("cmd")) {
                System.out.println("adding cmd");
                this.commands.add(player -> {
                    final String cmd = body.replaceAll("\\{PLAYER}", player.getDisplayName());;
                    dispatcher.execute(() -> {
                        try {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, false);
                });
            }

            if(label.equals("sound")) {
                this.commands.add(player -> {
                   final Sound sound = Sound.valueOf(body);
                   dispatcher.execute(() -> {
                       player.getWorld().playSound(player.getLocation(), sound, 1f, 1f);
                   }, false);
                });
            }

        }
    }

    @Override
    public void accept(@NotNull Player player) {
        commands.forEach(playerConsumer -> {
            playerConsumer.accept(player);
        });
    }
}
