package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

/**
 * public abstract class
 */
public interface XAbstractGraphicsProvider extends Accessor {
    /**
     * public abstract method
     */
    void draw(int x, int y, int width, int height);

    /**
     * public abstract method
     */
    void drawFull(int x, int y);
}
