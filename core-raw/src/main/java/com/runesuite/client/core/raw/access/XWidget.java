package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;
import java.lang.String;

/**
 * public class
 */
public interface XWidget extends Accessor, XNode {
    /**
     * public field
     */
    int getChildIndex();

    /**
     * public field
     */
    void setChildIndex(int value);

    /**
     * public field
     */
    XWidget[] getChildren();

    /**
     * public field
     */
    void setChildren(XWidget[] value);

    /**
     * public field
     */
    int getCycle();

    /**
     * public field
     */
    void setCycle(int value);

    /**
     * public field
     */
    int getHeight();

    /**
     * public field
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
     * public field
     */
    int getIndex();

    /**
     * public field
     */
    void setIndex(int value);

    /**
     * public field
     */
    boolean getIsHidden();

    /**
     * public field
     */
    void setIsHidden(boolean value);

    /**
     * public field
     */
    int getItemId();

    /**
     * public field
     */
    void setItemId(int value);

    /**
     * public field
     */
    int getItemQuantity();

    /**
     * public field
     */
    void setItemQuantity(int value);

    /**
     * public field
     */
    int getPaddingX();

    /**
     * public field
     */
    void setPaddingX(int value);

    /**
     * public field
     */
    int getPaddingY();

    /**
     * public field
     */
    void setPaddingY(int value);

    /**
     * public field
     */
    XWidget getParent();

    /**
     * public field
     */
    void setParent(XWidget value);

    /**
     * public field
     */
    int getParentId();

    /**
     * public field
     */
    void setParentId(int value);

    /**
     * public field
     */
    int getScrollMax();

    /**
     * public field
     */
    void setScrollMax(int value);

    /**
     * public field
     */
    int getScrollX();

    /**
     * public field
     */
    void setScrollX(int value);

    /**
     * public field
     */
    int getScrollY();

    /**
     * public field
     */
    void setScrollY(int value);

    /**
     * public field
     */
    String getSpell();

    /**
     * public field
     */
    void setSpell(String value);

    /**
     * public field
     */
    String getText();

    /**
     * public field
     */
    void setText(String value);

    /**
     * public field
     */
    int getTextColor();

    /**
     * public field
     */
    void setTextColor(int value);

    /**
     * public field
     */
    int getTextureId();

    /**
     * public field
     */
    void setTextureId(int value);

    /**
     * public field
     */
    String getTooltip();

    /**
     * public field
     */
    void setTooltip(String value);

    /**
     * public field
     */
    int getWidth();

    /**
     * public field
     */
    void setWidth(int value);

    /**
     * public field
     */
    int getX();

    /**
     * public field
     */
    void setX(int value);

    /**
     * public field
     */
    int getY();

    /**
     * public field
     */
    void setY(int value);
}
