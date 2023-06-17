package com.atique.balanceservice.infrustructure.logging;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author atiQue
 * @since 17'Jun 2023 at 10:33 PM
 */

public class CachedServletOutputStream extends ServletOutputStream {

    private final static Logger LOGGER = LoggerFactory.getLogger(CachedServletOutputStream.class);
    private OutputStream cachedOutStream;


    public CachedServletOutputStream(byte[] cachedBody) {
//        this.cachedOutStream = new ByteArrayOutputStream(cachedBody);
    }

    @Override
    public void write(int b) throws IOException {
        cachedOutStream.write(b);
    }


    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener listener) {
        throw new UnsupportedOperationException();
    }
}