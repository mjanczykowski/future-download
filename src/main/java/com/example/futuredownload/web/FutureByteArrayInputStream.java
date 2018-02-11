package com.example.futuredownload.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

class FutureByteArrayInputStream extends InputStream {

    private final Future<byte[]> futureData;

    private final Object lock = new Object();

    private volatile ByteArrayInputStream byteArrayInputStream;

    FutureByteArrayInputStream(Future<byte[]> futureData) {
        this.futureData = futureData;
    }

    @Override
    public int read() throws IOException {
        awaitCompletion();
        return byteArrayInputStream.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        awaitCompletion();
        return byteArrayInputStream.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        awaitCompletion();
        return byteArrayInputStream.read(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        awaitCompletion();
        return byteArrayInputStream.skip(n);
    }

    @Override
    public int available() throws IOException {
        awaitCompletion();
        return byteArrayInputStream.available();
    }

    @Override
    public void close() throws IOException {
        synchronized (lock) {
            if (byteArrayInputStream != null) {
                byteArrayInputStream.close();
            }
        }
    }

    @Override
    public synchronized void mark(int readlimit) {
        awaitCompletion();
        byteArrayInputStream.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        awaitCompletion();
        byteArrayInputStream.reset();
    }

    @Override
    public boolean markSupported() {
        awaitCompletion();
        return byteArrayInputStream.markSupported();
    }

    private void awaitCompletion() {
        synchronized (lock) {
            try {
                if (byteArrayInputStream != null) {
                    return;
                }
                final byte[] data = this.futureData.get();
                byteArrayInputStream = new ByteArrayInputStream(data);

            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
