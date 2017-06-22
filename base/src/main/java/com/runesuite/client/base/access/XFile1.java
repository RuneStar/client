package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

public interface XFile1 extends Accessor {
    @Field
    XAccessFile getFile();

    @Field
    void setFile(XAccessFile value);
}
