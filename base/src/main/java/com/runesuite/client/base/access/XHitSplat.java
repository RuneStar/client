package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

public interface XHitSplat extends Accessor, XNode {
    @Field
    int getCycle();

    @Field
    void setCycle(int value);

    @Field
    int getHealth();

    @Field
    void setHealth(int value);

    @Field
    int getInt1();

    @Field
    void setInt1(int value);

    @Field
    int getInt2();

    @Field
    void setInt2(int value);
}
