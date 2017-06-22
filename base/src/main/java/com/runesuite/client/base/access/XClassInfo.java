package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface XClassInfo extends Accessor, XNode {
    @Field
    byte[][][] getBytes();

    @Field
    void setBytes(byte[][][] value);

    @Field
    Field[] getFields();

    @Field
    void setFields(Field[] value);

    @Field
    Method[] getMethods();

    @Field
    void setMethods(Method[] value);
}
