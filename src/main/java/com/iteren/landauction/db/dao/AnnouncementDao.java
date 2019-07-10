package com.iteren.landauction.db.dao;

import java.util.List;

import com.iteren.landauction.model.anouncement.Announcement;

public interface AnnouncementDao {

	List<Announcement> getAnouncemets();

	Long save(Announcement announcement);

}
