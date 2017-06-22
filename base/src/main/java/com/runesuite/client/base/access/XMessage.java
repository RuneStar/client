package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import java.lang.String;

public interface XMessage extends Accessor, XCacheNode {
    @Field
    String getPrefix();

    @Field
    void setPrefix(String value);

    @Field
    String getSender();

    @Field
    void setSender(String value);

    @Field
    String getText();

    @Field
    void setText(String value);

    @Field
    int getType();

    @Field
    void setType(int value);
}
