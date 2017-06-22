package com.runesuite.client.base.access;

import com.jakewharton.rxrelay2.PublishRelay;
import com.runesuite.client.base.Accessor;
import com.runesuite.client.base.MethodEvent;
import java.io.RandomAccessFile;
import org.jetbrains.annotations.NotNull;

public interface XAccessFile extends Accessor {
    @Field
    long getCapacity();

    @Field
    void setCapacity(long value);

    @Field
    RandomAccessFile getFile();

    @Field
    void setFile(RandomAccessFile value);

    @Field
    long getIndex();

    @Field
    void setIndex(long value);

    @Method
    void close();

    @Method
    long length();

    @Method
    int read(byte[] bytes, int offset, int length);

    @Method
    void seek(long index);

    @Method
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
