package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;
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
