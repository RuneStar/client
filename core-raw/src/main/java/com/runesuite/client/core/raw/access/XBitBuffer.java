package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;

/**
 * public final class
 */
public interface XBitBuffer extends Accessor, XByteBuffer {
    /**
     *  field
     */
    int getBitIndex();

    /**
     *  field
     */
    void setBitIndex(int value);

    /**
     *  field
     */
    XIsaacCipher getIsaacCipher();

    /**
     *  field
     */
    void setIsaacCipher(XIsaacCipher value);
}
