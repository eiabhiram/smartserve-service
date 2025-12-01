package com.smartops.smartserve.domain;

import com.smartops.smartserve.constants.SSTableStatus;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SSTableState extends SSDomain {

	private SSTableStatus tableStatus;

	private String lastEvent;

	private String lastConfidence;

}
