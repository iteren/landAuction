package com.iteren.landauction.db.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iteren.landauction.model.anouncement.Announcement;

@Component
public class AnnouncementDaoImpl implements AnnouncementDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	@SuppressWarnings("unchecked")
	public List<Announcement> getAnouncemets() {
		Session session = this.sessionFactory.openSession();
		List<Announcement> announcements = session.createQuery("from Announcement").list();
		announcements.stream().forEach(a -> Hibernate.initialize(a.getPlots()));
		session.close();
		return announcements;
	}

	@Override
	public Long save(Announcement announcement) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.persist(announcement);
		tx.commit();
		session.close();
		return announcement.getAnnouncementId();
	}

}
