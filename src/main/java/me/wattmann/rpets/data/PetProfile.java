package me.wattmann.rpets.data;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Builder
public class PetProfile {

    @Getter
    @NotNull
    private String name;

    @Getter
    private int base;
    @Getter
    private double exponent;

    @Getter
    private int level;
    @Getter
    private long experience;

    protected synchronized boolean setVal(@NonNull long val) {
        this.experience = val;
        return calc();
    }

    protected boolean calc() {
        boolean result = false;
        var recalculated = this.level(experience);
        if (recalculated > level) {
            result = true;
            this.level = recalculated;
        }
        return result;
    }

    public int level(final long exp) {
        if(exp == 0)
            return 0;
        int lvl = 0;
        for (int i = 1; experience(i) <= exp; i++)
            lvl += 1;
        return lvl;
    }

    public long experience(final long lvl) {
        long required = 0;
        for (int i = 1; i <= lvl; i++)
            required += i * (base * exponent) + base;
        return required;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PetProfile)) return false;
        PetProfile that = (PetProfile) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public String toString() {
        return "PetProfile{" +
                "name='" + name + '\'' +
                ", level=" + level +
                ", experience=" + experience +
                ", base=" + base +
                ", exponent=" + exponent +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, level, experience);
    }
}
