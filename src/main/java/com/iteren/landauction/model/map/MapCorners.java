package com.iteren.landauction.model.map;

import lombok.Data;

@Data
public class MapCorners {
	private MapLocation neCorner;
	private MapLocation swCorner;
	private MapLocation nwCorner;
	private MapLocation seCorner;
}
