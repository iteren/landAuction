package com.iteren.landauction.db.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iteren.landauction.model.anouncement.Person;

@Component
public class PersonDaoImpl implements PersonDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Person> getPersons() {
		Session session = this.sessionFactory.openSession();
		@SuppressWarnings("unchecked")
		List<Person> persons = session.createQuery("from Person").list();
		session.close();
		return persons;
	}

	@Override
	public Long save(Person person) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.persist(person);
		tx.commit();
		session.close();
		return person.getPersonId();
	}

	@Override
	public void delete(Person person) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.delete(person);
		tx.commit();
		session.close();
	}

}
