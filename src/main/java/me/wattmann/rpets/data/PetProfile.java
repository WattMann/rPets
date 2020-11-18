package me.wattmann.rpets.data;

import lombok.Getter;
import lombok.NonNull;
import me.wattmann.rpets.RPetsSystem;
import me.wattmann.rpets.tuples.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

public class PetProfile extends Pair<String, Long>
{
    @Getter private int level = 0;

    public PetProfile(@NonNull String key, @Nullable long value) {
        super(key, value);
        calc();
    }

    protected synchronized boolean setVal(@NonNull long val) {
        this.value = val;
        return calc();
    }

    private boolean calc() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        getValueOpt().ifPresent((val) -> {
            var i = RPetsSystem.level(value);
            if(i > level) {
                atomicBoolean.set(true);
                this.level = i;
            }
        });
        return atomicBoolean.get();
    }

}
