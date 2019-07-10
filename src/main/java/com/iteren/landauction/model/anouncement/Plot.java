package com.iteren.landauction.model.anouncement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

@Data
@Entity
@Table(name = "plot", uniqueConstraints = { @UniqueConstraint(columnNames = "CAD_NUM") })
public class Plot {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PLOT_ID", unique = true, nullable = false)
	private Long plotId;
	@Column(name = "LAT")
	private Double lat;
	@Column(name = "LNG")
	private Double lng;
	@Column(name = "CAD_NUM", unique = true, nullable = false)
	private String cadNum;
	@Column(name = "SIZE")
	private Double size;
	@Column(name = "OWNERSHIP_CODE")
	private Integer ownershipcode;
	@Column(name = "PURPOSE")
	private String purpose;
	@Column(name = "USE")
	private String use;
}
