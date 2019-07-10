package com.iteren.landauction.db.dao;

import java.util.List;

import com.iteren.landauction.model.anouncement.Plot;

public interface PlotDao {

	List<Plot> getPlots();

	Long save(Plot plot);

}
