package com.runesuite.client.base.access;

import com.jakewharton.rxrelay2.PublishRelay;
import com.runesuite.client.base.Accessor;
import com.runesuite.client.base.MethodEvent;
import org.jetbrains.annotations.NotNull;

/**
 * public class
 */
public interface XNodeDeque extends Accessor {
    /**
     *  field
     */
    XNode getCurrent();

    /**
     *  field
     */
    void setCurrent(XNode value);

    /**
     * public field
     */
    XNode getSentinel();

    /**
     * public field
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
    void clear();

    /**
     * public method
     */
    XNode first();

    /**
     * public method
     */
    XNode last();

    /**
     * public method
     */
    XNode next();

    /**
     * public method
     */
    XNode previous();

    /**
     * public method
     */
    XNode removeFirst();

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

    final class clear {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private clear() {
        }
    }

    final class first {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private first() {
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

    final class next {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private next() {
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

    final class removeFirst {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private removeFirst() {
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
