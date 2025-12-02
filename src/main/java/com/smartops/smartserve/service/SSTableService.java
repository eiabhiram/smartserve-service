package com.smartops.smartserve.service;

import java.util.List;
import java.util.Optional;

import com.smartops.smartserve.domain.SSTableState;
import com.smartops.smartserve.model.SSCameraEventRequest;
import com.smartops.smartserve.model.SSCustomerEventRequest;
import com.smartops.smartserve.model.SSTableUpdatePayload;
import com.smartops.smartserve.model.SSWaiterActionRequest;

public interface SSTableService {

	SSTableUpdatePayload createTable();

	SSTableUpdatePayload processCameraEvent(SSCameraEventRequest req);

	SSTableUpdatePayload processCustomerEvent(SSCustomerEventRequest req);

	SSTableUpdatePayload processWaiterAction(SSWaiterActionRequest req);

	List<SSTableState> getAll();

	Optional<SSTableState> get(Long tableId);

}
