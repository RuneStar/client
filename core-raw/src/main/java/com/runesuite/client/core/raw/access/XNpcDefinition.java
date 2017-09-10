package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;
import java.lang.String;

/**
 * public class
 */
public interface XNpcDefinition extends Accessor, XCacheNode {
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
