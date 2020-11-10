package me.wattmann.rpets;

import lombok.NonNull;
import me.wattmann.rpets.imp.RPetsComponent;

public final class ExperienceSystem implements RPetsComponent
{
    @NonNull protected final RPets pets;

    private int base;
    private double exponent;


    public ExperienceSystem(@NonNull RPets pets) {
        this.pets = pets;
    }

    @Override
    public void init() throws Exception {
        this.base = getPetRef().getConfigRetail().get(Integer.class,"experience.base",  1000);
        this.exponent = getPetRef().getConfigRetail().get(Double.class,"experience.exponent", 0.25d);
    }

    public int level(long xp) {
        int lvl = 0;
        for(int i = 1; experience(i) <= xp; i++)
            lvl += 1;
        return lvl;
    }

    public long experience(long lvl) {
        long required = 0;
        for (int i = 1; i <= lvl; i++)
            required += i * (1000 * 0.25f) + 1000;
        return required;
    }

    @Override
    public @NonNull RPets getPetRef() {
        return pets;
    }
}
