package com.runesuite.client.game.raw;

import org.jetbrains.annotations.NotNull;
import java.util.Arrays;

/**
 * @param <I> the instance type the method is declared on, {@link Void} for {@code static} methods
 */
public abstract class MethodEvent<I> {

    private final long index;

    private final I instance;

    @NotNull
    private final Object[] arguments;

    private MethodEvent(long index, I instance, @NotNull Object[] arguments) {
        this.index = index;
        this.instance = instance;
        this.arguments = arguments;
    }

    /**
     * @return the instance the method was called on, {@code null} for {@code static} methods
     */
    public final I getInstance() {
        return instance;
    }

    /**
     * @return the number of times the method was called before the call this event is a part of
     */
    public final long getIndex() {
        return index;
    }

    /**
     * The arguments used to call the method, boxing primitive values. The returned array should not be
     * modified.
     *
     * @return the arguments used to call the method, boxing primitive values
     */
    @NotNull
    public final Object[] getArguments() {
        return arguments;
    }

    /**
     * @param <I> the instance type the method is declared on, {@link Void} for {@code static} methods
     */
    public final static class Enter<I> extends MethodEvent<I> {

        public Enter(long index, I instance, @NotNull Object[] arguments) {
            super(index, instance, arguments);
        }

        @Override
        public String toString() {
            return "MethodEvent.Enter(" +
                    getIndex() + ", " +
                    getInstance() + ", " +
                    Arrays.toString(getArguments()) +
                    ")";
        }
    }

    /**
     * @param <I> the instance type the method is declared on, {@link Void} for {@code static} methods
     * @param <R> the return type of the method, {@link Void} for {@code void} methods
     */
    public final static class Exit<I, R> extends MethodEvent<I> {

        private final R returned;

        public Exit(long index, I instance, @NotNull Object[] arguments, R returned) {
            super(index, instance, arguments);
            this.returned = returned;
        }

        /**
         * @return the value returned from the method, {@code null} for {@code void} methods
         */
        public R getReturned() {
            return returned;
        }

        @Override
        public String toString() {
            return "MethodEvent.Exit(" +
                    getIndex() + ", " +
                    getInstance() + ", " +
                    Arrays.toString(getArguments()) + ", " +
                    getReturned() +
                    ")";
        }
    }
}
