package com.runesuite.client.base.access;

import com.jakewharton.rxrelay2.PublishRelay;
import com.runesuite.client.base.Accessor;
import com.runesuite.client.base.MethodEvent;
import java.util.LinkedHashMap;
import org.jetbrains.annotations.NotNull;

/**
 * public class
 */
public interface XClientPreferences extends Accessor {
    /**
     *  field
     */
    LinkedHashMap getParameters();

    /**
     *  field
     */
    void setParameters(LinkedHashMap value);

    /**
     *  field
     */
    boolean getRoofsHidden();

    /**
     *  field
     */
    void setRoofsHidden(boolean value);

    /**
     *  field
     */
    boolean getTitleMusicDisabled();

    /**
     *  field
     */
    void setTitleMusicDisabled(boolean value);

    /**
     *  field
     */
    int getWindowMode();

    /**
     *  field
     */
    void setWindowMode(int value);

    /**
     *  method
     */
    XByteBuffer toBuffer();

    final class toBuffer {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private toBuffer() {
        }
    }
}
