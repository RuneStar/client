package com.runesuite.client.base.access;

import com.jakewharton.rxrelay2.PublishRelay;
import com.runesuite.client.base.Accessor;
import com.runesuite.client.base.MethodEvent;
import org.jetbrains.annotations.NotNull;

/**
 * public abstract class
 */
public interface XEntity extends Accessor, XCacheNode {
    /**
     * public field
     */
    int getHeight();

    /**
     * public field
     */
    void setHeight(int value);

    /**
     * protected method
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
