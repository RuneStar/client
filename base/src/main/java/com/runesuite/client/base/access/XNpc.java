package com.runesuite.client.base.access;

import com.jakewharton.rxrelay2.PublishRelay;
import com.runesuite.client.base.Accessor;
import com.runesuite.client.base.MethodEvent;
import org.jetbrains.annotations.NotNull;

/**
 * public final class
 */
public interface XNpc extends Accessor, XActor {
    /**
     *  field
     */
    XNpcDefinition getDefinition();

    /**
     *  field
     */
    void setDefinition(XNpcDefinition value);

    /**
     * protected final method
     */
    XModel getModel();

    /**
     * final method
     */
    boolean isVisible();

    final class getModel {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private getModel() {
        }
    }

    final class isVisible {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private isVisible() {
        }
    }
}
