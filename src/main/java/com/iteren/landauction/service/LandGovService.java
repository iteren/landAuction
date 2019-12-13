package com.iteren.landauction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.iteren.landauction.model.landgovmap.LandGovMapCoordinates;
import com.iteren.landauction.model.landgovmap.LandGovPoint;
import com.iteren.landauction.model.landgovmap.PlotInfo;
import com.iteren.landauction.model.landgovmap.PlotInfoResponse;
import com.iteren.landauction.model.map.Map3dPoint;
import com.iteren.landauction.model.map.MapLocation;

@Service
public class LandGovService {
	private static final String TANSFORM_COORDINATES = "https://epsg.io/trans";
	private static final String GET_LAND_GOV_COORDINATES = "https://map.land.gov.ua/kadastrova-karta/find-Parcel";
	private static final String GET_LAND_GOV_PLOT_INFO = "https://map.land.gov.ua/kadastrova-karta/get-parcel-Info";
	@Autowired
	private ExternalRestServiceHelper externalRestServiceHelper;

	public PlotInfo getInfoForPlot(String cadastrNum) {
		String[] params = cadastrNum.split(":");
		if (params.length < 4) {
			throw new IllegalArgumentException("Incorrect cadNum:" + cadastrNum);
		}
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(GET_LAND_GOV_PLOT_INFO)
				.queryParam("koatuu", params[0]).queryParam("zone", params[1]).queryParam("quartal", params[2])
				.queryParam("parcel", params[3]);
		HttpEntity<PlotInfoResponse> entity = externalRestServiceHelper.executeExternalApi(builder, PlotInfoResponse.class);
		return entity.getBody() == null ? null : entity.getBody().getData().get(0);
	}

	public MapLocation getLocationForPlot(String cadastrNum) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(GET_LAND_GOV_COORDINATES).queryParam("cadnum",
				cadastrNum);
		HttpEntity<LandGovMapCoordinates> entity = externalRestServiceHelper.executeExternalApi(builder,
				LandGovMapCoordinates.class);
		LandGovPoint point = entity.getBody().getCoords();
		return getCoordinatesForMap(point.getSt_xmin(), point.getSt_ymin(), point.getSt_xmax(), point.getSt_ymax());
	}

	public MapLocation getCoordinatesForMap(Double xMin, Double yMin, Double xMax, Double yMax) {
		Double x = (xMin + xMax) / 2;
		Double y = (yMin + yMax) / 2;
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(TANSFORM_COORDINATES)
				.queryParam("s_srs", "3857").queryParam("t_srs", "4326").queryParam("x", x).queryParam("y", y);
		HttpEntity<Map3dPoint> entity = externalRestServiceHelper.executeExternalApi(builder, Map3dPoint.class);
		return new MapLocation(entity.getBody().getY(), entity.getBody().getX());
	}

	public static void main(String[] args) {
		MapLocation loc = new LandGovService().getLocationForPlot("4623610100:04:000:0294");
		PlotInfo inf = new LandGovService().getInfoForPlot("4623610100:04:000:0294");
		System.out.println(loc);
		System.out.println(inf);
	}

}
