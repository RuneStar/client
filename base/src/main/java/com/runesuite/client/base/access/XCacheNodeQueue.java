package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import java.lang.Iterable;

public interface XCacheNodeQueue extends Accessor, Iterable {
    @Field
    XCacheNode getSentinel();

    @Field
    void setSentinel(XCacheNode value);
}
