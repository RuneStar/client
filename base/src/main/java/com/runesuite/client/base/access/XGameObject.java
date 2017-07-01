package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;

/**
 * public final class
 */
public interface XGameObject extends Accessor {
    /**
     *  field
     */
    int getCenterX();

    /**
     *  field
     */
    void setCenterX(int value);

    /**
     *  field
     */
    int getCenterY();

    /**
     *  field
     */
    void setCenterY(int value);

    /**
     *  field
     */
    int getEndX();

    /**
     *  field
     */
    void setEndX(int value);

    /**
     *  field
     */
    int getEndY();

    /**
     *  field
     */
    void setEndY(int value);

    /**
     * public field
     */
    XEntity getEntity();

    /**
     * public field
     */
    void setEntity(XEntity value);

    /**
     *  field
     */
    int getFlags();

    /**
     *  field
     */
    void setFlags(int value);

    /**
     *  field
     */
    int getHeight();

    /**
     *  field
     */
    void setHeight(int value);

    /**
     * public field
     */
    int getId();

    /**
     * public field
     */
    void setId(int value);

    /**
     *  field
     */
    int getInt6();

    /**
     *  field
     */
    void setInt6(int value);

    /**
     *  field
     */
    int getPlane();

    /**
     *  field
     */
    void setPlane(int value);

    /**
     *  field
     */
    int getStartX();

    /**
     *  field
     */
    void setStartX(int value);

    /**
     *  field
     */
    int getStartY();

    /**
     *  field
     */
    void setStartY(int value);
}
