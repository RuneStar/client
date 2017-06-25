package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import java.lang.String;

/**
 * public class
 */
public interface XMessage extends Accessor, XCacheNode {
    /**
     *  field
     */
    String getPrefix();

    /**
     *  field
     */
    void setPrefix(String value);

    /**
     *  field
     */
    String getSender();

    /**
     *  field
     */
    void setSender(String value);

    /**
     *  field
     */
    String getText();

    /**
     *  field
     */
    void setText(String value);

    /**
     *  field
     */
    int getType();

    /**
     *  field
     */
    void setType(int value);
}
