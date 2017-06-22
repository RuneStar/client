package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import java.util.Iterator;

public interface XNodeDeque2DescendingIterator extends Accessor, Iterator {
    @Field
    XNodeDeque2 getDeque();

    @Field
    void setDeque(XNodeDeque2 value);
}
