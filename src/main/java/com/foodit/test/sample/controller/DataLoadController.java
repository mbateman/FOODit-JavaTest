package com.foodit.test.sample.controller;

import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.googlecode.objectify.Key;
import com.threewks.thundr.http.ContentType;
import com.threewks.thundr.http.HttpSupport;
import com.threewks.thundr.logger.Logger;
import com.threewks.thundr.view.jsp.JspView;
import com.threewks.thundr.view.string.StringView;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class DataLoadController {

	public JspView instructions() {
		return new JspView("instructions.jsp");
	}

	public StringView load() {
		Logger.info("Loading data");
		List<String> restaurants = Lists.newArrayList("bbqgrill", "butlersthaicafe", "jashanexquisiteindianfood", "newchinaexpress");
		List<RestaurantData> restaurantData = Lists.newArrayList();
		for (String restaurant : restaurants) {
			restaurantData.add(loadData(restaurant));
		}
		ofy().save().entities(restaurantData);
		return new StringView("Data loaded.");
	}

	private RestaurantData loadData(String restaurantName) {
		String orders = readFile(String.format("orders-%s.json", restaurantName));
		String menu = readFile(String.format("menu-%s.json", restaurantName));
		RestaurantData restaurantLoadData = new RestaurantData(restaurantName, menu, orders);
		return restaurantLoadData;
	}

	private String readFile(String resourceName) {
		URL url = Resources.getResource(resourceName);
		try {
			return IOUtils.toString(new InputStreamReader(url.openStream()));
		} catch (IOException e) {
			Logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public void viewData(String restaurant, HttpServletResponse response) throws IOException {
		response.addHeader(HttpSupport.Header.ContentType, ContentType.ApplicationJson.value());
		RestaurantData restaurantLoadData = ofy().load().key(Key.create(RestaurantData.class, restaurant)).now();
		String data = restaurantLoadData.viewData();
		response.getWriter().write(data);
		response.setContentLength(data.getBytes().length);
	}

}
