package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

public interface XHealthBar extends Accessor, XNode {
    @Field
    XHealthBarDefinition getDefinition();

    @Field
    void setDefinition(XHealthBarDefinition value);

    @Field
    XNodeDeque2 getHitSplats();

    @Field
    void setHitSplats(XNodeDeque2 value);
}
