package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;

/**
 * public class
 */
public interface XItemContainer extends Accessor, XNode {
    /**
     *  field
     */
    int[] getIds();

    /**
     *  field
     */
    void setIds(int[] value);

    /**
     *  field
     */
    int[] getQuantities();

    /**
     *  field
     */
    void setQuantities(int[] value);
}
