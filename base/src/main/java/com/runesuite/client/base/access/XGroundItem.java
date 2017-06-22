package com.runesuite.client.base.access;

import com.jakewharton.rxrelay2.PublishRelay;
import com.runesuite.client.base.Accessor;
import com.runesuite.client.base.MethodEvent;
import org.jetbrains.annotations.NotNull;

public interface XGroundItem extends Accessor, XEntity {
    @Field
    int getId();

    @Field
    void setId(int value);

    @Field
    int getQuantity();

    @Field
    void setQuantity(int value);

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
