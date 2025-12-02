package com.smartops.smartserve.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartops.smartserve.domain.SSTableState;

@Repository
public interface SSTableStateRepository extends JpaRepository<SSTableState, Long> {

}
