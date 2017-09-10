package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;
import java.util.LinkedHashMap;
import org.jetbrains.annotations.NotNull;

/**
 * public class
 */
public interface XClientPreferences extends Accessor {
    @NotNull
    MethodExecution toBuffer = new MethodExecution();

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
}
