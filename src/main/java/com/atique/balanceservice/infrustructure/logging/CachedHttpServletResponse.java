package com.atique.balanceservice.infrustructure.logging;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.springframework.util.StreamUtils;

import java.io.*;

/**
 * @author atiQue
 * @since 17'Jun 2023 at 10:34 PM
 */

public class CachedHttpServletResponse extends HttpServletResponseWrapper {

    private byte[] cachedPayload;

    public CachedHttpServletResponse(HttpServletResponse response) throws IOException {
        super(response);
        OutputStream resOutputStream = response.getOutputStream();
//        this.cachedPayload = StreamUtils.copyToByteArray(resOutputStream);
    }
//
//    @Override
//    public ServletOutputStream getOutPutStream() {
//        return new CachedServletInputStream(this.cachedPayload);
//    }
//
//    @Override
//    public BufferedReader getReader() {
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedPayload);
//        return new BufferedReader(new InputStreamReader(byteArrayInputStream));
//    }
}