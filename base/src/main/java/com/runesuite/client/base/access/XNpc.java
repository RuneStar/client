package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import org.jetbrains.annotations.NotNull;

/**
 * public final class
 */
public interface XNpc extends Accessor, XActor {
    @NotNull
    MethodExecution getModel = new MethodExecution();

    @NotNull
    MethodExecution isVisible = new MethodExecution();

    /**
     *  field
     */
    XNpcDefinition getDefinition();

    /**
     *  field
     */
    void setDefinition(XNpcDefinition value);

    /**
     * protected final method
     */
    XModel getModel();

    /**
     * final method
     */
    boolean isVisible();
}
