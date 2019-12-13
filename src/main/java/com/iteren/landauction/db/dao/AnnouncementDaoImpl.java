package com.iteren.landauction.db.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.iteren.landauction.model.anouncement.Announcement;
import com.iteren.landauction.model.anouncement.Description;
import com.iteren.landauction.model.anouncement.Plot;

@Component
public class AnnouncementDaoImpl implements AnnouncementDao {

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private PlotDao plotDao;
	@Autowired
	private DescriptionDao descriptionDao;
	@Autowired
	private PersonDao personDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<Announcement> getAnouncemets() {
		Session session = this.sessionFactory.openSession();
		List<Announcement> announcements = session.createQuery("from Announcement").list();
		announcements.stream().forEach(a -> Hibernate.initialize(a.getDescriptions()));
		announcements.stream().forEach(a -> a.setDescription(String.join("",
				a.getDescriptions().stream().map(Description::getDescription).collect(Collectors.toList()))));
		session.close();
		return announcements;
	}

	@Override
	public Long save(Announcement announcement) {
		List<Description> descriptions = new ArrayList<>();
		for (int i = 0; i < announcement.getDescription().length(); i += 254) {
			Description d = new Description();
			d.setDescription(announcement.getDescription().substring(i, Math.min(i + 254, announcement.getDescription().length())));
			descriptionDao.save(d);
			descriptions.add(d);
		}
		announcement.setDescriptions(descriptions);
		
		plotDao.save(announcement.getPlot());
		personDao.save(announcement.getOwner());
		
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.persist(announcement);
		tx.commit();
		session.close();
		return announcement.getAnnouncementId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Announcement> getAnouncemetsFor(List<Plot> plots) {
		if (CollectionUtils.isEmpty(plots)) {
			return Collections.emptyList();
		}
		Session session = this.sessionFactory.openSession();
		Query query = session.createQuery("SELECT a FROM Announcement a WHERE a.plot.plotId in(:plotIds)");
		query.setParameterList("plotIds", plots.stream().map(Plot::getPlotId).collect(Collectors.toList()));
		List<Announcement> announcements = query.list();
		announcements.stream().forEach(a -> Hibernate.initialize(a.getDescriptions()));
		announcements.stream().forEach(a -> a.setDescription(String.join("",
				a.getDescriptions().stream().map(Description::getDescription).collect(Collectors.toList()))));
		session.close();
		return announcements;
	}

}
