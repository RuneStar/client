package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import java.lang.Object;
import java.lang.Runnable;

public interface XMouseTracker extends Accessor, Runnable {
    @Field
    Object getLock();

    @Field
    void setLock(Object value);
}
