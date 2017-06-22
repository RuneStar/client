package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

public interface XGroundItemPile extends Accessor {
    @Field
    XEntity getBottom();

    @Field
    void setBottom(XEntity value);

    @Field
    XEntity getMiddle();

    @Field
    void setMiddle(XEntity value);

    @Field
    XEntity getTop();

    @Field
    void setTop(XEntity value);
}
