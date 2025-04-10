package com.naczea.bankapp.services;

import com.naczea.bankapp.entities.Movement;
import com.naczea.bankapp.repositories.MovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class MovementServiceImpl implements MovementService {
    private final MovementRepository movementRepository;

    public MovementServiceImpl(MovementRepository movementRepository) {
        this.movementRepository = movementRepository;
    }

    public Movement findById(Long id) {
        return movementRepository.findById(id).orElse(null);
    }

    public List<Movement> findByAccountId(Long accountId) {
        return movementRepository.findByAccountId(accountId);
    }

    public List<Movement> findByDateRangeAccountId(Date dateTo, Date dateFrom, Long accountId) {
        return movementRepository.findByAccountIdAndDateTimeBetween(accountId,dateTo, dateFrom);
    }

    public void deleteMovement(Movement movement) {
        movementRepository.delete(movement);
    }

    @Override
    @Transactional
    public Movement saveMovement(Movement movement){
        try {
            return movementRepository.save(movement);
        }catch (Exception e){
            e.printStackTrace();
            throw new IllegalArgumentException("Problems in the creation of [Movement]: " + e.getMessage());
        }
    }

}
