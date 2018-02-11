package com.example.futuredownload.report;

import java.util.UUID;

public interface ReportProducer {

    GeneratedFile produce(UUID reportID);

}
