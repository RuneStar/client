package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import java.util.Iterator;

/**
 * public class
 */
public interface XNodeHashTable2Iterator extends Accessor, Iterator {
    /**
     *  field
     */
    XNodeHashTable2 getHashTable();

    /**
     *  field
     */
    void setHashTable(XNodeHashTable2 value);
}
