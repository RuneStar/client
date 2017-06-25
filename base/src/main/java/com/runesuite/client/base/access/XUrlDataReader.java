package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import java.io.DataInputStream;

/**
 * public class
 */
public interface XUrlDataReader extends Accessor {
    /**
     *  field
     */
    DataInputStream getDataInputStream();

    /**
     *  field
     */
    void setDataInputStream(DataInputStream value);
}
