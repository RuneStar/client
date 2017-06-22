package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

public interface XItemContainer extends Accessor, XNode {
    @Field
    int[] getIds();

    @Field
    void setIds(int[] value);

    @Field
    int[] getQuantities();

    @Field
    void setQuantities(int[] value);
}
