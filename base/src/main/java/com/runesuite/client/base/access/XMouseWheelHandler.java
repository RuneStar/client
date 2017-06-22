package com.runesuite.client.base.access;

import com.jakewharton.rxrelay2.PublishRelay;
import com.runesuite.client.base.Accessor;
import com.runesuite.client.base.MethodEvent;
import java.awt.Component;
import java.awt.event.MouseWheelListener;
import org.jetbrains.annotations.NotNull;

public interface XMouseWheelHandler extends Accessor, XMouseWheel, MouseWheelListener {
    @Field
    int getRotation();

    @Field
    void setRotation(int value);

    @Method
    void addTo(Component component);

    @Method
    void removeFrom(Component component);

    @Method
    int useRotation();

    final class addTo {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private addTo() {
        }
    }

    final class removeFrom {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private removeFrom() {
        }
    }

    final class useRotation {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private useRotation() {
        }
    }
}
