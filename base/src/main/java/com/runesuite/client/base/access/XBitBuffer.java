package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

public interface XBitBuffer extends Accessor, XByteBuffer {
    @Field
    int getBitIndex();

    @Field
    void setBitIndex(int value);

    @Field
    XIsaacCipher getIsaacCipher();

    @Field
    void setIsaacCipher(XIsaacCipher value);
}
