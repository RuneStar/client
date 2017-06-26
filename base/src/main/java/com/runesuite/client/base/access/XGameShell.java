package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
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
    @NotNull
    MethodExecution paint = new MethodExecution();

    @NotNull
    MethodExecution replaceCanvas = new MethodExecution();

    @NotNull
    MethodExecution update = new MethodExecution();

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
     * final synchronized method
     */
    void replaceCanvas();

    /**
     * public final method
     */
    void update(Graphics g);
}
