package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

/**
 * public class
 */
public interface XClanMate extends Accessor, XNode {
    /**
     *  field
     */
    byte getRank();

    /**
     *  field
     */
    void setRank(byte value);

    /**
     *  field
     */
    int getWorld();

    /**
     *  field
     */
    void setWorld(int value);
}
