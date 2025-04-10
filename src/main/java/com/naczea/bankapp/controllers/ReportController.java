    package com.naczea.bankapp.controllers;

import com.naczea.bankapp.entities.Movement;
import com.naczea.bankapp.services.MovementService;
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

    private final MovementService movementService;

    public ReportController(MovementService movementService) {
        this.movementService = movementService;
    }

    @GetMapping("/movementsByDateRangeClientId")
    public ResponseEntity<?> getByDateRangeClientIdRequest(
            @RequestParam("dateFrom") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFrom,
            @RequestParam("dateTo") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTo,
            @RequestParam(required = false) Long accountId){
        try {
            List<Movement> movements = movementService.findByDateRangeAccountId(dateTo, dateFrom, accountId);
            ResponseUtil responseUtil = new ResponseUtil(
                    movements != null ? ResponseUtilStatus.OK: ResponseUtilStatus.ERROR,
                    movements != null ? "Registro consultado con exito!": ConstantsUtil.NO_RESPONSE,
                    Movement.class.getSimpleName(),
                    movements
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
