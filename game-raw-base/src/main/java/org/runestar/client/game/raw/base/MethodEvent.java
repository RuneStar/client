package org.runestar.client.game.raw.base;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * An event representing a point in the execution of a method.
 *
 * Changes made to {@link #arguments} before the method is executed will replace the originals.
 * Changes made to {@link #returned} or {@link #thrown} before the method is returned from will replace the originals.
 *
 * @param <I> the instance type the method is declared on, {@link Void} for {@code static} methods
 * @param <R> the return type of the method, wrappers for primitives, {@link Void} for {@code void} methods
 */
public final class MethodEvent<I, R> {

    /**
     * The instance the method was called on, {@code null} for {@code static} methods.
     */
    public final I instance;

    /**
     * The arguments used to call the method, boxing primitives.
     */
    @NotNull
    public final Object[] arguments;

    /**
     * Whether the method body should be skipped, default is {@code false}.
     */
    public boolean skipBody = false;

    /**
     * The value returned from the method, boxing primitives, {@code null} for {@code void} methods, {@code null} if
     * the method has not yet returned.
     */
    public R returned = null;

    /**
     * The exception thrown by the method, {@code null} if there was no exception, {@code null} if the method has not
     * yet returned.
     */
    public Throwable thrown = null;

    public MethodEvent(I instance, @NotNull Object[] arguments) {
        this.instance = instance;
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "MethodEvent(instance=" + instance +
                ", arguments=" + Arrays.toString(arguments) +
                ", skipBody=" + skipBody +
                ", returned=" + returned +
                ", thrown=" + thrown +
                ')';
    }
}