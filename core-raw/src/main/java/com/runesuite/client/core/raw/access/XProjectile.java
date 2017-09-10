package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;
import org.jetbrains.annotations.NotNull;

/**
 * public final class
 */
public interface XProjectile extends Accessor, XEntity {
    @NotNull
    MethodExecution getModel = new MethodExecution();

    /**
     *  field
     */
    double getAccelerationZ();

    /**
     *  field
     */
    void setAccelerationZ(double value);

    /**
     *  field
     */
    int getId();

    /**
     *  field
     */
    void setId(int value);

    /**
     *  field
     */
    int getInt1();

    /**
     *  field
     */
    void setInt1(int value);

    /**
     *  field
     */
    int getInt2();

    /**
     *  field
     */
    void setInt2(int value);

    /**
     *  field
     */
    int getInt3();

    /**
     *  field
     */
    void setInt3(int value);

    /**
     *  field
     */
    int getInt4();

    /**
     *  field
     */
    void setInt4(int value);

    /**
     *  field
     */
    int getInt5();

    /**
     *  field
     */
    void setInt5(int value);

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
    int getInt7();

    /**
     *  field
     */
    void setInt7(int value);

    /**
     *  field
     */
    boolean getIsMoving();

    /**
     *  field
     */
    void setIsMoving(boolean value);

    /**
     *  field
     */
    int getPitch();

    /**
     *  field
     */
    void setPitch(int value);

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
    XSequenceDefinition getSequenceDefinition();

    /**
     *  field
     */
    void setSequenceDefinition(XSequenceDefinition value);

    /**
     *  field
     */
    int getSourceX();

    /**
     *  field
     */
    void setSourceX(int value);

    /**
     *  field
     */
    int getSourceY();

    /**
     *  field
     */
    void setSourceY(int value);

    /**
     *  field
     */
    int getSourceZ();

    /**
     *  field
     */
    void setSourceZ(int value);

    /**
     *  field
     */
    double getSpeed();

    /**
     *  field
     */
    void setSpeed(double value);

    /**
     *  field
     */
    double getSpeedX();

    /**
     *  field
     */
    void setSpeedX(double value);

    /**
     *  field
     */
    double getSpeedY();

    /**
     *  field
     */
    void setSpeedY(double value);

    /**
     *  field
     */
    double getSpeedZ();

    /**
     *  field
     */
    void setSpeedZ(double value);

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
    double getX();

    /**
     *  field
     */
    void setX(double value);

    /**
     *  field
     */
    double getY();

    /**
     *  field
     */
    void setY(double value);

    /**
     *  field
     */
    int getYaw();

    /**
     *  field
     */
    void setYaw(int value);

    /**
     *  field
     */
    double getZ();

    /**
     *  field
     */
    void setZ(double value);

    /**
     * protected final method
     */
    XModel getModel();
}
