package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
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
