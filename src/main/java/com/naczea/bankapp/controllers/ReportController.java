    package com.naczea.bankapp.controllers;

import com.naczea.bankapp.dto.ReportFormat;
import com.naczea.bankapp.services.ReportService;
import com.naczea.bankapp.util.ConstantsUtil;
import com.naczea.bankapp.util.ResponseUtil;
import com.naczea.bankapp.util.ResponseUtilStatus;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/reportes")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/reportByDate")
    public ResponseEntity<?> getByDateRangeClientIdentificationRequest(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(required = false) String identification){
        try {
            List<ReportFormat> report = reportService.findByDateRangeClientIdentification(identification, startDate, endDate);
            ResponseUtil responseUtil = new ResponseUtil(
                    report != null ? ResponseUtilStatus.OK: ResponseUtilStatus.ERROR,
                    report != null ? "Registros consultado con exito!": ConstantsUtil.NO_RESPONSE,
                    ReportFormat.class.getSimpleName(),
                    report
            );
            return new ResponseEntity<>(responseUtil, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ConstantsUtil.NO_RESPONSE);
        }catch(DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseUtil(ResponseUtilStatus.ERROR,ConstantsUtil.DB_ERROR,null, null));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseUtil(ResponseUtilStatus.ERROR, ConstantsUtil.SERVER_ERROR + e.getMessage(), null, null));
        }
    }
}
