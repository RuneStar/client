package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import java.lang.Object;
import java.lang.Runnable;

/**
 * public class
 */
public interface XMouseTracker extends Accessor, Runnable {
    /**
     *  field
     */
    Object getLock();

    /**
     *  field
     */
    void setLock(Object value);
}
