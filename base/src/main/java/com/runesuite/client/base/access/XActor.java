package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import java.lang.String;
import org.jetbrains.annotations.NotNull;

/**
 * public abstract class
 */
public interface XActor extends Accessor, XEntity {
    @NotNull
    MethodExecution isVisible = new MethodExecution();

    /**
     *  field
     */
    int getAnimation();

    /**
     *  field
     */
    void setAnimation(int value);

    /**
     *  field
     */
    int getAnimationDelay();

    /**
     *  field
     */
    void setAnimationDelay(int value);

    /**
     *  field
     */
    XNodeDeque2 getHealthBars();

    /**
     *  field
     */
    void setHealthBars(XNodeDeque2 value);

    /**
     *  field
     */
    int getOrientation();

    /**
     *  field
     */
    void setOrientation(int value);

    /**
     *  field
     */
    String getOverheadMessage();

    /**
     *  field
     */
    void setOverheadMessage(String value);

    /**
     *  field
     */
    int getPathLength();

    /**
     *  field
     */
    void setPathLength(int value);

    /**
     *  field
     */
    byte[] getPathTraversed();

    /**
     *  field
     */
    void setPathTraversed(byte[] value);

    /**
     *  field
     */
    int[] getPathX();

    /**
     *  field
     */
    void setPathX(int[] value);

    /**
     *  field
     */
    int[] getPathY();

    /**
     *  field
     */
    void setPathY(int[] value);

    /**
     *  field
     */
    int getRunAnimation();

    /**
     *  field
     */
    void setRunAnimation(int value);

    /**
     *  field
     */
    int getStandAnimation();

    /**
     *  field
     */
    void setStandAnimation(int value);

    /**
     *  field
     */
    int getTargetIndex();

    /**
     *  field
     */
    void setTargetIndex(int value);

    /**
     *  field
     */
    int getX();

    /**
     *  field
     */
    void setX(int value);

    /**
     *  field
     */
    int getY();

    /**
     *  field
     */
    void setY(int value);

    /**
     *  method
     */
    boolean isVisible();
}
