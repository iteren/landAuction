package com.iteren.landauction.db.dao;

import java.util.List;

import com.iteren.landauction.model.anouncement.Person;

public interface PersonDao {

	List<Person> getPersons();

	Long save(Person person);

}
