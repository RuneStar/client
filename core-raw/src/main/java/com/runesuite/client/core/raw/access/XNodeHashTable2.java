package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;
import java.lang.Iterable;

/**
 * public final class
 */
public interface XNodeHashTable2 extends Accessor, Iterable {
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
