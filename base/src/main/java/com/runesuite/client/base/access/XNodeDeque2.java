package com.runesuite.client.base.access;

import com.jakewharton.rxrelay2.PublishRelay;
import com.runesuite.client.base.Accessor;
import com.runesuite.client.base.MethodEvent;
import java.lang.Iterable;
import org.jetbrains.annotations.NotNull;

public interface XNodeDeque2 extends Accessor, Iterable {
    @Field
    XNode getCurrent();

    @Field
    void setCurrent(XNode value);

    @Field
    XNode getSentinel();

    @Field
    void setSentinel(XNode value);

    @Method
    void addFirst(XNode node);

    @Method
    void addLast(XNode node);

    @Method
    XNode last();

    @Method
    XNode previous();

    @Method
    XNode previousOrLast(XNode node);

    @Method
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
