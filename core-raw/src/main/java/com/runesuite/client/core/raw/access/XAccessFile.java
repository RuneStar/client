package com.runesuite.client.core.raw.access;

import com.runesuite.client.core.raw.Accessor;
import java.io.RandomAccessFile;
import org.jetbrains.annotations.NotNull;

/**
 * public final class
 */
public interface XAccessFile extends Accessor {
    @NotNull
    MethodExecution length = new MethodExecution();

    @NotNull
    MethodExecution read = new MethodExecution();

    @NotNull
    MethodExecution seek = new MethodExecution();

    @NotNull
    MethodExecution write = new MethodExecution();

    /**
     *  field
     */
    long getCapacity();

    /**
     *  field
     */
    void setCapacity(long value);

    /**
     *  field
     */
    RandomAccessFile getFile();

    /**
     *  field
     */
    void setFile(RandomAccessFile value);

    /**
     *  field
     */
    long getIndex();

    /**
     *  field
     */
    void setIndex(long value);

    /**
     * public final method
     */
    long length();

    /**
     * public final method
     */
    int read(byte[] bytes, int offset, int length);

    /**
     * final method
     */
    void seek(long index);

    /**
     * public final method
     */
    void write(byte[] bytes, int offset, int length);
}
