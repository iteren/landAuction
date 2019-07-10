package com.iteren.landauction.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iteren.landauction.model.anouncement.Announcement;
import com.iteren.landauction.service.AnnouncementService;

@RestController
@RequestMapping("announcement")
public class AnnouncementController {
	@Autowired
	private AnnouncementService announcementService;
	
	@RequestMapping(method=RequestMethod.POST, path="/add", consumes={"application/json"}, produces={"application/json"})
	public Long addAnnouncement(@RequestBody Announcement announcement) {
		announcement = announcementService.populatePlotsInfo(announcement);
		return announcementService.addAnnouncement(announcement);
	}
	
	@RequestMapping(method=RequestMethod.GET, path="/list", consumes={"application/json"}, produces={"application/json"})
	public List<Announcement> getAnnouncements() {
		return announcementService.getAllAnnouncements();
	}
}
