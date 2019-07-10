package com.iteren.landauction.model.landgovmap;

import java.util.List;

import lombok.Data;

@Data
public class PlotInfoResponse {
	private Boolean status;
	private List<PlotInfo> data;
}
