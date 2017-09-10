package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;
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
