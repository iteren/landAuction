package com.iteren.landauction.model.anouncement;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "announcement")
public class Announcement {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ANNOUNCEMENT_ID", unique = true, nullable = false)
	private Long announcementId;
	@Column(name = "TITLE")
	private String title;
	@Column(name = "DESCRIPTION")
	private String description;
	@Column(name = "PRICE")
	private Double price;
	@OneToMany
	@JoinTable(name = "announcement_to_plot", joinColumns = {
			@JoinColumn(name = "ANNOUNCEMENT_ID", referencedColumnName = "ANNOUNCEMENT_ID") }, inverseJoinColumns = {
					@JoinColumn(name = "PLOT_ID", referencedColumnName = "PLOT_ID") })
	private List<Plot> plots;
	@Column(name = "CREATED_DATE")
	private Date created;
	@Column(name = "UPDATED_DATE")
	private Date updated;
	@ManyToOne
	@JoinColumn(name = "OWNER_ID")
	private Person owner;
}
