package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * public class
 */
public interface XClassInfo extends Accessor, XNode {
    /**
     * public field
     */
    byte[][][] getBytes();

    /**
     * public field
     */
    void setBytes(byte[][][] value);

    /**
     * public field
     */
    Field[] getFields();

    /**
     * public field
     */
    void setFields(Field[] value);

    /**
     * public field
     */
    Method[] getMethods();

    /**
     * public field
     */
    void setMethods(Method[] value);
}
