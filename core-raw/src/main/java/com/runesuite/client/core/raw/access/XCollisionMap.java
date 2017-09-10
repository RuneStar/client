package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;

/**
 * public class
 */
public interface XCollisionMap extends Accessor {
    /**
     * public field
     */
    int[][] getFlags();

    /**
     * public field
     */
    void setFlags(int[][] value);
}
