package com.smartops.smartserve.service.validation;

import com.smartops.smartserve.model.SSCameraEventRequest;
import com.smartops.smartserve.model.SSErrorModelDTO;

public interface SSCameraEventValidationService {
    SSErrorModelDTO validateCameraEvent(SSCameraEventRequest request, String currentTableStatus);
}
