package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

public interface XByteBuffer extends Accessor, XNode {
    @Field
    byte[] getBuffer();

    @Field
    void setBuffer(byte[] value);

    @Field
    int getIndex();

    @Field
    void setIndex(int value);
}
