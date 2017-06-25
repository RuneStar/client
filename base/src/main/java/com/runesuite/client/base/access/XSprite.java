package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

/**
 * public final class
 */
public interface XSprite extends Accessor, XRasterizer2D {
    /**
     * public field
     */
    int[] getPixels();

    /**
     * public field
     */
    void setPixels(int[] value);
}
