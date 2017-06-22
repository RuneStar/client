package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import java.lang.String;

public interface XItemDefinition extends Accessor, XCacheNode {
    @Field
    String[] getGroundActions();

    @Field
    void setGroundActions(String[] value);

    @Field
    String[] getInventoryActions();

    @Field
    void setInventoryActions(String[] value);

    @Field
    String getName();

    @Field
    void setName(String value);
}
