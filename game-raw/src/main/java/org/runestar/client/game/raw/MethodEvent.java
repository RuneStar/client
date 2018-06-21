package org.runestar.client.game.raw;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * An event representing a point in the execution of a method.
 *
 * @param <I> the instance type the method is declared on, {@link Void} for {@code static} methods
 */
public interface MethodEvent<I> {

    /**
     * The instance the method was called on, {@code null} for {@code static} methods.
     */
    I getInstance();

    /**
     * The arguments used to call the method, boxing primitive values, can be modified.
     */
    @NotNull
    Object[] getArguments();

    /**
     * Whether the method body should be skipped or not, default is {@code false}.
     */
    boolean getSkipBody();

    /**
     * An event representing the entrance to a method.
     *
     * @param <I> the instance type the method is declared on, {@link Void} for {@code static} methods
     */
    interface Enter<I> extends MethodEvent<I> {

        void setSkipBody(boolean skipBody);
    }

    /**
     * An event representing the exit from a method.
     *
     * @param <I> the instance type the method is declared on, {@link Void} for {@code static} methods
     * @param <R> the return type of the method, wrappers for primitives, {@link Void} for {@code void} methods
     */
    interface Exit<I, R> extends MethodEvent<I> {

        /**
         * The value returned from the method, boxing primitives, {@code null} for {@code void} methods.
         */
        R getReturned();

        void setReturned(R returned);

        /**
         * The exception thrown by the method, {@code null} if there was no exception.
         */
        Throwable getThrown();

        void setThrown(Throwable thrown);
    }

    /**
     * For internal use only.
     */
    final class Implementation<I, R> implements MethodEvent.Enter<I>, MethodEvent.Exit<I, R> {

        public final I instance;

        @NotNull
        public final Object[] arguments;

        public R returned;

        public Throwable thrown;

        private boolean skipBody;

        public Implementation(I instance, @NotNull Object[] arguments) {
            this.instance = instance;
            this.arguments = arguments;
        }

        @Override
        public I getInstance() {
            return instance;
        }

        @NotNull
        @Override
        public Object[] getArguments() {
            return arguments;
        }

        @Override
        public R getReturned() {
            return returned;
        }

        @Override
        public void setReturned(R returned) {
            this.returned = returned;
        }

        @Override
        public Throwable getThrown() {
            return thrown;
        }

        @Override
        public void setThrown(Throwable thrown) {
            this.thrown = thrown;
        }

        @Override
        public boolean getSkipBody() {
            return false;
        }

        @Override
        public void setSkipBody(boolean skipBody) {
            this.skipBody = skipBody;
        }

        @Override
        public String toString() {
            return "MethodEvent(instance=" + instance +
                    ", arguments=" + Arrays.toString(arguments) +
                    ", returned=" + returned +
                    ", thrown=" + thrown +
                    ", skipBody=" + skipBody +
                    ')';
        }

        @NotNull
        public Object toSkippable() {
            if (skipBody) {
                return new Object[] { this };
            } else {
                return this;
            }
        }

        @NotNull
        public static MethodEvent.Implementation<?, ?> fromSkippable(@NotNull Object o) {
            if (o instanceof Object[]) {
                o = ((Object[]) o)[0];
            }
            return (MethodEvent.Implementation) o;
        }
    }
}