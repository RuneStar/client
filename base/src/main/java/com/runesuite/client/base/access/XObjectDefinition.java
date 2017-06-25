package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import java.lang.String;

/**
 * public class
 */
public interface XObjectDefinition extends Accessor, XCacheNode {
    /**
     * public field
     */
    String[] getActions();

    /**
     * public field
     */
    void setActions(String[] value);

    /**
     * public field
     */
    String getName();

    /**
     * public field
     */
    void setName(String value);
}
