package com.naczea.bankapp.controllers;

import com.naczea.bankapp.entities.Account;
import com.naczea.bankapp.services.AccountService;
import com.naczea.bankapp.util.ConstantsUtil;
import com.naczea.bankapp.util.ResponseUtil;
import com.naczea.bankapp.util.ResponseUtilStatus;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/cuentas")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/accountByNumber")
    public ResponseEntity<?> getByNumberRequest(@RequestParam(required = false) Long accountNumber) {
        try {
            Account account = accountService.findByAccountNumber(accountNumber);
            ResponseUtil responseUtil = new ResponseUtil(
                    account != null ? ResponseUtilStatus.OK: ResponseUtilStatus.ERROR,
                    account != null ? "Registro consultado con exito!": ConstantsUtil.NO_RESPONSE,
                    Account.class.getSimpleName(),
                    account
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

    @GetMapping("/accountByClientIdentification")
    public ResponseEntity<?> getByClientIdentificationRequest(@RequestParam(required = false) String clientIdentification) {
        try {
            List<Account> accounts = accountService.findByClientIdentification(clientIdentification);
            ResponseUtil responseUtil = new ResponseUtil(
                    accounts != null ? ResponseUtilStatus.OK: ResponseUtilStatus.ERROR,
                    accounts != null ? "Registros consultado con exito!": ConstantsUtil.NO_RESPONSE,
                    Account.class.getSimpleName(),
                    accounts
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

    @PostMapping("/newAccount")
    public ResponseEntity<?> createNewAccountRequest(@RequestBody Account account){
        try {
            Account accountNew = accountService.saveAccount(account);
            ResponseUtil responseUtil = new ResponseUtil(
                    accountNew != null ? ResponseUtilStatus.OK: ResponseUtilStatus.ERROR,
                    accountNew != null ? "Registro creado con exito!": ConstantsUtil.NO_RESPONSE,
                    Account.class.getSimpleName(),
                    accountNew
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

    @PutMapping("/updateAccount/{id}")
    public ResponseEntity<?> updateAccountRequest(@PathVariable Long id, @RequestBody Account account) {
        try {
            Account existingAccount = accountService.findById(id);
            if (Objects.isNull(existingAccount)) {
                return new ResponseEntity<>(new ResponseUtil(
                        ResponseUtilStatus.ERROR,
                        "Cuenta no encontrado",
                        Account.class.getSimpleName(),
                        null
                ), HttpStatus.NOT_FOUND);
            }

            existingAccount.setNumber(account.getNumber());
            existingAccount.setType(account.getType());
            existingAccount.setState(account.getState());
            existingAccount.setOpeningBalance(account.getOpeningBalance());
            existingAccount.setClient(account.getClient());

            Account updatedAccount = accountService.saveAccount(existingAccount);
            ResponseUtil responseUtil = new ResponseUtil(
                    updatedAccount != null ? ResponseUtilStatus.OK: ResponseUtilStatus.ERROR,
                    updatedAccount != null ? "Registro actualizado con exito!": ConstantsUtil.NO_RESPONSE,
                    Account.class.getSimpleName(),
                    updatedAccount
            );
            return new ResponseEntity<>(responseUtil, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ConstantsUtil.NO_RESPONSE);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseUtil(ResponseUtilStatus.ERROR, ConstantsUtil.DB_ERROR, null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseUtil(ResponseUtilStatus.ERROR, ConstantsUtil.SERVER_ERROR + e.getMessage(), null, null));
        }
    }

    @DeleteMapping("/deleteAccount/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        try {
            Account accountToDelete = accountService.findById(id);
            if (Objects.isNull(accountToDelete)) {
                return new ResponseEntity<>(new ResponseUtil(
                        ResponseUtilStatus.ERROR,
                        "Cuenta no encontrado",
                        Account.class.getSimpleName(),
                        null
                ), HttpStatus.NOT_FOUND);
            }
            accountService.deleteAccount(accountToDelete);
            return new ResponseEntity<>(new ResponseUtil(
                    ResponseUtilStatus.OK,
                    "Cuenta eliminada con éxito",
                    Account.class.getSimpleName(),
                    null
            ), HttpStatus.OK);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseUtil(ResponseUtilStatus.ERROR, ConstantsUtil.DB_ERROR, null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseUtil(ResponseUtilStatus.ERROR, ConstantsUtil.SERVER_ERROR + e.getMessage(), null, null));
        }
    }

    @PutMapping("/deactivateAccount/{id}")
    public ResponseEntity<?> deactivateAccount(@PathVariable Long id) {
        try {
            Account account = accountService.findById(id);
            if (Objects.isNull(account)) {
                return new ResponseEntity<>(new ResponseUtil(
                        ResponseUtilStatus.ERROR,
                        "Cuenta no encontrada",
                        Account.class.getSimpleName(),
                        null
                ), HttpStatus.NOT_FOUND);
            }

            account.setState(false);
            Account updatedAccount = accountService.saveAccount(account);
            ResponseUtil responseUtil = new ResponseUtil(
                    updatedAccount != null ? ResponseUtilStatus.OK: ResponseUtilStatus.ERROR,
                    updatedAccount != null ? "Cuenta desactivada con exito!": ConstantsUtil.NO_RESPONSE,
                    Account.class.getSimpleName(),
                    updatedAccount
            );
            return new ResponseEntity<>(responseUtil, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseUtil(ResponseUtilStatus.ERROR, ConstantsUtil.SERVER_ERROR + e.getMessage(), null, null));
        }
    }
}
