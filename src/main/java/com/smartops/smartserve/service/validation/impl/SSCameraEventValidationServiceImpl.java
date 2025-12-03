package com.smartops.smartserve.service.validation.impl;

import org.springframework.stereotype.Service;

import com.smartops.smartserve.constants.SSTableStatus;
import com.smartops.smartserve.model.SSCameraEventRequest;
import com.smartops.smartserve.model.SSErrorModelDTO;
import com.smartops.smartserve.service.validation.SSCameraEventValidationService;

@Service
public class SSCameraEventValidationServiceImpl implements SSCameraEventValidationService {

    @Override
    public SSErrorModelDTO validateCameraEvent(SSCameraEventRequest request, String currentTableStatus) {
//        // Rule 1: Table FREE -> OCCUPIED after that cannot switch back to FREE unless waiter makes it free (MARK_TABLE_CLEANED)
//        if (isPersonNotDetectedAction(request.getAction())) {
//            if (SSTableStatus.OCCUPIED.name().equals(currentTableStatus)) {
//                return new SSErrorModelDTO(
//                    "Table is OCCUPIED. Only waiter can mark table as cleaned to make it FREE.",
//                    new Object[]{request.getTableId()}
//                );
//            }
//        }
//
//        // Rule 2: If table is already OCCUPIED, PERSON_DETECTED should be blocked
//        if (isPersonDetectedAction(request.getAction())) {
//            if (SSTableStatus.OCCUPIED.name().equals(currentTableStatus)) {
//                return new SSErrorModelDTO(
//                    "Table already OCCUPIED",
//                    new Object[]{request.getTableId()}
//                );
//            }
//        }
//
//        // Rule 3: Table FREE -> FREE ,action not required
//        // Rule 1 and 3 can be avoided if pushes not done for PERSON_NOT_DETECTED
//        if (isPersonNotDetectedAction(request.getAction())) {
//            if (SSTableStatus.FREE.name().equals(currentTableStatus)) {
//                return new SSErrorModelDTO(
//                    "Table is already FREE.",
//                    new Object[]{request.getTableId()}
//                );
//            }
//        }

        return null; 
    }
    
    private boolean isPersonNotDetectedAction(String action) {
        return "PERSON_NOT_DETECTED".equals(action);
    }

    private boolean isPersonDetectedAction(String action) {
        return "PERSON_DETECTED".equals(action);
    }
}
