package me.wattmann.rpets.tuples;

import lombok.Getter;
import lombok.NonNull;

import javax.annotation.Nullable;
import java.util.Optional;

@Getter public class Pair<KT, VT>
{
    @NonNull
    protected KT key;
    @Nullable
    protected VT value;

    public Pair(@NonNull KT key, @Nullable VT value) {
        this.key = key;
        this.value = value;
    }

    public Optional<VT> getValueOpt() {
        return Optional.ofNullable(value);
    }
}
