package com.iteren.landauction.web.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.iteren.landauction.model.GenericRsponse;
import com.iteren.landauction.model.ParseAddForm;
import com.iteren.landauction.model.anouncement.Announcement;
import com.iteren.landauction.model.map.MapCorners;
import com.iteren.landauction.service.AnnouncementService;

@RestController
@RequestMapping("announcement")
public class AnnouncementController {
	@Autowired
	private AnnouncementService announcementService;

	@RequestMapping(method = RequestMethod.POST, path = "/add", consumes = { "application/json" }, produces = {
			"application/json" })
	public GenericRsponse<Announcement> addAnnouncement(@RequestBody Announcement announcement) {
		Announcement parsedAnnouncement = announcementService.parseAdd(announcement.getSourceLink());
		parsedAnnouncement = announcementService.populatePlotsInfo(announcement);
		announcementService.addAnnouncement(parsedAnnouncement);
		return GenericRsponse.of(parsedAnnouncement);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/list", consumes = { "application/json" }, produces = {
			"application/json" })
	public GenericRsponse<List<Announcement>> getAnnouncements() {
		return GenericRsponse.of(announcementService.getAllAnnouncements());
	}

	@RequestMapping(method = RequestMethod.POST, path = "/list", consumes = { "application/json" }, produces = {
			"application/json" })
	public GenericRsponse<List<Announcement>> getLocations(@RequestBody MapCorners corners) {
		return GenericRsponse.of(announcementService.getAnnouncementsInLocation(corners));
	}

	@RequestMapping(method = RequestMethod.POST, path = "/parseAdd", consumes = { "application/json" }, produces = {
			"application/json" })
	public GenericRsponse<Announcement> parseAdd(@RequestBody ParseAddForm form) {
		Announcement announcement = announcementService.parseAdd(form.getSourceLink());
		return addAnnouncement(announcement);
	}

	@SuppressWarnings("rawtypes")
	@ExceptionHandler(Exception.class)
	public GenericRsponse handleError(HttpServletRequest req, Exception ex) {
		return GenericRsponse.failure("Exception : " + ex.getMessage());
	}
}
