package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

public interface XBoundaryObject extends Accessor {
    @Field
    XEntity getEntity1();

    @Field
    void setEntity1(XEntity value);

    @Field
    XEntity getEntity2();

    @Field
    void setEntity2(XEntity value);

    @Field
    int getFlags();

    @Field
    void setFlags(int value);

    @Field
    int getHeight();

    @Field
    void setHeight(int value);

    @Field
    int getId();

    @Field
    void setId(int value);

    @Field
    int getOrientation();

    @Field
    void setOrientation(int value);

    @Field
    int getPlane();

    @Field
    void setPlane(int value);

    @Field
    int getX();

    @Field
    void setX(int value);

    @Field
    int getY();

    @Field
    void setY(int value);
}
