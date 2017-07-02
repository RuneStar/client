package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import org.jetbrains.annotations.NotNull;

/**
 * public class
 */
public interface XChatBox extends Accessor {
    @NotNull
    MethodExecution addMessage = new MethodExecution();

    /**
     *  field
     */
    XMessage[] getMessages();

    /**
     *  field
     */
    void setMessages(XMessage[] value);

    /**
     *  method
     */
    XMessage addMessage(int type, String sender, String text, String prefix);
}
