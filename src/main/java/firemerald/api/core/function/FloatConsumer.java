package firemerald.api.core.function;

import java.util.Objects;

@FunctionalInterface
public interface FloatConsumer
{
    void accept(final float f);

    default FloatConsumer andThen(final FloatConsumer floatConsumer)
    {
        Objects.requireNonNull(floatConsumer);
        return f ->
        {
            this.accept(f);
            floatConsumer.accept(f);
        };
    }
}
