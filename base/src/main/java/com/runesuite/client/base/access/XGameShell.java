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

/**
 * public abstract class
 */
public interface XGameShell extends Accessor, Runnable, FocusListener, WindowListener {
    /**
     *  field
     */
    Canvas getCanvas();

    /**
     *  field
     */
    void setCanvas(Canvas value);

    /**
     *  field
     */
    Clipboard getClipboard();

    /**
     *  field
     */
    void setClipboard(Clipboard value);

    /**
     * final field
     */
    EventQueue getEventQueue();

    /**
     *  field
     */
    Frame getFrame();

    /**
     *  field
     */
    void setFrame(Frame value);

    /**
     *  field
     */
    XMouseWheelHandler getMouseWheelHandler();

    /**
     *  field
     */
    void setMouseWheelHandler(XMouseWheelHandler value);

    /**
     * public final synchronized method
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
