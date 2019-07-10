package com.iteren.landauction.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.iteren.landauction.model.GeoLocationPoint;
import com.iteren.landauction.model.LandGovPoint;
import com.iteren.landauction.model.Location;
import com.iteren.landauction.model.ResponseLandGovMap;

@Service
public class CoordinateService {

	public Location getLocationForPlot(String cadastrNum) {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		RestTemplate restTemplate = new RestTemplate(factory);
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		messageConverters.add(converter);
		restTemplate.setMessageConverters(messageConverters);
		ResponseLandGovMap resp = restTemplate.getForObject(
				"https://map.land.gov.ua/kadastrova-karta/find-Parcel?cadnum=" + cadastrNum, ResponseLandGovMap.class);
		LandGovPoint point = resp.getCoords();
		return getCoordinatesForMap(point.getSt_xmin(), point.getSt_ymin(), point.getSt_xmax(), point.getSt_ymax());
	}

	public Location getCoordinatesForMap(Double xMin, Double yMin, Double xMax, Double yMax) {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		RestTemplate restTemplate = new RestTemplate(factory);
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		messageConverters.add(converter);
		restTemplate.setMessageConverters(messageConverters);
		Double x = (xMin + xMax) / 2;
		Double y = (yMin + yMax) / 2;
		GeoLocationPoint point = restTemplate.getForObject(
				"https://epsg.io/trans?x=" + x + "&y=" + y + "&s_srs=3857&t_srs=4326", GeoLocationPoint.class);
		Location location = new Location();
		location.setLat(point.getY());
		location.setLng(point.getX());
		return location;
	}

	public static void main(String[] args) {
		new CoordinateService().getCoordinatesForMap(0d, 0d, 0d, 0d);
		new CoordinateService().getLocationForPlot("4623610100:04:000:0294");
	}
}
