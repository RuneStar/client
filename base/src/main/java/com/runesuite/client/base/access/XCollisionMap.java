package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

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
