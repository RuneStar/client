package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;
import java.lang.String;
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
