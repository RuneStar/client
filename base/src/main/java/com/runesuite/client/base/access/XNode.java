package com.runesuite.client.base.access;

import com.jakewharton.rxrelay2.PublishRelay;
import com.runesuite.client.base.Accessor;
import com.runesuite.client.base.MethodEvent;
import org.jetbrains.annotations.NotNull;

public interface XNode extends Accessor {
    @Field
    XNode getNext();

    @Field
    void setNext(XNode value);

    @Field
    XNode getPrevious();

    @Field
    void setPrevious(XNode value);

    @Field
    long getUid();

    @Field
    void setUid(long value);

    @Method
    boolean hasNext();

    @Method
    void remove();

    final class hasNext {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private hasNext() {
        }
    }

    final class remove {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private remove() {
        }
    }
}
