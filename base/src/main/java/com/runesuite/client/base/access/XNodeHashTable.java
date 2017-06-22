package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

public interface XNodeHashTable extends Accessor {
    @Field
    XNode[] getBuckets();

    @Field
    void setBuckets(XNode[] value);

    @Field
    XNode getCurrent();

    @Field
    void setCurrent(XNode value);

    @Field
    XNode getCurrentGet();

    @Field
    void setCurrentGet(XNode value);

    @Field
    int getIndex();

    @Field
    void setIndex(int value);

    @Field
    int getSize();

    @Field
    void setSize(int value);
}
