package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;

/**
 * public class
 */
public interface XHealthBar extends Accessor, XNode {
    /**
     *  field
     */
    XHealthBarDefinition getDefinition();

    /**
     *  field
     */
    void setDefinition(XHealthBarDefinition value);

    /**
     *  field
     */
    XNodeDeque2 getHitSplats();

    /**
     *  field
     */
    void setHitSplats(XNodeDeque2 value);
}
