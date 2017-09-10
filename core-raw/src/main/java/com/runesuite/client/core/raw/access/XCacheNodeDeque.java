package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;

/**
 * public final class
 */
public interface XCacheNodeDeque extends Accessor {
    /**
     *  field
     */
    XCacheNode getSentinel();

    /**
     *  field
     */
    void setSentinel(XCacheNode value);
}
