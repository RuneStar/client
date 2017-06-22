package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

public interface XPlayerAppearance extends Accessor {
    @Field
    int[] getBodyColors();

    @Field
    void setBodyColors(int[] value);

    @Field
    int[] getEquipment();

    @Field
    void setEquipment(int[] value);

    @Field
    int getId();

    @Field
    void setId(int value);

    @Field
    boolean getIsFemale();

    @Field
    void setIsFemale(boolean value);
}
