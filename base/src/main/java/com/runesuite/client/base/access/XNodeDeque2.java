package com.runesuite.client.base.access;

import com.jakewharton.rxrelay2.PublishRelay;
import com.runesuite.client.base.Accessor;
import com.runesuite.client.base.MethodEvent;
import java.lang.Iterable;
import org.jetbrains.annotations.NotNull;

/**
 * public class
 */
public interface XNodeDeque2 extends Accessor, Iterable {
    /**
     *  field
     */
    XNode getCurrent();

    /**
     *  field
     */
    void setCurrent(XNode value);

    /**
     *  field
     */
    XNode getSentinel();

    /**
     *  field
     */
    void setSentinel(XNode value);

    /**
     * public method
     */
    void addFirst(XNode node);

    /**
     * public method
     */
    void addLast(XNode node);

    /**
     * public method
     */
    XNode last();

    /**
     * public method
     */
    XNode previous();

    /**
     *  method
     */
    XNode previousOrLast(XNode node);

    /**
     * public method
     */
    XNode removeLast();

    final class addFirst {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private addFirst() {
        }
    }

    final class addLast {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private addLast() {
        }
    }

    final class last {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private last() {
        }
    }

    final class previous {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private previous() {
        }
    }

    final class previousOrLast {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private previousOrLast() {
        }
    }

    final class removeLast {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private removeLast() {
        }
    }
}
