package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

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
