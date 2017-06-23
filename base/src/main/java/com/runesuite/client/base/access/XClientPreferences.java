package com.runesuite.client.base.access;

import com.jakewharton.rxrelay2.PublishRelay;
import com.runesuite.client.base.Accessor;
import com.runesuite.client.base.MethodEvent;
import java.util.LinkedHashMap;
import org.jetbrains.annotations.NotNull;

public interface XClientPreferences extends Accessor {
    @Field
    LinkedHashMap getParameters();

    @Field
    void setParameters(LinkedHashMap value);

    @Field
    boolean getRoofsHidden();

    @Field
    void setRoofsHidden(boolean value);

    @Field
    boolean getTitleMusicDisabled();

    @Field
    void setTitleMusicDisabled(boolean value);

    @Field
    int getWindowMode();

    @Field
    void setWindowMode(int value);

    @Method
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
