package com.iteren.landauction.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iteren.landauction.connector.olx.OlxAnnouncement;
import com.iteren.landauction.connector.olx.OlxConnector;
import com.iteren.landauction.db.dao.AnnouncementDao;
import com.iteren.landauction.db.dao.PlotDao;
import com.iteren.landauction.model.anouncement.Announcement;
import com.iteren.landauction.model.anouncement.Person;
import com.iteren.landauction.model.anouncement.Plot;
import com.iteren.landauction.model.landgovmap.PlotInfo;
import com.iteren.landauction.model.map.MapCorners;
import com.iteren.landauction.model.map.MapLocation;

@Service
public class AnnouncementService {
	@Autowired
	private AnnouncementDao announcementDao;
	@Autowired
	private PlotDao plotDao;
	@Autowired
	private LandGovService landGovService;
	@Autowired
	private OlxConnector olxConnector;

	public List<Announcement> getAllAnnouncements() {
		return announcementDao.getAnouncemets();
	}

	public Long addAnnouncement(Announcement announcement) {
		return announcementDao.save(announcement);
	}

	public Announcement populatePlotsInfo(Announcement announcement) {
		populatePlotInfo(announcement.getPlot());
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

	public List<Announcement> getAnnouncementsInLocation(MapCorners corners) {
		List<Plot> plots = plotDao.getPlotsInRange(corners.getSeCorner().getLat(), corners.getNwCorner().getLat(),
				corners.getNwCorner().getLng(), corners.getSeCorner().getLng());
		return announcementDao.getAnouncemetsFor(plots);
	}

	public Announcement parseAdd(String link) {
		OlxAnnouncement olxAnnouncement = olxConnector.getAnnouncement(link);
		if (olxAnnouncement == null) {
			return null;
		}
		Announcement announcement = new Announcement();
		announcement.setDescription(olxAnnouncement.getDescription());
		announcement.setPrice(olxAnnouncement.getPrice());
		announcement.setSourceLink(link);
		announcement.setPriceCurrency(olxAnnouncement.getCurrency());
		announcement.setImageUrl(olxAnnouncement.getImageUrl());
		Plot plot = new Plot();
		plot.setCadNum(olxAnnouncement.getCadNum());
		announcement.setPlot(plot);
		announcement.setOwner(new Person());
		return announcement;
	}
}
