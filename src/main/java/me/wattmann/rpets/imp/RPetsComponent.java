package me.wattmann.rpets.imp;

import lombok.NonNull;
import me.wattmann.rpets.RPets;

public interface RPetsComponent
{
    /**
     * Used to initialize this object.
     *
     * @throws Exception in case something goes wrong
     * */
    default void init() throws Exception {};

    /**
     * Used to terminate this object.
     *
     * @throws Exception in case something goes wrong
     * */
    default void term() throws Exception {};

    /**
     * Returns a reference to the plugin's kernel, non-null ofc.
     * */
    @NonNull RPets getPetRef();
}
