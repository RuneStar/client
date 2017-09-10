package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;

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
