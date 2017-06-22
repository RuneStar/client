package com.runesuite.client.base.access;

import com.jakewharton.rxrelay2.PublishRelay;
import com.runesuite.client.base.Accessor;
import com.runesuite.client.base.MethodEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public interface XCanvas extends Accessor {
    @Field
    Component getComponent();

    @Field
    void setComponent(Component value);

    @Method
    void paint(Graphics g);

    @Method
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
