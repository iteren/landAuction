package com.iteren.landauction.db.dao;

import java.util.List;

import com.iteren.landauction.model.anouncement.Description;

public interface DescriptionDao {

	List<Description> getDescriptions();

	Long save(Description description);

	void delete(Description d);

}
