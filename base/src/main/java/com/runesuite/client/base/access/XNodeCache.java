package com.runesuite.client.base.access;

import com.jakewharton.rxrelay2.PublishRelay;
import com.runesuite.client.base.Accessor;
import com.runesuite.client.base.MethodEvent;
import org.jetbrains.annotations.NotNull;

public interface XNodeCache extends Accessor {
    @Field
    int getCapacity();

    @Field
    void setCapacity(int value);

    @Field
    XCacheNodeDeque getDeque();

    @Field
    void setDeque(XCacheNodeDeque value);

    @Field
    XNodeHashTable getHashTable();

    @Field
    void setHashTable(XNodeHashTable value);

    @Field
    int getRemainingCapacity();

    @Field
    void setRemainingCapacity(int value);

    @Method
    void clear();

    @Method
    XCacheNode get(long key);

    @Method
    void put(XCacheNode cacheNode, long key);

    @Method
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
