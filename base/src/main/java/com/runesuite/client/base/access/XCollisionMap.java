package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

public interface XCollisionMap extends Accessor {
    @Field
    int[][] getFlags();

    @Field
    void setFlags(int[][] value);
}
