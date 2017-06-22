package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import java.lang.String;

public interface XObjectDefinition extends Accessor, XCacheNode {
    @Field
    String[] getActions();

    @Field
    void setActions(String[] value);

    @Field
    String getName();

    @Field
    void setName(String value);
}
