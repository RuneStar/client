package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

public interface XCacheNodeDeque extends Accessor {
    @Field
    XCacheNode getSentinel();

    @Field
    void setSentinel(XCacheNode value);
}
