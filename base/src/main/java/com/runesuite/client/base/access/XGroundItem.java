package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import org.jetbrains.annotations.NotNull;

/**
 * public final class
 */
public interface XGroundItem extends Accessor, XEntity {
    @NotNull
    MethodExecution getModel = new MethodExecution();

    /**
     *  field
     */
    int getId();

    /**
     *  field
     */
    void setId(int value);

    /**
     *  field
     */
    int getQuantity();

    /**
     *  field
     */
    void setQuantity(int value);

    /**
     * protected final method
     */
    XModel getModel();
}
