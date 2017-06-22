package com.runesuite.client.base;

import java.util.Arrays;

public abstract class MethodEvent {

    private final Object instance;

    private final Object[] arguments;

    private MethodEvent(final Object instance, final Object[] arguments) {
        this.instance = instance;
        this.arguments = arguments;
    }

    /**
     * @return null for static methods
     */
    public final Object getInstance() {
        return instance;
    }

    public final Object[] getArguments() {
        return arguments;
    }

    public final static class Enter extends MethodEvent {

        public Enter(final Object instance, final Object[] arguments) {
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

        public Exit(final Object instance, final Object[] arguments, final Object returned) {
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
