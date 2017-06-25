package com.runesuite.client.base.access;

import com.jakewharton.rxrelay2.PublishRelay;
import com.runesuite.client.base.Accessor;
import com.runesuite.client.base.MethodEvent;
import org.jetbrains.annotations.NotNull;

/**
 * public final class
 */
public interface XGroundItem extends Accessor, XEntity {
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
    int getQuantity();

    /**
     *  field
     */
    void setQuantity(int value);

    /**
     * protected final method
     */
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
