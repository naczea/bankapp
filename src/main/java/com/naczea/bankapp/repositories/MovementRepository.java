package com.naczea.bankapp.repositories;

import com.naczea.bankapp.entities.Movement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface MovementRepository extends JpaRepository<Movement, Long> {
    List<Movement> findByAccountId(Long accountId);
    List<Movement> findByAccountIdAndDateTimeBetween(Long accountId, Date dateTo, Date dateFrom);
}