package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import java.lang.Iterable;
import org.jetbrains.annotations.NotNull;

/**
 * public class
 */
public interface XNodeDeque2 extends Accessor, Iterable {
    @NotNull
    MethodExecution addFirst = new MethodExecution();

    @NotNull
    MethodExecution addLast = new MethodExecution();

    @NotNull
    MethodExecution last = new MethodExecution();

    @NotNull
    MethodExecution previous = new MethodExecution();

    @NotNull
    MethodExecution previousOrLast = new MethodExecution();

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
     *  field
     */
    XNode getSentinel();

    /**
     *  field
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
    XNode last();

    /**
     * public method
     */
    XNode previous();

    /**
     *  method
     */
    XNode previousOrLast(XNode node);

    /**
     * public method
     */
    XNode removeLast();
}
