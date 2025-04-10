package com.naczea.bankapp.services;

import com.naczea.bankapp.entities.Movement;

import java.util.Date;
import java.util.List;

public interface MovementService {
    Movement findById(Long id);
    List<Movement> findByAccountId(Long accountId);
    List<Movement> findByDateRangeAccountId(Date dateTo, Date dateFrom, Long accountId);
    Movement saveMovement(Movement movement);
    void deleteMovement(Movement movement);
}