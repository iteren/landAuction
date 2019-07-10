package com.iteren.landauction.db.dao;

import java.util.List;

import com.iteren.landauction.model.Location;

public interface LocationDao {

	List<Location> getLocations();
	
	List<Location> getLocationsInRange(Double latMin, Double latMax, Double lngMin, Double lngMax);
	
	void save(Location location);

}
