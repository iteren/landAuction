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
@Table(name = "person")
public class Person {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "PERSON_ID", unique = true, nullable = false)
	private Long personId;
	@Column(name = "NAME")
	private String name;
	@Column(name = "PHONE")
	private String phone;
}
