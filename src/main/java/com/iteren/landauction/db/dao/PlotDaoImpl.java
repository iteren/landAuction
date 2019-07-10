package com.iteren.landauction.db.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iteren.landauction.model.anouncement.Plot;

@Component
public class PlotDaoImpl implements PlotDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Plot> getPlots() {
		Session session = this.sessionFactory.openSession();
		@SuppressWarnings("unchecked")
		List<Plot> plots = session.createQuery("from Plot").list();
		session.close();
		return plots;
	}

	@Override
	public Long save(Plot plot) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.persist(plot);
		tx.commit();
		session.close();
		return plot.getPlotId();
	}
	
	@Override
	public List<Plot> getPlotsInRange(Double latMin, Double latMax, Double lngMin, Double lngMax) {
		Session session = this.sessionFactory.openSession();
		Query query = session
				.createQuery("from Plot where lat > :latMin and lat < :latMax and lng > :lngMin and lng < :lngMax");
		query.setParameter("latMin", latMin);
		query.setParameter("latMax", latMax);
		query.setParameter("lngMin", lngMin);
		query.setParameter("lngMax", lngMax);
		@SuppressWarnings("unchecked")
		List<Plot> plots = query.list();
		session.close();
		return plots;
	}

}
