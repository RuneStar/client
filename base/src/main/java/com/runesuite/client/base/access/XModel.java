package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

public interface XModel extends Accessor, XEntity {
    @Field
    int[] getIndicesX();

    @Field
    void setIndicesX(int[] value);

    @Field
    int[] getIndicesY();

    @Field
    void setIndicesY(int[] value);

    @Field
    int[] getIndicesZ();

    @Field
    void setIndicesZ(int[] value);

    @Field
    int[] getVerticesX();

    @Field
    void setVerticesX(int[] value);

    @Field
    int[] getVerticesY();

    @Field
    void setVerticesY(int[] value);

    @Field
    int[] getVerticesZ();

    @Field
    void setVerticesZ(int[] value);
}
