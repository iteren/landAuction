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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

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
	@OneToMany
	@JoinTable(name = "announcement_to_description", joinColumns = {
			@JoinColumn(name = "ANNOUNCEMENT_ID", referencedColumnName = "ANNOUNCEMENT_ID") }, inverseJoinColumns = {
					@JoinColumn(name = "DESCRIPTION_ID", referencedColumnName = "DESCRIPTION_ID") })
	private List<Description> descriptions;
	@Transient
	private String description;
	@Column(name = "PRICE")
	private Double price;
	@Column(name = "PRICE_CURRENCY")
	private String priceCurrency;
	@OneToOne
	@JoinColumn(name = "PLOT_ID")
	private Plot plot;
	@Column(name = "CREATED_DATE")
	private Date created;
	@Column(name = "UPDATED_DATE")
	private Date updated;
	@ManyToOne
	@JoinColumn(name = "OWNER_ID")
	private Person owner;
	@Column(name = "SOURCE_LINK")
	private String sourceLink;
	@Column(name = "IMAGE_URL")
	private String imageUrl;
}
