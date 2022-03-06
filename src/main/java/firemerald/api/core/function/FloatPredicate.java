package firemerald.api.core.function;

import java.util.Objects;

@FunctionalInterface
public interface FloatPredicate
{
	public boolean test(final float p0);

	public default FloatPredicate and(final FloatPredicate floatPredicate)
    {
        Objects.requireNonNull(floatPredicate);
        return n -> this.test(n) && floatPredicate.test(n);
    }

	public default FloatPredicate negate()
    {
        return n -> !this.test(n);
    }

	public default FloatPredicate or(final FloatPredicate floatPredicate)
    {
        Objects.requireNonNull(floatPredicate);
        return n -> this.test(n) || floatPredicate.test(n);
    }
}
