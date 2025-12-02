package com.smartops.smartserve.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartops.smartserve.domain.SSEventsLog;

@Repository
public interface SSEventLogRepository extends JpaRepository<SSEventsLog, Long>{

}
