package com.runesuite.client.base.access;

import com.jakewharton.rxrelay2.PublishRelay;
import com.runesuite.client.base.Accessor;
import com.runesuite.client.base.MethodEvent;
import org.jetbrains.annotations.NotNull;

/**
 * public final class
 */
public interface XNodeCache extends Accessor {
    /**
     *  field
     */
    int getCapacity();

    /**
     *  field
     */
    void setCapacity(int value);

    /**
     *  field
     */
    XCacheNodeDeque getDeque();

    /**
     *  field
     */
    void setDeque(XCacheNodeDeque value);

    /**
     *  field
     */
    XNodeHashTable getHashTable();

    /**
     *  field
     */
    void setHashTable(XNodeHashTable value);

    /**
     *  field
     */
    int getRemainingCapacity();

    /**
     *  field
     */
    void setRemainingCapacity(int value);

    /**
     * public method
     */
    void clear();

    /**
     * public method
     */
    XCacheNode get(long key);

    /**
     * public method
     */
    void put(XCacheNode cacheNode, long key);

    /**
     * public method
     */
    void remove(long key);

    final class clear {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private clear() {
        }
    }

    final class get {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private get() {
        }
    }

    final class put {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private put() {
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
