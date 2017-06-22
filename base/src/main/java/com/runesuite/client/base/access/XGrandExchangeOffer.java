package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

public interface XGrandExchangeOffer extends Accessor {
    @Field
    byte getState();

    @Field
    void setState(byte value);
}
