package com.example.futuredownload.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
public class ReportProducerImpl implements ReportProducer {

    private final TaskExecutor taskExecutor;

    @Autowired
    public ReportProducerImpl(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Override
    public GeneratedFile produce(UUID reportID) {
        final CompletableFuture<byte[]> futureData = new CompletableFuture<>();

        taskExecutor.execute(() -> {
            try {
                Thread.sleep(180000);
                futureData.complete(reportID.toString().getBytes(StandardCharsets.UTF_8));

            } catch (InterruptedException e) {
                futureData.completeExceptionally(e);
            }

        });

        return new GeneratedFile("file-name-" + LocalDateTime.now().toString() + ".txt", MediaType.APPLICATION_OCTET_STREAM, futureData);
    }
}
