package com.iteren.landauction.model;

import java.util.List;

import org.springframework.util.CollectionUtils;

import lombok.Data;

@Data
public class ResponseLandGovMap {
	private boolean status;
	private List<LandGovPoint> data;
	private int isArchTable;

	public LandGovPoint getCoords() {
		if (CollectionUtils.isEmpty(data)) {
			return null;
		}
		return data.get(0);
	}
}
