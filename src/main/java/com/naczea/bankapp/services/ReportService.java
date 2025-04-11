package com.naczea.bankapp.services;

import com.naczea.bankapp.dto.ReportFormat;

import java.util.Date;
import java.util.List;

public interface ReportService {
    List<ReportFormat> findByDateRangeClientIdentification(String identification, Date startDate, Date endDate);
}