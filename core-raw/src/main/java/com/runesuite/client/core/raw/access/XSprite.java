package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;

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
