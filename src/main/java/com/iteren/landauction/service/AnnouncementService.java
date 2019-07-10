package com.iteren.landauction.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iteren.landauction.db.dao.AnnouncementDao;
import com.iteren.landauction.db.dao.PersonDao;
import com.iteren.landauction.db.dao.PlotDao;
import com.iteren.landauction.model.anouncement.Announcement;

@Service
public class AnnouncementService {
	@Autowired
	private AnnouncementDao announcementDao;
	@Autowired
	private PlotDao plotDao;
	@Autowired
	private PersonDao personDao;

	public List<Announcement> getAllAnnouncements() {
		return announcementDao.getAnouncemets();
	}
	
	public Long addAnnouncement(Announcement announcement) {
		announcement.getPlots().forEach(plotDao::save);
		personDao.save(announcement.getOwner());
		return announcementDao.save(announcement);
	}
}
