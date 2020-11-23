package me.wattmann.rpets.data;

import lombok.NonNull;
import me.wattmann.concurrent.BukkitDispatcher;
import me.wattmann.rpets.RPets;
import me.wattmann.rpets.imp.RPetsComponent;
import me.wattmann.rpets.tuples.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;


public final class DataRegistry implements RPetsComponent {

    @NonNull
    private final RPets kernel;
    @NonNull
    private final Set<DataRecord> cache = Collections.synchronizedSet(new HashSet<>());
    @NonNull
    private Path data_path;

    @NonNull
    protected BukkitDispatcher bukkitDispatcher;


    public DataRegistry(@NonNull RPets kernel) {
        this.kernel = kernel;
        this.bukkitDispatcher = new BukkitDispatcher(kernel);
    }

    @Override
    public void init() throws Exception {
        this.data_path = Path.of(getPetRef().getDataFolder().toPath().toString(), "data");
        this.data_path.toFile().mkdirs();
        bukkitDispatcher.executeTicking(this::saveCachedAsync, 0L, 20 * 60 * 5);
        kernel.getLogback().logInfo("Async save callback hooked");
    }

    @Override
    public void term() throws Exception {
        kernel.getLogback().logInfo("Saving all cached data");
        cache.iterator().forEachRemaining((entry) -> {
            try {
                kernel.getLogback().logDebug("Saving %s", entry.getUuid().toString());
                writeFile(entry);
            } catch (IOException e) {
                kernel.getLogback().logError("Failed to save data for %s", e, entry.getUuid().toString());
            }
        });
        kernel.getLogback().logInfo("Saved all cached data");
    }

    /**
     * Used to make a  data-friendly string, lowercase and stripped of bukkit color, with only ascii characters allowed
     * @return a friendly String
     * */
    public static @NonNull String makeFriendly(@NonNull final String string) {
        var copy = ChatColor.stripColor(string);
        StringBuilder builder = new StringBuilder();
        for (char c : copy.toCharArray())
            if(Character.isLetterOrDigit(c))
                if (c > 0x40 && c < 0x58)
                    builder.append(Character.valueOf((char) (c + 32)));
                else builder.append(c);
        return builder.toString();
    }


    /**
     * Used to save all cached data asynchronously
     * */
    public Set<CompletableFuture<Void>> saveCachedAsync() {

        var iterator = Set.copyOf(cache).iterator();
        cache.clear();
        Set<CompletableFuture<Void>> tasks = new HashSet<>();

        while(iterator.hasNext()) {
            var data = iterator.next();
            tasks.add(saveAsync(data).whenComplete((v, throwable) -> {
                if(throwable != null)
                    kernel.getLogback().logError("Failed to save data for %s", throwable, data.getUuid().toString());
                else {
                    kernel.getLogback().logDebug("Saved data for %s", data.getUuid().toString());
                }
            }));
        }
        kernel.getLogback().logInfo("Asynchronously saved all cached data");
        return tasks;
    }

    /**
     * Asynchronously saves a {@link DataRecord} to its file
     * @param profile {@link DataRecord} to be saved
     * @return {@link CompletableFuture } of {@link Void} which completes exceptionally when writing to file was unsuccessful
     * */
    public CompletableFuture<Void> saveAsync(@NonNull DataRecord profile) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                future.orTimeout(1, TimeUnit.MINUTES);
                writeFile(profile);
                future.complete(null);
            } catch (IOException e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    public void save(@NonNull DataRecord profile) {
        try {
            writeFile(profile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Asynchronously fetches a {@link DataRecord} using a specified identifier
     *
     * @param uuid {@link UUID} identifier of the data file
     * @param create in case a file is not found, should this return a new entry?
     * @return {@link CompletableFuture } of {@link DataRecord}, when successful, null if an exception was thrown
     * or null if not found and the boolean create has been set to false
     *
     */
    public CompletableFuture<DataRecord> fetch(UUID uuid, boolean create) {
        CompletableFuture<DataRecord> future = new CompletableFuture<>();
        return CompletableFuture.supplyAsync(() -> {
            return cache.stream().filter((entry) -> {
                return entry.getUuid().equals(uuid);
            }).findFirst().orElseGet(() -> {
                DataRecord profile = null;
                try {
                    profile = readFile(uuid);
                } catch (IOException e) {
                    if (create)
                        profile = DataRecord.make(uuid);
                }
                if (profile != null)
                    cache.add(profile);
                return profile;
            });
        }).orTimeout(5, TimeUnit.SECONDS);
    }


    private @NonNull DataRecord readFile(@NonNull UUID uuid) throws IOException {
        try (InputStream in = new FileInputStream(data_path.resolve(uuid.toString() + ".bin").toFile())) {
            StringBuffer key_buffer = new StringBuffer();
            ByteBuffer val_buffer = ByteBuffer.allocate(Long.BYTES);
            Map<String, Long> data = new HashMap<>();

            boolean key = true;
            int readen;

            while((readen = in.read()) != -0x1)
            {
                if (key) {
                    if (readen == 0x0)
                        key = false;
                    else key_buffer.append((char) readen);
                } else {
                    val_buffer.put((byte) readen);
                    if(!val_buffer.hasRemaining()) {
                        //EOR
                        key = true;
                        data.put(key_buffer.toString(), val_buffer.getLong(0));
                        key_buffer.setLength(0);
                        val_buffer.clear();
                    }
                }
            }
            return DataRecord.make(uuid, data);
        }

    }

    private void writeFile(@NonNull DataRecord profile) throws IOException {
        try (OutputStream out = new FileOutputStream(data_path.resolve(profile.getUuid() + ".bin").toFile())) {
            ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
            for (Pair<String, Long> datum : profile.getData()) {
                out.write(datum.getKey().getBytes());
                out.write(0x0);
                out.write(buffer.putLong(0, datum.getValueOpt().orElse(0L)).array());
                out.flush();
            }
        }
    }

    @Override
    public @NonNull RPets getPetRef() {
        return kernel;
    }
}