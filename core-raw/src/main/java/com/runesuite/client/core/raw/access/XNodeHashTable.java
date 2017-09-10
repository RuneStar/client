package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;
import org.jetbrains.annotations.NotNull;

/**
 * public final class
 */
public interface XNodeHashTable extends Accessor {
    @NotNull
    MethodExecution clear = new MethodExecution();

    @NotNull
    MethodExecution first = new MethodExecution();

    @NotNull
    MethodExecution get = new MethodExecution();

    @NotNull
    MethodExecution next = new MethodExecution();

    @NotNull
    MethodExecution put = new MethodExecution();

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

    /**
     *  method
     */
    void clear();

    /**
     * public method
     */
    XNode first();

    /**
     * public method
     */
    XNode get(long key);

    /**
     * public method
     */
    XNode next();

    /**
     * public method
     */
    void put(XNode node, long key);
}
