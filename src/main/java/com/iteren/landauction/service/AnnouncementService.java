package com.iteren.landauction.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iteren.landauction.db.dao.AnnouncementDao;
import com.iteren.landauction.db.dao.PersonDao;
import com.iteren.landauction.db.dao.PlotDao;
import com.iteren.landauction.model.anouncement.Announcement;
import com.iteren.landauction.model.anouncement.Plot;
import com.iteren.landauction.model.landgovmap.PlotInfo;
import com.iteren.landauction.model.map.MapLocation;

@Service
public class AnnouncementService {
	@Autowired
	private AnnouncementDao announcementDao;
	@Autowired
	private PlotDao plotDao;
	@Autowired
	private PersonDao personDao;
	@Autowired
	private LandGovService landGovService;

	public List<Announcement> getAllAnnouncements() {
		return announcementDao.getAnouncemets();
	}
	
	public Long addAnnouncement(Announcement announcement) {
		announcement.getPlots().forEach(plotDao::save);
		personDao.save(announcement.getOwner());
		return announcementDao.save(announcement);
	}
	
	public Announcement populatePlotsInfo(Announcement announcement) {
		announcement.getPlots().stream().forEach(this::populatePlotInfo);
		return announcement;
	}
	
	public Plot populatePlotInfo(Plot plot) {
		PlotInfo plotInfo = landGovService.getInfoForPlot(plot.getCadNum());
		plot.setOwnershipcode(plotInfo.getOwnershipcode());
		plot.setPurpose(plotInfo.getPurpose());
		plot.setSize(plotInfo.getArea());
		plot.setUse(plotInfo.getUse());
		MapLocation loc = landGovService.getLocationForPlot(plot.getCadNum());
		plot.setLat(loc.getLat());
		plot.setLng(loc.getLng());
		return plot;
	}
}
