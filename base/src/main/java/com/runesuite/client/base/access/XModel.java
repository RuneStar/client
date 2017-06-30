package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

/**
 * public class
 */
public interface XModel extends Accessor, XEntity {
    /**
     *  field
     */
    int[] getIndicesX();

    /**
     *  field
     */
    void setIndicesX(int[] value);

    /**
     *  field
     */
    int[] getIndicesY();

    /**
     *  field
     */
    void setIndicesY(int[] value);

    /**
     *  field
     */
    int[] getIndicesZ();

    /**
     *  field
     */
    void setIndicesZ(int[] value);

    /**
     *  field
     */
    int[] getVerticesX();

    /**
     *  field
     */
    void setVerticesX(int[] value);

    /**
     *  field
     */
    int[] getVerticesY();

    /**
     *  field
     */
    void setVerticesY(int[] value);

    /**
     *  field
     */
    int[] getVerticesZ();

    /**
     *  field
     */
    void setVerticesZ(int[] value);
}
