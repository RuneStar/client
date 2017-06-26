package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import org.jetbrains.annotations.NotNull;

/**
 * public class
 */
public interface XNode extends Accessor {
    @NotNull
    MethodExecution hasNext = new MethodExecution();

    @NotNull
    MethodExecution remove = new MethodExecution();

    /**
     *  field
     */
    XNode getNext();

    /**
     *  field
     */
    void setNext(XNode value);

    /**
     * public field
     */
    XNode getPrevious();

    /**
     * public field
     */
    void setPrevious(XNode value);

    /**
     * public field
     */
    long getUid();

    /**
     * public field
     */
    void setUid(long value);

    /**
     * public method
     */
    boolean hasNext();

    /**
     * public method
     */
    void remove();
}
