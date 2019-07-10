package com.iteren.landauction.model;

import lombok.Data;

@Data
public class MapCorners {
	private Location neCorner;
	private Location swCorner;
	private Location nwCorner;
	private Location seCorner;
}
