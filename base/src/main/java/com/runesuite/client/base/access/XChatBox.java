package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

public interface XChatBox extends Accessor {
    @Field
    XMessage[] getMessages();

    @Field
    void setMessages(XMessage[] value);
}
