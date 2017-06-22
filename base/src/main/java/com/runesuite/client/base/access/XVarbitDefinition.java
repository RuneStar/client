package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

public interface XVarbitDefinition extends Accessor, XCacheNode {
    @Field
    int getFirst();

    @Field
    void setFirst(int value);

    @Field
    int getIndex();

    @Field
    void setIndex(int value);

    @Field
    int getLast();

    @Field
    void setLast(int value);
}
