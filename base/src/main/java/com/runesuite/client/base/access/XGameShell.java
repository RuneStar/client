package com.runesuite.client.base.access;

import com.jakewharton.rxrelay2.PublishRelay;
import com.runesuite.client.base.Accessor;
import com.runesuite.client.base.MethodEvent;
import java.awt.Canvas;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.datatransfer.Clipboard;
import java.awt.event.FocusListener;
import java.awt.event.WindowListener;
import java.lang.Runnable;
import org.jetbrains.annotations.NotNull;

public interface XGameShell extends Accessor, Runnable, FocusListener, WindowListener {
    @Field
    Canvas getCanvas();

    @Field
    void setCanvas(Canvas value);

    @Field
    Clipboard getClipboard();

    @Field
    void setClipboard(Clipboard value);

    @Field
    EventQueue getEventQueue();

    @Field
    Frame getFrame();

    @Field
    void setFrame(Frame value);

    @Field
    XMouseWheelHandler getMouseWheelHandler();

    @Field
    void setMouseWheelHandler(XMouseWheelHandler value);

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
