package com.runesuite.client.base.access;

import com.jakewharton.rxrelay2.PublishRelay;
import com.runesuite.client.base.Accessor;
import com.runesuite.client.base.MethodEvent;
import org.jetbrains.annotations.NotNull;

public interface XCacheNode extends Accessor, XNode {
    @Field
    XCacheNode getCacheNext();

    @Field
    void setCacheNext(XCacheNode value);

    @Field
    XCacheNode getCachePrevious();

    @Field
    void setCachePrevious(XCacheNode value);

    @Method
    void cacheRemove();

    final class cacheRemove {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private cacheRemove() {
        }
    }
}
