package com.iteren.landauction.db.dao;

import java.util.List;

import com.iteren.landauction.model.anouncement.Announcement;
import com.iteren.landauction.model.anouncement.Plot;

public interface AnnouncementDao {

	List<Announcement> getAnouncemets();

	Long save(Announcement announcement);

	List<Announcement> getAnouncemetsFor(List<Plot> plots);

	void delete(Announcement announcement);

}
