package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

public interface XSprite extends Accessor, XRasterizer2D {
    @Field
    int[] getPixels();

    @Field
    void setPixels(int[] value);
}
