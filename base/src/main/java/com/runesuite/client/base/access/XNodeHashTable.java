package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

/**
 * public final class
 */
public interface XNodeHashTable extends Accessor {
    /**
     *  field
     */
    XNode[] getBuckets();

    /**
     *  field
     */
    void setBuckets(XNode[] value);

    /**
     *  field
     */
    XNode getCurrent();

    /**
     *  field
     */
    void setCurrent(XNode value);

    /**
     *  field
     */
    XNode getCurrentGet();

    /**
     *  field
     */
    void setCurrentGet(XNode value);

    /**
     *  field
     */
    int getIndex();

    /**
     *  field
     */
    void setIndex(int value);

    /**
     *  field
     */
    int getSize();

    /**
     *  field
     */
    void setSize(int value);
}
