package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

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
