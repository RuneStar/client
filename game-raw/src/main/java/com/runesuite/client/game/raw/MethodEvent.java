package com.runesuite.client.game.raw;

import org.jetbrains.annotations.NotNull;
import java.util.Arrays;

public abstract class MethodEvent<I> {

    private final long id;

    private final I instance;

    @NotNull
    private final Object[] arguments;

    private MethodEvent(final long id, final I instance, @NotNull final Object[] arguments) {
        this.id = id;
        this.instance = instance;
        this.arguments = arguments;
    }

    /**
     * @return null for static methods
     */
    public final I getInstance() {
        return instance;
    }

    public final long getId() {
        return id;
    }

    @NotNull
    public final Object[] getArguments() {
        return arguments;
    }

    public final static class Enter<I> extends MethodEvent<I> {

        public Enter(final long id, final I instance, @NotNull final Object[] arguments) {
            super(id, instance, arguments);
        }

        @Override
        public String toString() {
            return "MethodEvent.Enter(" +
                    getId() + ", " +
                    getInstance() + ", " +
                    Arrays.toString(getArguments()) +
                    ")";
        }
    }

    public final static class Exit<I, R> extends MethodEvent<I> {

        private final R returned;

        public Exit(final long id, final I instance, @NotNull final Object[] arguments, final R returned) {
            super(id, instance, arguments);
            this.returned = returned;
        }

        /**
         * @return null for void methods
         */
        public R getReturned() {
            return returned;
        }

        @Override
        public String toString() {
            return "MethodEvent.Exit(" +
                    getId() + ", " +
                    getInstance() + ", " +
                    Arrays.toString(getArguments()) + ", " +
                    getReturned() +
                    ")";
        }
    }
}
