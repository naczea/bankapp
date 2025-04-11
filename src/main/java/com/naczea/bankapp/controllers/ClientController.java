package com.naczea.bankapp.controllers;

import com.naczea.bankapp.entities.Client;
import com.naczea.bankapp.services.ClientService;
import com.naczea.bankapp.util.ResponseUtil;
import com.naczea.bankapp.util.ResponseUtilStatus;
import com.naczea.bankapp.util.ConstantsUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/clientes")
public class ClientController {

    private final ClientService clientService;
    private final PasswordEncoder passwordEncoder;

    public ClientController(ClientService clientService, PasswordEncoder passwordEncoder) {
        this.clientService = clientService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/clientByIdentification")
    public ResponseEntity<?> getByClientIdentificationRequest(@RequestParam(required = false) String clientIdentification){
        try {
            Client client = clientService.findByClientIdentification(clientIdentification);
            ResponseUtil responseUtil = new ResponseUtil(
                    client != null ? ResponseUtilStatus.OK: ResponseUtilStatus.ERROR,
                    client != null ? "Registro consultado con exito!": ConstantsUtil.NO_RESPONSE,
                    Client.class.getSimpleName(),
                    client
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

    @GetMapping("/clientByName")
    public ResponseEntity<?> getByClientNameRequest(@RequestParam(required = false) String clientName){
        try {
            Client client = clientService.findByClientName(clientName);
            ResponseUtil responseUtil = new ResponseUtil(
                    client != null ? ResponseUtilStatus.OK: ResponseUtilStatus.ERROR,
                    client != null ? "Registro consultado con exito!": ConstantsUtil.NO_RESPONSE,
                    Client.class.getSimpleName(),
                    client
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

    @PostMapping("/newCient")
    public ResponseEntity<?> createNewClientRequest(@RequestBody Client client){
        try {
            Client clientNew = clientService.saveClient(client);
            ResponseUtil responseUtil = new ResponseUtil(
                    clientNew != null ? ResponseUtilStatus.OK: ResponseUtilStatus.ERROR,
                    clientNew != null ? "Registro creado con exito!": ConstantsUtil.NO_RESPONSE,
                    Client.class.getSimpleName(),
                    clientNew
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

    @PutMapping("/updateClient/{id}")
    public ResponseEntity<?> updateClientRequest(@PathVariable Long id, @RequestBody Client client) {
        try {
            Client existingClient = clientService.findById(id);
            if (Objects.isNull(existingClient)) {
                return new ResponseEntity<>(new ResponseUtil(
                        ResponseUtilStatus.ERROR,
                        "Cliente no encontrado",
                        Client.class.getSimpleName(),
                        null
                ), HttpStatus.NOT_FOUND);
            }

            existingClient.setName(client.getName());
            existingClient.setGender(client.getGender());
            existingClient.setAge(client.getAge());
            existingClient.setIdentification(client.getIdentification());
            existingClient.setAddress(client.getAddress());
            existingClient.setPhoneNumber(client.getPhoneNumber());
            existingClient.setState(client.getState());

            if (client.getPassword() != null && !client.getPassword().isBlank()) {
                existingClient.setPassword(passwordEncoder.encode(client.getPassword()));
            }

            Client updatedClient = clientService.saveClient(existingClient);
            ResponseUtil responseUtil = new ResponseUtil(
                        updatedClient != null ? ResponseUtilStatus.OK: ResponseUtilStatus.ERROR,
                        updatedClient != null ? "Registro actualizado con exito!": ConstantsUtil.NO_RESPONSE,
                        Client.class.getSimpleName(),
                        updatedClient
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

    @DeleteMapping("/deleteClient/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        try {
            Client clientToDelete = clientService.findById(id);
            if (Objects.isNull(clientToDelete)) {
                return new ResponseEntity<>(new ResponseUtil(
                        ResponseUtilStatus.ERROR,
                        "Cliente no encontrado",
                        Client.class.getSimpleName(),
                        null
                ), HttpStatus.NOT_FOUND);
            }
            clientService.deleteClient(clientToDelete);
            return new ResponseEntity<>(new ResponseUtil(
                    ResponseUtilStatus.OK,
                    "Cliente eliminado con éxito",
                    Client.class.getSimpleName(),
                    null
            ), HttpStatus.OK);
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

    @PutMapping("/deactivateClient/{id}")
    public ResponseEntity<?> deactivateClient(@PathVariable Long id) {
        try {
            Client client = clientService.findById(id);
            if (Objects.isNull(client)) {
                return new ResponseEntity<>(new ResponseUtil(
                        ResponseUtilStatus.ERROR,
                        "Cliente no encontrado",
                        Client.class.getSimpleName(),
                        null
                ), HttpStatus.NOT_FOUND);
            }

            client.setState(false);
            Client updatedClient = clientService.saveClient(client);
            ResponseUtil responseUtil = new ResponseUtil(
                    updatedClient != null ? ResponseUtilStatus.OK: ResponseUtilStatus.ERROR,
                    updatedClient != null ? "Cliente desactivado con exito!": ConstantsUtil.NO_RESPONSE,
                    Client.class.getSimpleName(),
                    updatedClient
            );
            return new ResponseEntity<>(responseUtil, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ConstantsUtil.NO_RESPONSE);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseUtil(ResponseUtilStatus.ERROR, ConstantsUtil.SERVER_ERROR + e.getMessage(), null, null));
        }
    }
}