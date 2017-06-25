package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

/**
 * public class
 */
public interface XPlayerAppearance extends Accessor {
    /**
     *  field
     */
    int[] getBodyColors();

    /**
     *  field
     */
    void setBodyColors(int[] value);

    /**
     *  field
     */
    int[] getEquipment();

    /**
     *  field
     */
    void setEquipment(int[] value);

    /**
     * public field
     */
    int getId();

    /**
     * public field
     */
    void setId(int value);

    /**
     * public field
     */
    boolean getIsFemale();

    /**
     * public field
     */
    void setIsFemale(boolean value);
}
