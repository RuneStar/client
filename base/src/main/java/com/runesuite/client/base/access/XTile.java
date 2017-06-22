package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

public interface XTile extends Accessor, XNode {
    @Field
    XBoundaryObject getBoundaryObject();

    @Field
    void setBoundaryObject(XBoundaryObject value);

    @Field
    XFloorDecoration getFloorDecoration();

    @Field
    void setFloorDecoration(XFloorDecoration value);

    @Field
    XGameObject[] getGameObjects();

    @Field
    void setGameObjects(XGameObject[] value);

    @Field
    XGroundItemPile getGroundItemPile();

    @Field
    void setGroundItemPile(XGroundItemPile value);

    @Field
    int getPlane();

    @Field
    void setPlane(int value);

    @Field
    XWallDecoration getWallDecoration();

    @Field
    void setWallDecoration(XWallDecoration value);

    @Field
    int getX();

    @Field
    void setX(int value);

    @Field
    int getY();

    @Field
    void setY(int value);
}
