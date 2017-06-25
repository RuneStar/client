package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

/**
 * public class
 */
public interface XChatBox extends Accessor {
    /**
     *  field
     */
    XMessage[] getMessages();

    /**
     *  field
     */
    void setMessages(XMessage[] value);
}
