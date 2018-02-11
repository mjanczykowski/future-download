package com.example.futuredownload.web;

import com.example.futuredownload.report.GeneratedFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
public class GeneratedFileHttpMessageConverter extends AbstractHttpMessageConverter<GeneratedFile> {

    public GeneratedFileHttpMessageConverter() {
        super(MediaType.ALL);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return GeneratedFile.class.isAssignableFrom(clazz);
    }

    @Override
    protected GeneratedFile readInternal(Class<? extends GeneratedFile> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        throw new UnsupportedOperationException("readInternal");
    }

    @Override
    protected MediaType getDefaultContentType(GeneratedFile generatedFile) throws IOException {
        return generatedFile.getMediaType();
    }

    @Override
    protected Long getContentLength(GeneratedFile generatedFile, MediaType contentType) throws IOException {
        Future<byte[]> futureData = generatedFile.getFutureData();
        if (!futureData.isDone()) {
            return null;
        }
        try {
            return (long) futureData.get().length;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void addDefaultHeaders(HttpHeaders headers, GeneratedFile generatedFile, MediaType contentType) throws IOException {
        super.addDefaultHeaders(headers, generatedFile, contentType);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + generatedFile.getFilename());
    }

    @Override
    protected void writeInternal(GeneratedFile generatedFile, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        final OutputStream outputMessageBody = outputMessage.getBody();

        // first write headers to start download
        outputMessageBody.flush();

        final InputStream inputStream = new FutureByteArrayInputStream(generatedFile.getFutureData());

        StreamUtils.copy(inputStream, outputMessageBody);
    }
}
