package com.runesuite.client.base.access;

import com.jakewharton.rxrelay2.PublishRelay;
import com.runesuite.client.base.Accessor;
import com.runesuite.client.base.MethodEvent;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import org.jetbrains.annotations.NotNull;

public interface XGraphicsProvider extends Accessor, XAbstractGraphicsProvider {
    @Field
    Component getComponent();

    @Field
    void setComponent(Component value);

    @Field
    Image getImage();

    @Field
    void setImage(Image value);

    @Method
    void draw(int x, int y, int width, int height);

    @Method
    void draw0(Graphics graphics, int x, int y, int width, int height);

    @Method
    void drawFull(int x, int y);

    @Method
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
