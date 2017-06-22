package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

public interface XAbstractGraphicsProvider extends Accessor {
    @Method
    void draw(int x, int y, int width, int height);

    @Method
    void drawFull(int x, int y);
}
