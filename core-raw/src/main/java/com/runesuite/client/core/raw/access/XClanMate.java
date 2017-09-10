package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;
import java.lang.String;

/**
 * public class
 */
public interface XClanMate extends Accessor, XNode {
    /**
     *  field
     */
    String getName();

    /**
     *  field
     */
    void setName(String value);

    /**
     *  field
     */
    String getName2();

    /**
     *  field
     */
    void setName2(String value);

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
