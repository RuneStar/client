package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import java.util.Iterator;

public interface XCacheNodeQueueIterator extends Accessor, Iterator {
    @Field
    XCacheNodeQueue getQueue();

    @Field
    void setQueue(XCacheNodeQueue value);
}
