package com.iteren.landauction.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iteren.landauction.db.dao.LocationDao;
import com.iteren.landauction.model.Location;
import com.iteren.landauction.model.MapCorners;

@Service("locationService")
public class LocationService {

	public LocationService() {
		System.out.println("TaskService.initialized!");
	}

	@Autowired
	private LocationDao locationDao;

	public List<Location> getAllLocations() {
		return locationDao.getLocations();
	}

	public List<Location> getLocationInRectangle(MapCorners corners) {
		return locationDao.getLocationsInRange(corners.getSeCorner().getLat(), corners.getNwCorner().getLat(),
				corners.getNwCorner().getLng(), corners.getSeCorner().getLng());
	}

	public void addLocation(Location location) {
		locationDao.save(location);
	}
}
