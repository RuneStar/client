package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;

/**
 * public final class
 */
public interface XTile extends Accessor, XNode {
    /**
     *  field
     */
    XBoundaryObject getBoundaryObject();

    /**
     *  field
     */
    void setBoundaryObject(XBoundaryObject value);

    /**
     *  field
     */
    XFloorDecoration getFloorDecoration();

    /**
     *  field
     */
    void setFloorDecoration(XFloorDecoration value);

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
    XGroundItemPile getGroundItemPile();

    /**
     *  field
     */
    void setGroundItemPile(XGroundItemPile value);

    /**
     *  field
     */
    int getPlane();

    /**
     *  field
     */
    void setPlane(int value);

    /**
     *  field
     */
    XWallDecoration getWallDecoration();

    /**
     *  field
     */
    void setWallDecoration(XWallDecoration value);

    /**
     *  field
     */
    int getX();

    /**
     *  field
     */
    void setX(int value);

    /**
     *  field
     */
    int getY();

    /**
     *  field
     */
    void setY(int value);
}
