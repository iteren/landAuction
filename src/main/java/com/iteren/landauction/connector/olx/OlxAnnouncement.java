package com.iteren.landauction.connector.olx;

import lombok.Data;

@Data
public class OlxAnnouncement {
	private Double price;
	private String currency;
	private String description;
	private String cadNum;
	private String imageUrl;
}
