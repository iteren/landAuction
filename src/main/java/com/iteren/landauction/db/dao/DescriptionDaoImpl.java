package com.iteren.landauction.db.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iteren.landauction.model.anouncement.Description;

@Component
public class DescriptionDaoImpl implements DescriptionDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Description> getDescriptions() {
		Session session = this.sessionFactory.openSession();
		@SuppressWarnings("unchecked")
		List<Description> descriptions = session.createQuery("from Description").list();
		session.close();
		return descriptions;
	}

	@Override
	public Long save(Description description) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.persist(description);
		tx.commit();
		session.close();
		return description.getDescriptionId();
	}


}
