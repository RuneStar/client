package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import org.jetbrains.annotations.NotNull;

/**
 * public abstract class
 */
public interface XEntity extends Accessor, XCacheNode {
    @NotNull
    MethodExecution getModel = new MethodExecution();

    /**
     * public field
     */
    int getHeight();

    /**
     * public field
     */
    void setHeight(int value);

    /**
     * protected method
     */
    XModel getModel();
}
