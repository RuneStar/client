package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import org.jetbrains.annotations.NotNull;

/**
 * public final class
 */
public interface XGraphicsProvider extends Accessor, XAbstractGraphicsProvider {
    @NotNull
    MethodExecution draw = new MethodExecution();

    @NotNull
    MethodExecution draw0 = new MethodExecution();

    @NotNull
    MethodExecution drawFull = new MethodExecution();

    @NotNull
    MethodExecution drawFull0 = new MethodExecution();

    /**
     *  field
     */
    Component getComponent();

    /**
     *  field
     */
    void setComponent(Component value);

    /**
     *  field
     */
    Image getImage();

    /**
     *  field
     */
    void setImage(Image value);

    /**
     * public final method
     */
    void draw(int x, int y, int width, int height);

    /**
     * final method
     */
    void draw0(Graphics graphics, int x, int y, int width, int height);

    /**
     * public final method
     */
    void drawFull(int x, int y);

    /**
     * final method
     */
    void drawFull0(Graphics graphics, int x, int y);
}
