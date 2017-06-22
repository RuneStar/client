package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

public interface XClanMate extends Accessor, XNode {
    @Field
    byte getRank();

    @Field
    void setRank(byte value);

    @Field
    int getWorld();

    @Field
    void setWorld(int value);
}
