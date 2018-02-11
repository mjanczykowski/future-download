package com.example.futuredownload.report;

import org.springframework.http.MediaType;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class GeneratedFile {

    private final String filename;

    private final MediaType mediaType;

    private final CompletableFuture<byte[]> futureData;

    GeneratedFile(String filename, MediaType mediaType, CompletableFuture<byte[]> futureData) {
        this.filename = filename;
        this.mediaType = mediaType;
        this.futureData = futureData;
    }

    public Future<byte[]> getFutureData() {
        return futureData;
    }

    public String getFilename() {
        return filename;
    }

    public MediaType getMediaType() {
        return mediaType;
    }
}
