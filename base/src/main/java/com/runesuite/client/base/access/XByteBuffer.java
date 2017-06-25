package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

/**
 * public class
 */
public interface XByteBuffer extends Accessor, XNode {
    /**
     * public field
     */
    byte[] getBuffer();

    /**
     * public field
     */
    void setBuffer(byte[] value);

    /**
     * public field
     */
    int getIndex();

    /**
     * public field
     */
    void setIndex(int value);
}
