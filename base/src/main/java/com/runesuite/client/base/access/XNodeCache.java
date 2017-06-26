package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import org.jetbrains.annotations.NotNull;

/**
 * public final class
 */
public interface XNodeCache extends Accessor {
    @NotNull
    MethodExecution clear = new MethodExecution();

    @NotNull
    MethodExecution get = new MethodExecution();

    @NotNull
    MethodExecution put = new MethodExecution();

    @NotNull
    MethodExecution remove = new MethodExecution();

    /**
     *  field
     */
    int getCapacity();

    /**
     *  field
     */
    void setCapacity(int value);

    /**
     *  field
     */
    XCacheNodeDeque getDeque();

    /**
     *  field
     */
    void setDeque(XCacheNodeDeque value);

    /**
     *  field
     */
    XNodeHashTable getHashTable();

    /**
     *  field
     */
    void setHashTable(XNodeHashTable value);

    /**
     *  field
     */
    int getRemainingCapacity();

    /**
     *  field
     */
    void setRemainingCapacity(int value);

    /**
     * public method
     */
    void clear();

    /**
     * public method
     */
    XCacheNode get(long key);

    /**
     * public method
     */
    void put(XCacheNode cacheNode, long key);

    /**
     * public method
     */
    void remove(long key);
}
