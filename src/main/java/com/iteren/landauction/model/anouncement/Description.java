package com.iteren.landauction.model.anouncement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "description")
public class Description {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DESCRIPTION_ID", unique = true, nullable = false)
	private Long descriptionId;
	@Column(name = "DESCRIPTION")
	private String description;
}
