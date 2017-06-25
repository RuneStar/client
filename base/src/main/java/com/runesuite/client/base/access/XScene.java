package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

/**
 * public class
 */
public interface XScene extends Accessor {
    /**
     *  field
     */
    XGameObject[] getGameObjects();

    /**
     *  field
     */
    void setGameObjects(XGameObject[] value);

    /**
     *  field
     */
    XTile[][][] getTiles();

    /**
     *  field
     */
    void setTiles(XTile[][][] value);
}
