package com.runesuite.client.base.access;

import com.jakewharton.rxrelay2.PublishRelay;
import com.runesuite.client.base.Accessor;
import com.runesuite.client.base.MethodEvent;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import org.jetbrains.annotations.NotNull;

/**
 * public final class
 */
public interface XGraphicsProvider extends Accessor, XAbstractGraphicsProvider {
    /**
     *  field
     */
    Component getComponent();

    /**
     *  field
     */
    void setComponent(Component value);

    /**
     *  field
     */
    Image getImage();

    /**
     *  field
     */
    void setImage(Image value);

    /**
     * public final method
     */
    void draw(int x, int y, int width, int height);

    /**
     * final method
     */
    void draw0(Graphics graphics, int x, int y, int width, int height);

    /**
     * public final method
     */
    void drawFull(int x, int y);

    /**
     * final method
     */
    void drawFull0(Graphics graphics, int x, int y);

    final class draw {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private draw() {
        }
    }

    final class draw0 {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private draw0() {
        }
    }

    final class drawFull {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private drawFull() {
        }
    }

    final class drawFull0 {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private drawFull0() {
        }
    }
}
