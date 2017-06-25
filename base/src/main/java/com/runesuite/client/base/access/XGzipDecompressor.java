package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import java.util.zip.Inflater;

/**
 * public class
 */
public interface XGzipDecompressor extends Accessor {
    /**
     *  field
     */
    Inflater getInflater();

    /**
     *  field
     */
    void setInflater(Inflater value);
}
