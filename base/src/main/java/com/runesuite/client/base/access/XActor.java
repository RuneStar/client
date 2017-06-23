package com.runesuite.client.base.access;

import com.jakewharton.rxrelay2.PublishRelay;
import com.runesuite.client.base.Accessor;
import com.runesuite.client.base.MethodEvent;
import java.lang.String;
import org.jetbrains.annotations.NotNull;

public interface XActor extends Accessor, XEntity {
    @Field
    int getAnimation();

    @Field
    void setAnimation(int value);

    @Field
    int getAnimationDelay();

    @Field
    void setAnimationDelay(int value);

    @Field
    XNodeDeque2 getHealthBars();

    @Field
    void setHealthBars(XNodeDeque2 value);

    @Field
    String getOverheadMessage();

    @Field
    void setOverheadMessage(String value);

    @Field
    int getPathLength();

    @Field
    void setPathLength(int value);

    @Field
    byte[] getPathTraversed();

    @Field
    void setPathTraversed(byte[] value);

    @Field
    int[] getPathX();

    @Field
    void setPathX(int[] value);

    @Field
    int[] getPathY();

    @Field
    void setPathY(int[] value);

    @Field
    int getRunAnimation();

    @Field
    void setRunAnimation(int value);

    @Field
    int getStandAnimation();

    @Field
    void setStandAnimation(int value);

    @Field
    int getTargetIndex();

    @Field
    void setTargetIndex(int value);

    @Field
    int getX();

    @Field
    void setX(int value);

    @Field
    int getY();

    @Field
    void setY(int value);

    @Method
    boolean isVisible();

    final class isVisible {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private isVisible() {
        }
    }
}
