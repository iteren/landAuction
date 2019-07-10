package com.iteren.landauction.db.dao;

import java.util.List;

import com.iteren.landauction.model.map.MapLocation;

public interface LocationDao {

	List<MapLocation> getLocations();
	
	List<MapLocation> getLocationsInRange(Double latMin, Double latMax, Double lngMin, Double lngMax);
	
	void save(MapLocation location);

}
