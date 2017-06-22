package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

public interface XGameObject extends Accessor {
    @Field
    XEntity getEntity();

    @Field
    void setEntity(XEntity value);

    @Field
    int getId();

    @Field
    void setId(int value);
}
