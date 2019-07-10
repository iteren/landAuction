package com.iteren.landauction.db.dao;

import java.util.List;

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

}
