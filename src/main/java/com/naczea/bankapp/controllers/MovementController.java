package com.naczea.bankapp.controllers;

import com.naczea.bankapp.entities.Movement;
import com.naczea.bankapp.exception.AccountNotFoundException;
import com.naczea.bankapp.exception.InsufficientBalanceException;
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
import java.util.Objects;

@RestController
@RequestMapping("/movimientos")
public class MovementController {

    private final MovementService movementService;

    public MovementController(MovementService movementService) {
        this.movementService = movementService;
    }

    @GetMapping("/movementsByAccountId")
    public ResponseEntity<?> getByAccountIdRequest(@RequestParam(required = false) Long accountId){
        try {
            List<Movement> movements = movementService.findByAccountId(accountId);
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

    @GetMapping("/movementsByDateRangeAccountId")
    public ResponseEntity<?> getByDateRangeAccountIdRequest(
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

    @PostMapping("/newMovement")
    public ResponseEntity<?> createNewMovementRequest(@RequestBody Movement movement){
        try {
            Movement movementNew = movementService.saveMovement(movement);
            ResponseUtil responseUtil = new ResponseUtil(
                    movementNew != null ? ResponseUtilStatus.OK: ResponseUtilStatus.ERROR,
                    movementNew != null ? "Registro creado con exito!": ConstantsUtil.NO_RESPONSE,
                    Movement.class.getSimpleName(),
                    movementNew
            );
            return new ResponseEntity<>(responseUtil, HttpStatus.OK);
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseUtil(ResponseUtilStatus.ERROR, e.getMessage(), null, null));
        } catch (InsufficientBalanceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseUtil(ResponseUtilStatus.ERROR, e.getMessage(), null, null));
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

    @PutMapping("/updateMovement/{id}")
    public ResponseEntity<?> updateMovementRequest(@PathVariable Long id, @RequestBody Movement movement) {
        try {
            Movement existingMovement = movementService.findById(id);
            if (Objects.isNull(existingMovement)) {
                return new ResponseEntity<>(new ResponseUtil(
                        ResponseUtilStatus.ERROR,
                        "Movimiento no encontrado",
                        Movement.class.getSimpleName(),
                        null
                ), HttpStatus.NOT_FOUND);
            }

            existingMovement.setDateTime(movement.getDateTime());
            existingMovement.setType(movement.getType());
            existingMovement.setBalance(movement.getBalance());
            existingMovement.setValueTransaction(movement.getValueTransaction());
            existingMovement.setAccount(movement.getAccount());

            Movement updatedMovement = movementService.saveMovement(existingMovement);
            ResponseUtil responseUtil = new ResponseUtil(
                    updatedMovement != null ? ResponseUtilStatus.OK: ResponseUtilStatus.ERROR,
                    updatedMovement != null ? "Registro actualizado con exito!": ConstantsUtil.NO_RESPONSE,
                    Movement.class.getSimpleName(),
                    updatedMovement
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

    @DeleteMapping("/deleteMovement/{id}")
    public ResponseEntity<?> deleteMovement(@PathVariable Long id) {
        try {
            Movement movementToDelete = movementService.findById(id);
            if (Objects.isNull(movementToDelete)) {
                return new ResponseEntity<>(new ResponseUtil(
                        ResponseUtilStatus.ERROR,
                        "Movimiento no encontrado",
                        Movement.class.getSimpleName(),
                        null
                ), HttpStatus.NOT_FOUND);
            }
            movementService.deleteMovement(movementToDelete);
            return new ResponseEntity<>(new ResponseUtil(
                    ResponseUtilStatus.OK,
                    "Movimiento eliminado con éxito",
                    Movement.class.getSimpleName(),
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
}
