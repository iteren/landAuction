package com.iteren.landauction.connector.olx;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import com.iteren.landauction.service.ExternalRestServiceHelper;

@Service
public class OlxConnector {
	private static Pattern pricePattern = Pattern.compile(".*\"ad_price\":\"(\\d+)\".*");
	private static Pattern descriptionPattern = Pattern.compile(
			".*<div class=\"clr lheight20 large\" id=\"textContent\">([\\w\\s\\W]+?)<\\/div>", Pattern.MULTILINE);
	private static Pattern currencyPattern = Pattern.compile(".*\\\"price_currency\\\":\\\"(\\w+?)\\\".*");
	private static Pattern cadNumPattern = Pattern.compile("(\\d{10}:\\d{2}:\\d{3}:\\d{4})");
	private static Pattern imgPattern = Pattern.compile("<meta property=\"og:image\" content=\"(.+?)\"\\/>");
	@Autowired
	private ExternalRestServiceHelper externalRestServiceHelper;

	public OlxAnnouncement parse(String content) {
		if (StringUtils.isEmpty(content)) {
			throw new IllegalArgumentException("Cannot get Olx announcement, Please check the url.");
		}
		OlxAnnouncement announcement = new OlxAnnouncement();
		Matcher mPrice = pricePattern.matcher(content);
		if (mPrice.find()) {
			String price = mPrice.group(1).trim();
			announcement.setPrice(Double.parseDouble(price));
		}

		Matcher mCurrency = currencyPattern.matcher(content);
		if (mCurrency.find()) {
			String currency = mCurrency.group(1).trim();
			announcement.setCurrency(currency);
		}

		Matcher mDescription = descriptionPattern.matcher(content);
		if (mDescription.find()) {
			String description = mDescription.group(1).trim();
			announcement.setDescription(description);
		}

		Matcher mCad = cadNumPattern.matcher(content);
		if (mCad.find()) {
			String cad = mCad.group(1).trim();
			announcement.setCadNum(cad);
		}

		Matcher mImg = imgPattern.matcher(content);
		if (mImg.find()) {
			String img = mImg.group(1).trim();
			announcement.setImageUrl(img);
		}

		return announcement;
	}

	public OlxAnnouncement getAnnouncement(String url) {
		url = url.substring(0, url.indexOf("#"));
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
		HttpEntity<String> entity = externalRestServiceHelper.executeExternalHtml(builder, String.class);
		String body = entity.getBody() == null ? null : entity.getBody();
		return parse(body);
	}

}
