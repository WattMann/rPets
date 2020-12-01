package me.wattmann.rpets.data;

import lombok.Getter;
import lombok.NonNull;
import me.wattmann.rpets.RPetsSystem;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PetProfile {
    @Getter
    private final String name;
    @Getter
    private int level = 0;
    @Getter
    private long experience;

    public PetProfile(@NotNull String name, long experience) {
        this.name = name;
        this.experience = experience;
        calc();
    }

    protected synchronized boolean setVal(@NonNull long val) {
        this.experience = val;
        return calc();
    }

    private boolean calc() {
        boolean result = false;
        var recalculated = RPetsSystem.level(experience);
        if (recalculated > level) {
            result = true;
            this.level = recalculated;
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PetProfile)) return false;
        PetProfile that = (PetProfile) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, level, experience);
    }
}
