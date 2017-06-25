package com.runesuite.client.base.access;

import com.jakewharton.rxrelay2.PublishRelay;
import com.runesuite.client.base.Accessor;
import com.runesuite.client.base.MethodEvent;
import org.jetbrains.annotations.NotNull;

/**
 * public class
 */
public interface XCacheNode extends Accessor, XNode {
    /**
     * public field
     */
    XCacheNode getCacheNext();

    /**
     * public field
     */
    void setCacheNext(XCacheNode value);

    /**
     * public field
     */
    XCacheNode getCachePrevious();

    /**
     * public field
     */
    void setCachePrevious(XCacheNode value);

    /**
     * public method
     */
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
