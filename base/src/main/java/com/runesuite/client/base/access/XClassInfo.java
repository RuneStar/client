package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * public class
 */
public interface XClassInfo extends Accessor, XNode {
    /**
     *  field
     */
    byte[][][] getBytes();

    /**
     *  field
     */
    void setBytes(byte[][][] value);

    /**
     *  field
     */
    Field[] getFields();

    /**
     *  field
     */
    void setFields(Field[] value);

    /**
     *  field
     */
    Method[] getMethods();

    /**
     *  field
     */
    void setMethods(Method[] value);
}
