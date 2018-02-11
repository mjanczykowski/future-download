package com.example.futuredownload.web;

import com.example.futuredownload.report.GeneratedFile;
import com.example.futuredownload.report.ReportProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
public class DownloadController {

    private final ReportProducer reportProducer;

    @Autowired
    public DownloadController(ReportProducer reportProducer) {
        this.reportProducer = reportProducer;
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    ResponseEntity<GeneratedFile> download(HttpServletResponse response) throws ExecutionException, InterruptedException, IOException {
        final GeneratedFile report = reportProducer.produce(UUID.randomUUID());

        return new ResponseEntity<>(report, HttpStatus.OK);
    }

}
