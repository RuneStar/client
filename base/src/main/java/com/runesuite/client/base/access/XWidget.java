package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import java.lang.String;

public interface XWidget extends Accessor, XNode {
    @Field
    int getChildIndex();

    @Field
    void setChildIndex(int value);

    @Field
    XWidget[] getChildren();

    @Field
    void setChildren(XWidget[] value);

    @Field
    int getCycle();

    @Field
    void setCycle(int value);

    @Field
    int getHeight();

    @Field
    void setHeight(int value);

    @Field
    int getId();

    @Field
    void setId(int value);

    @Field
    int getIndex();

    @Field
    void setIndex(int value);

    @Field
    boolean getIsHidden();

    @Field
    void setIsHidden(boolean value);

    @Field
    int getItemId();

    @Field
    void setItemId(int value);

    @Field
    int getItemQuantity();

    @Field
    void setItemQuantity(int value);

    @Field
    int getPaddingX();

    @Field
    void setPaddingX(int value);

    @Field
    int getPaddingY();

    @Field
    void setPaddingY(int value);

    @Field
    XWidget getParent();

    @Field
    void setParent(XWidget value);

    @Field
    int getParentId();

    @Field
    void setParentId(int value);

    @Field
    int getScrollMax();

    @Field
    void setScrollMax(int value);

    @Field
    int getScrollX();

    @Field
    void setScrollX(int value);

    @Field
    int getScrollY();

    @Field
    void setScrollY(int value);

    @Field
    String getSpell();

    @Field
    void setSpell(String value);

    @Field
    String getText();

    @Field
    void setText(String value);

    @Field
    int getTextColor();

    @Field
    void setTextColor(int value);

    @Field
    int getTextureId();

    @Field
    void setTextureId(int value);

    @Field
    String getTooltip();

    @Field
    void setTooltip(String value);

    @Field
    int getWidth();

    @Field
    void setWidth(int value);

    @Field
    int getX();

    @Field
    void setX(int value);

    @Field
    int getY();

    @Field
    void setY(int value);
}
