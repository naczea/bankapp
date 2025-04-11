package com.naczea.bankapp.services;

import com.naczea.bankapp.dto.ReportFormat;
import com.naczea.bankapp.entities.Client;
import com.naczea.bankapp.entities.Movement;
import com.naczea.bankapp.repositories.ClientRepository;
import com.naczea.bankapp.repositories.MovementRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    private static final Logger LOGGER = Logger.getLogger(ReportServiceImpl.class.getName());
    private final MovementRepository movementRepository;
    private final ClientRepository clientRepository;

    public ReportServiceImpl(MovementRepository movementRepository, ClientRepository clientRepository) {
        this.movementRepository = movementRepository;
        this.clientRepository = clientRepository;
    }

    public List<ReportFormat> findByDateRangeClientIdentification(String identification, Date startDate, Date endDate) {
        Client client = clientRepository.findByIdentification(identification);
        LOGGER.log(Level.INFO, "CLIENT: " + client.getName());
        List<Movement> movements = movementRepository.findByAccountClientIdentificationAndDateTimeBetween(identification, startDate, endDate);
        LOGGER.log(Level.INFO, "MOVEMENTS: " + movements.size());
        List<ReportFormat> reportFormats = new ArrayList<>();
        //Map grouping by account
        Map<Long, List<Movement>> movementsByAccountId = movements.stream()
                .filter(movement -> movement.getAccount() != null && movement.getAccount().getId() != null)
                .collect(Collectors.groupingBy(movement -> movement.getAccount().getId()));

        movementsByAccountId.forEach((accountId, movementList) -> {
            //The movements are ordered by date
            movementList.sort(Comparator.comparing(Movement::getDateTime));

            for (int i = 0; i < movementList.size(); i++) {
                Movement current = movementList.get(i);
                Movement next = (i + 1 < movementList.size()) ? movementList.get(i + 1) : null;

                ReportFormat report = new ReportFormat();
                report.setDate(current.getDateTime());
                report.setClientName(client.getName());
                report.setNumberAccount(current.getAccount().getNumber());
                report.setType(current.getAccount().getType().toString());
                report.setInicialBalance(current.getBalance());
                report.setState(current.getAccount().getState());
                report.setValue(current.getValueTransaction());

                //If there is no next movement, it is the last movement and the account balance is added to it
                if (Objects.nonNull(next)) {
                    report.setFinalBalance(next.getBalance());
                } else {
                    report.setFinalBalance(current.getAccount().getOpeningBalance());
                }
                reportFormats.add(report);
            }
        });

        return reportFormats;
    }
}