package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import java.lang.Iterable;

/**
 * public class
 */
public interface XCacheNodeQueue extends Accessor, Iterable {
    /**
     * public field
     */
    XCacheNode getSentinel();

    /**
     * public field
     */
    void setSentinel(XCacheNode value);
}
