package com.iteren.landauction.db.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iteren.landauction.model.Location;

@Component
public class LocationDaoImpl implements LocationDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Location> getLocations() {
		Session session = this.sessionFactory.openSession();
		@SuppressWarnings("unchecked")
		List<Location> locations = session.createQuery("from Location").list();
		session.close();
		return locations;
	}

	@Override
	public List<Location> getLocationsInRange(Double latMin, Double latMax, Double lngMin, Double lngMax) {
		Session session = this.sessionFactory.openSession();
		Query query = session
				.createQuery("from Location where lat > :latMin and lat < :latMax and lng > :lngMin and lng < :lngMax");
		query.setParameter("latMin", latMin);
		query.setParameter("latMax", latMax);
		query.setParameter("lngMin", lngMin);
		query.setParameter("lngMax", lngMax);
		@SuppressWarnings("unchecked")
		List<Location> locations = query.list();
		session.close();
		return locations;
	}

	@Override
	public void save(Location location) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.persist(location);
		tx.commit();
		session.close();
	}

}
