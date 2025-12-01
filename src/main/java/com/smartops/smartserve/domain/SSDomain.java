package com.smartops.smartserve.domain;

import java.util.Date;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class SSDomain {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@UuidGenerator
	private String uuid;

	@LastModifiedBy
	private String updatedBy;

	@CreatedBy
	private String createdBy;

	@CreatedDate
	private Date createdDateTime;

	@LastModifiedDate
	private Date updatedDateTime;

}
