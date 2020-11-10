package me.wattmann.rpets;

import lombok.NonNull;
import me.wattmann.rpets.imp.RPetsComponent;

public final class ExperienceSystem implements RPetsComponent
{
    @NonNull protected final RPets pets;

    private int base = 1000;
    private double exponent = 0.25d;


    public ExperienceSystem(@NonNull RPets pets) {
        this.pets = pets;
    }

    @Override
    public void init() throws Exception {
        if(!getPetRef().getConfig().contains("experience.base"))
            getPetRef().getLogback().logWarn("Key experience.base not found, using default value");
        if(!getPetRef().getConfig().contains("experience.exponent"))
            getPetRef().getLogback().logWarn("Key experience.exponent not found, using default value");
        //TODO: config wrapper probably
        this.base = getPetRef().getConfig().getInt("experience.base", 1000);
        this.exponent = getPetRef().getConfig().getDouble("experience.exponent", 0.25d);
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
