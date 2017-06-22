package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

public interface XScene extends Accessor {
    @Field
    XGameObject[] getGameObjects();

    @Field
    void setGameObjects(XGameObject[] value);

    @Field
    XTile[][][] getTiles();

    @Field
    void setTiles(XTile[][][] value);
}
