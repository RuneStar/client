package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;

/**
 * public final class
 */
public interface XGroundItemPile extends Accessor {
    /**
     *  field
     */
    XEntity getBottom();

    /**
     *  field
     */
    void setBottom(XEntity value);

    /**
     *  field
     */
    XEntity getMiddle();

    /**
     *  field
     */
    void setMiddle(XEntity value);

    /**
     *  field
     */
    XEntity getTop();

    /**
     *  field
     */
    void setTop(XEntity value);
}
