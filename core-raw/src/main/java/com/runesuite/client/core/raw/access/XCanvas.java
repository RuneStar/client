package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;
import java.awt.Component;
import java.awt.Graphics;
import org.jetbrains.annotations.NotNull;

/**
 * public final class
 */
public interface XCanvas extends Accessor {
    @NotNull
    MethodExecution paint = new MethodExecution();

    @NotNull
    MethodExecution update = new MethodExecution();

    /**
     *  field
     */
    Component getComponent();

    /**
     *  field
     */
    void setComponent(Component value);

    /**
     * public final method
     */
    void paint(Graphics g);

    /**
     * public final method
     */
    void update(Graphics g);
}
