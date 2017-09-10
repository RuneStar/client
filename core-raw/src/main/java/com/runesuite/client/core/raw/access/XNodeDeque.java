package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;
import org.jetbrains.annotations.NotNull;

/**
 * public class
 */
public interface XNodeDeque extends Accessor {
    @NotNull
    MethodExecution addFirst = new MethodExecution();

    @NotNull
    MethodExecution addLast = new MethodExecution();

    @NotNull
    MethodExecution clear = new MethodExecution();

    @NotNull
    MethodExecution first = new MethodExecution();

    @NotNull
    MethodExecution last = new MethodExecution();

    @NotNull
    MethodExecution next = new MethodExecution();

    @NotNull
    MethodExecution previous = new MethodExecution();

    @NotNull
    MethodExecution removeFirst = new MethodExecution();

    @NotNull
    MethodExecution removeLast = new MethodExecution();

    /**
     *  field
     */
    XNode getCurrent();

    /**
     *  field
     */
    void setCurrent(XNode value);

    /**
     * public field
     */
    XNode getSentinel();

    /**
     * public field
     */
    void setSentinel(XNode value);

    /**
     * public method
     */
    void addFirst(XNode node);

    /**
     * public method
     */
    void addLast(XNode node);

    /**
     * public method
     */
    void clear();

    /**
     * public method
     */
    XNode first();

    /**
     * public method
     */
    XNode last();

    /**
     * public method
     */
    XNode next();

    /**
     * public method
     */
    XNode previous();

    /**
     * public method
     */
    XNode removeFirst();

    /**
     * public method
     */
    XNode removeLast();
}
