package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import java.util.Iterator;

public interface XNodeHashTable2Iterator extends Accessor, Iterator {
    @Field
    XNodeHashTable2 getHashTable();

    @Field
    void setHashTable(XNodeHashTable2 value);
}
