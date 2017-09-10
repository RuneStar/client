package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;
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
