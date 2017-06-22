package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import java.io.DataInputStream;

public interface XUrlDataReader extends Accessor {
    @Field
    DataInputStream getDataInputStream();

    @Field
    void setDataInputStream(DataInputStream value);
}
