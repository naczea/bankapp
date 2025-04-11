package com.naczea.bankapp.services;

import com.naczea.bankapp.entities.Account;
import com.naczea.bankapp.entities.Movement;
import com.naczea.bankapp.exception.AccountNotFoundException;
import com.naczea.bankapp.exception.InsufficientBalanceException;
import com.naczea.bankapp.repositories.AccountRepository;
import com.naczea.bankapp.repositories.MovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class MovementServiceImpl implements MovementService {
    private static final Logger LOGGER = Logger.getLogger(MovementServiceImpl.class.getName());
    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;

    public MovementServiceImpl(MovementRepository movementRepository, AccountRepository accountRepository) {
        this.movementRepository = movementRepository;
        this.accountRepository = accountRepository;
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
        Account account = accountRepository.findById(movement.getAccount().getId()).orElse(null);
        if(Objects.isNull(account)){
            throw new AccountNotFoundException("Cuenta con ID " + movement.getAccount().getId() + " no encontrada.");
        }

        BigDecimal value = movement.getValueTransaction();
        if(value.compareTo(BigDecimal.ZERO) >= 0){
            return depositMoney(movement, account);
        }else{
            return withdrawalMoney(movement,account);
        }
    }

    public Movement depositMoney(Movement movement, Account account){
        LOGGER.log(Level.INFO, "MOVEMENT TYPE: DEPOSIT");
        BigDecimal balance = account.getOpeningBalance();
        BigDecimal newBalance = balance.add(movement.getValueTransaction());
        return updateTransaction(movement, account, newBalance, balance);
    }

    public Movement withdrawalMoney(Movement movement, Account account){
        LOGGER.log(Level.INFO, "MOVEMENT TYPE: WITHDRAWAL");
        BigDecimal balance = account.getOpeningBalance();
        if(movement.getValueTransaction().abs().compareTo(balance) <= 0){
            BigDecimal newBalance = balance.subtract(movement.getValueTransaction().abs());
            return updateTransaction(movement, account, newBalance, balance);
        }else{
            throw new InsufficientBalanceException("Saldo insuficiente para realizar el retiro.");
        }
    }

    private Movement updateTransaction(Movement movement, Account account, BigDecimal newBalance, BigDecimal balance) {
        newBalance = newBalance.setScale(2, BigDecimal.ROUND_HALF_UP);
        Integer movementNumber = account.getMovementNumber() + 1;
        account.setMovementNumber(movementNumber);
        account.setOpeningBalance(newBalance);
        accountRepository.save(account);
        LOGGER.log(Level.INFO, "FINISH UPDATE ACCOUNT WITH NUMBER {0} <====== ", account.getNumber());
        movement.setType(account.getType());
        movement.setBalance(balance);
        LOGGER.log(Level.INFO, "FINISH SAVE MOVEMENT WITH VALUE {0} <====== ", movement.getValueTransaction());
        return movementRepository.save(movement);
    }

}
