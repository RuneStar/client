package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import java.awt.Component;
import java.awt.event.MouseWheelListener;
import org.jetbrains.annotations.NotNull;

/**
 * public final class
 */
public interface XMouseWheelHandler extends Accessor, XMouseWheel, MouseWheelListener {
    @NotNull
    MethodExecution addTo = new MethodExecution();

    @NotNull
    MethodExecution removeFrom = new MethodExecution();

    @NotNull
    MethodExecution useRotation = new MethodExecution();

    /**
     *  field
     */
    int getRotation();

    /**
     *  field
     */
    void setRotation(int value);

    /**
     *  method
     */
    void addTo(Component component);

    /**
     *  method
     */
    void removeFrom(Component component);

    /**
     * public synchronized method
     */
    int useRotation();
}
