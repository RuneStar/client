package com.runesuite.client.core.raw;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public abstract class MethodEvent {

    private final Object instance;

    @NotNull
    private final Object[] arguments;

    private MethodEvent(final Object instance, @NotNull final Object[] arguments) {
        this.instance = instance;
        this.arguments = arguments;
    }

    /**
     * @return null for static methods
     */
    public final Object getInstance() {
        return instance;
    }

    @NotNull
    public final Object[] getArguments() {
        return arguments;
    }

    public final static class Enter extends MethodEvent {

        public Enter(final Object instance, @NotNull final Object[] arguments) {
            super(instance, arguments);
        }

        @Override
        public String toString() {
            return "MethodEvent.Enter(" +
                    getInstance() + ", " +
                    Arrays.toString(getArguments()) +
                    ")";
        }
    }

    public final static class Exit extends MethodEvent {

        private final Object returned;

        public Exit(final Object instance, @NotNull final Object[] arguments, final Object returned) {
            super(instance, arguments);
            this.returned = returned;
        }

        /**
         * @return null for void methods
         */
        public Object getReturned() {
            return returned;
        }

        @Override
        public String toString() {
            return "MethodEvent.Exit(" +
                    getInstance() + ", " +
                    Arrays.toString(getArguments()) + ", " +
                    getReturned() +
                    ")";
        }
    }
}
