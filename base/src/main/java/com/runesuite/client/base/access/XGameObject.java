package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

/**
 * public final class
 */
public interface XGameObject extends Accessor {
    /**
     * public field
     */
    XEntity getEntity();

    /**
     * public field
     */
    void setEntity(XEntity value);

    /**
     * public field
     */
    int getId();

    /**
     * public field
     */
    void setId(int value);
}
