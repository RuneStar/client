package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;
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
