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
     * The arguments used to call the method, boxing primitive values. Should not be modified, modifications will have
     * no effect.
     */
    Object[] getArguments();

    /**
     * An event representing the entrance to a method.
     *
     * @param <I> the instance type the method is declared on, {@link Void} for {@code static} methods
     */
    interface Enter<I> extends MethodEvent<I> {

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
    }

    /**
     * For internal use only.
     */
    class Implementation<I, R> implements MethodEvent.Enter<I>, MethodEvent.Exit<I, R> {

        public final I instance;

        @NotNull
        public final Object[] arguments;

        public R returned;

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
        public String toString() {
            return "MethodEvent(" +
                    "instance=" + instance +
                    ", arguments=" + Arrays.toString(arguments) +
                    ", returned=" + returned +
                    ')';
        }
    }
}