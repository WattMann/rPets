package me.wattmann.rpets.data;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.sql.SQLOutput;
import java.util.Objects;

public class PetProfile {

    @Getter
    private final String name;
    @Getter
    private int level = 0;
    @Getter
    private long experience;

    @Getter
    @Setter
    private int base = 1000;
    @Getter
    @Setter
    private double exponent = 0.25d;

    public PetProfile(String name, long experience) {
        this.name = name;
        this.experience = experience;
        calc();
    }

    public PetProfile(String name, long experience, int base, double exponent) {
        this(name, experience);
        this.base = base;
        this.exponent = exponent;
        calc();
    }


    protected synchronized boolean setVal(@NonNull long val) {
        this.experience = val;
        return calc();
    }

    private boolean calc() {
        boolean result = false;
        var recalculated = this.level(experience);
        if (recalculated > level) {
            result = true;
            this.level = recalculated;
        }
        return result;
    }

    public int level(long exp) {
        int lvl = 0;
        for (int i = 1; experience(i) <= exp; i++)
            lvl += 1;
        return lvl;
    }

    public long experience(long lvl) {
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
    public int hashCode() {
        return Objects.hash(name, level, experience);
    }
}
