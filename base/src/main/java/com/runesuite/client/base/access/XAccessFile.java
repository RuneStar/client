package com.runesuite.client.base.access;

import com.jakewharton.rxrelay2.PublishRelay;
import com.runesuite.client.base.Accessor;
import com.runesuite.client.base.MethodEvent;
import org.jetbrains.annotations.NotNull;

import java.io.RandomAccessFile;

/**
 * public final class
 */
public interface XAccessFile extends Accessor {
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
    void close();

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

    final class close {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private close() {
        }
    }

    final class length {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private length() {
        }
    }

    final class read {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private read() {
        }
    }

    final class seek {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private seek() {
        }
    }

    final class write {
        @NotNull
        public static final PublishRelay<MethodEvent.Enter> ENTER = PublishRelay.create();

        @NotNull
        public static final PublishRelay<MethodEvent.Exit> EXIT = PublishRelay.create();

        private write() {
        }
    }
}
