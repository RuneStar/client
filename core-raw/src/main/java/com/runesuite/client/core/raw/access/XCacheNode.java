package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;
import org.jetbrains.annotations.NotNull;

/**
 * public class
 */
public interface XCacheNode extends Accessor, XNode {
    @NotNull
    MethodExecution cacheRemove = new MethodExecution();

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
}
