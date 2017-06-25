package com.runesuite.client.base.access;

import com.jakewharton.rxrelay2.PublishRelay;
import com.runesuite.client.base.Accessor;
import com.runesuite.client.base.MethodEvent;
import org.jetbrains.annotations.NotNull;

/**
 * public class
 */
public interface XNode extends Accessor {
    /**
     *  field
     */
    XNode getNext();

    /**
     *  field
     */
    void setNext(XNode value);

    /**
     * public field
     */
    XNode getPrevious();

    /**
     * public field
     */
    void setPrevious(XNode value);

    /**
     * public field
     */
    long getUid();

    /**
     * public field
     */
    void setUid(long value);

    /**
     * public method
     */
    boolean hasNext();

    /**
     * public method
     */
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
