package com.iteren.landauction.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iteren.landauction.model.map.MapLocation;
import com.iteren.landauction.model.map.MapCorners;
import com.iteren.landauction.service.LandGovService;
import com.iteren.landauction.service.LocationService;

@RestController
public class LocationController {

	@Autowired
	private LocationService locationService;
	@Autowired
	private LandGovService coordinateService;
	
	@RequestMapping(method=RequestMethod.POST, path="/addLocation", consumes={"application/json"}, produces={"application/json"})
	public MapLocation addLocation(@RequestBody MapLocation location) {
		locationService.addLocation(location);
		return location;
	}
	
	@RequestMapping(method=RequestMethod.GET, path="/getLocations", consumes={"application/json"}, produces={"application/json"})
	public List<MapLocation> getLocations() {
		return locationService.getAllLocations();
	}
	
	@RequestMapping(method=RequestMethod.POST, path="/getLocations", consumes={"application/json"}, produces={"application/json"})
	public List<MapLocation> getLocations(@RequestBody MapCorners corners) {
		return locationService.getLocationInRectangle(corners);
	}
	
	@RequestMapping(method=RequestMethod.GET, path="/addPlot", consumes={"application/json"}, produces={"application/json"})
	public MapLocation addPlot(@RequestParam String cadNum) {
		MapLocation plotLocation = coordinateService.getLocationForPlot(cadNum);
		locationService.addLocation(plotLocation);
		return plotLocation;
	}
}
