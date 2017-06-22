package com.runesuite.client.base.access;

import com.jakewharton.rxrelay2.PublishRelay;
import com.runesuite.client.base.Accessor;
import com.runesuite.client.base.MethodEvent;
import org.jetbrains.annotations.NotNull;

public interface XProjectile extends Accessor, XEntity {
    @Field
    double getAccelerationZ();

    @Field
    void setAccelerationZ(double value);

    @Field
    int getId();

    @Field
    void setId(int value);

    @Field
    int getInt1();

    @Field
    void setInt1(int value);

    @Field
    int getInt2();

    @Field
    void setInt2(int value);

    @Field
    int getInt3();

    @Field
    void setInt3(int value);

    @Field
    int getInt4();

    @Field
    void setInt4(int value);

    @Field
    int getInt5();

    @Field
    void setInt5(int value);

    @Field
    int getInt6();

    @Field
    void setInt6(int value);

    @Field
    int getInt7();

    @Field
    void setInt7(int value);

    @Field
    boolean getIsMoving();

    @Field
    void setIsMoving(boolean value);

    @Field
    int getPitch();

    @Field
    void setPitch(int value);

    @Field
    int getPlane();

    @Field
    void setPlane(int value);

    @Field
    XSequenceDefinition getSequenceDefinition();

    @Field
    void setSequenceDefinition(XSequenceDefinition value);

    @Field
    int getSourceX();

    @Field
    void setSourceX(int value);

    @Field
    int getSourceY();

    @Field
    void setSourceY(int value);

    @Field
    int getSourceZ();

    @Field
    void setSourceZ(int value);

    @Field
    double getSpeed();

    @Field
    void setSpeed(double value);

    @Field
    double getSpeedX();

    @Field
    void setSpeedX(double value);

    @Field
    double getSpeedY();

    @Field
    void setSpeedY(double value);

    @Field
    double getSpeedZ();

    @Field
    void setSpeedZ(double value);

    @Field
    int getTargetIndex();

    @Field
    void setTargetIndex(int value);

    @Field
    double getX();

    @Field
    void setX(double value);

    @Field
    double getY();

    @Field
    void setY(double value);

    @Field
    int getYaw();

    @Field
    void setYaw(int value);

    @Field
    double getZ();

    @Field
    void setZ(double value);

    @Method
    XModel getModel();

    final class getModel {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private getModel() {
        }
    }
}
