package com.runesuite.client.base.access;

import com.jakewharton.rxrelay2.PublishRelay;
import com.runesuite.client.base.Accessor;
import com.runesuite.client.base.MethodEvent;
import java.awt.Component;
import java.awt.Graphics;
import org.jetbrains.annotations.NotNull;

/**
 * public final class
 */
public interface XCanvas extends Accessor {
    /**
     *  field
     */
    Component getComponent();

    /**
     *  field
     */
    void setComponent(Component value);

    /**
     * public final method
     */
    void paint(Graphics g);

    /**
     * public final method
     */
    void update(Graphics g);

    final class paint {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private paint() {
        }
    }

    final class update {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private update() {
        }
    }
}
