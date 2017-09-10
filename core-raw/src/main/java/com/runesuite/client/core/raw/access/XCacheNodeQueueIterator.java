package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;
import java.util.Iterator;

/**
 * public class
 */
public interface XCacheNodeQueueIterator extends Accessor, Iterator {
    /**
     *  field
     */
    XCacheNodeQueue getQueue();

    /**
     *  field
     */
    void setQueue(XCacheNodeQueue value);
}
