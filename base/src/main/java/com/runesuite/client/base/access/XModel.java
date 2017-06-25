package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

/**
 * public class
 */
public interface XModel extends Accessor, XEntity {
    /**
     * public field
     */
    int[] getIndicesX();

    /**
     * public field
     */
    void setIndicesX(int[] value);

    /**
     * public field
     */
    int[] getIndicesY();

    /**
     * public field
     */
    void setIndicesY(int[] value);

    /**
     * public field
     */
    int[] getIndicesZ();

    /**
     * public field
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
