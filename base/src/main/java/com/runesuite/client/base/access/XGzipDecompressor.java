package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import java.util.zip.Inflater;

public interface XGzipDecompressor extends Accessor {
    @Field
    Inflater getInflater();

    @Field
    void setInflater(Inflater value);
}
