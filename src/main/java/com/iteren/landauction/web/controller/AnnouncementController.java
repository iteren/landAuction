package com.iteren.landauction.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iteren.landauction.model.GenericRsponse;
import com.iteren.landauction.model.ParseAddForm;
import com.iteren.landauction.model.anouncement.Announcement;
import com.iteren.landauction.model.anouncement.Plot;
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
		String cadNum = announcement.getPlot().getCadNum() != null ? announcement.getPlot().getCadNum()
				: parsedAnnouncement.getPlot().getCadNum();
		if (cadNum == null) {
			return GenericRsponse.failureOf(parsedAnnouncement, "cadNum.dialog.show", "Please specify cadNum.");
		}
		Plot plot = announcementService.getPlotInfo(cadNum);
		parsedAnnouncement.setPlot(plot);
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
	public GenericRsponse<Announcement> parse(@RequestBody ParseAddForm form) {
		Announcement announcement = announcementService.parseAdd(form.getSourceLink());
		if (announcement.getPlot().getCadNum() == null) {
			return GenericRsponse.failureOf(announcement, "cadNum.dialog.show", "Please specify cadNum.");
		}
		return addAnnouncement(announcement);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(method = RequestMethod.POST, path = "/delete", consumes = { "application/json" }, produces = {
			"application/json" })
	public GenericRsponse delete(@RequestBody Announcement announcement) {
		announcementService.delete(announcement);
		return GenericRsponse.of(null);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(method = RequestMethod.POST, path = "/reload", consumes = { "application/json" }, produces = {
			"application/json" })
	public GenericRsponse reload(@RequestBody Announcement announcement) {
		announcementService.delete(announcement);
		return GenericRsponse.of(addAnnouncement(announcement));
	}

	@SuppressWarnings("rawtypes")
	@ExceptionHandler(Exception.class)
	public GenericRsponse handleError(HttpServletRequest req, Exception ex) {
		return GenericRsponse.failure("Exception : " + ex.getMessage());
	}
}
