package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Runnable;
import java.net.Socket;

public interface XNetSocket extends Accessor, Runnable {
    @Field
    InputStream getInputStream();

    @Field
    void setInputStream(InputStream value);

    @Field
    OutputStream getOutputStream();

    @Field
    void setOutputStream(OutputStream value);

    @Field
    Socket getSocket();

    @Field
    void setSocket(Socket value);
}
