package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;

/**
 * public class
 */
public interface XVarbitDefinition extends Accessor, XCacheNode {
    /**
     * public field
     */
    int getFirst();

    /**
     * public field
     */
    void setFirst(int value);

    /**
     * public field
     */
    int getIndex();

    /**
     * public field
     */
    void setIndex(int value);

    /**
     * public field
     */
    int getLast();

    /**
     * public field
     */
    void setLast(int value);
}
