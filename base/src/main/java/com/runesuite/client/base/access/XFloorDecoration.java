package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

public interface XFloorDecoration extends Accessor {
    @Field
    XEntity getEntity();

    @Field
    void setEntity(XEntity value);
}
