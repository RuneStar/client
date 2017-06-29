package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import java.lang.String;

/**
 * public class
 */
public interface XFriend extends Accessor {
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
    String getPreviousName();

    /**
     *  field
     */
    void setPreviousName(String value);

    /**
     *  field
     */
    int getRank();

    /**
     *  field
     */
    void setRank(int value);

    /**
     *  field
     */
    int getWorld();

    /**
     *  field
     */
    void setWorld(int value);
}
