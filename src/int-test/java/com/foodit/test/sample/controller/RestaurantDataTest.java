package com.foodit.test.sample.controller;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
import com.google.common.io.Resources;

public class RestaurantDataTest {

	@Test
	public void countOrdersForRestaurant() throws ParseException, IOException {
		String restaurantName = "bbqgrill";
		JSONArray orders = (JSONArray) new JSONParser().parse(ordersAsJson(restaurantName));
		assertThat(orders.size(), equalTo(86));
	}

	@Test
	public void countTotalAmountOfMoneySalesPerRestaurant() throws ParseException, IOException {
		String restaurantName = "bbqgrill";
		JSONArray orders = (JSONArray) new JSONParser().parse(ordersAsJson(restaurantName));
		BigDecimal totalSales = BigDecimal.ZERO;
		for (Object orderObject : orders) {
			totalSales = totalSales.add(salesValue(orderObject));
		}
		assertThat(totalSales.doubleValue(), equalTo(1497.10));
	}

	@Test
	public void theMostFrequentlyOrderedMealsOnFOODitPlatform()	throws ParseException, IOException {
		List<String> restaurants = Lists.newArrayList("bbqgrill", "butlersthaicafe", "jashanexquisiteindianfood", "newchinaexpress");
		Map<Long, Integer> lineItemsToOrderFrequency = new HashMap<>();
		Map<Long, String> names = new HashMap<>();
		for (String restaurant : restaurants) {
			mapLineItemsToOrderFrequency(restaurant, lineItemsToOrderFrequency);
			mapIdsToNames(restaurant, names);
		}
		
		Map<Long, Integer> lineItemsByOrderFrequency = sortByValues(lineItemsToOrderFrequency);
		assertThat(lineItemsByOrderFrequency.size(), equalTo(144));

	}
	
	@Test
	public void theMostFrequentlyOrderedMealsPerRestaurant()	throws ParseException, IOException {
		Map<Long, Integer> lineItemsByOrderFrequency = new HashMap<>();
		Map<Long, String> names = new HashMap<>();
		String restaurant = "bbqgrill";
		mapLineItemsToOrderFrequency(restaurant, lineItemsByOrderFrequency);
		mapIdsToNames(restaurant, names);	
		lineItemsByOrderFrequency = sortByValues(lineItemsByOrderFrequency);
		assertThat(lineItemsByOrderFrequency.size(), equalTo(38));
	}
	
	@Test
	public void theMostFrequentlyOrderedCategoryPerRestaurant()	throws ParseException, IOException {
		Map<Long, Integer> lineItemsByOrderFrequency = new HashMap<>();
		Map<Long, String> categories = new HashMap<>();
		String restaurant = "bbqgrill";
		mapLineItemsToOrderFrequency(restaurant, lineItemsByOrderFrequency);
		mapIdsToCategories(restaurant, categories);
		
		Map<String, Integer> frequenciesByCategory = new HashMap<>();
		for (Long id : lineItemsByOrderFrequency.keySet()) {
			String category = categories.get(id);
			Integer lineItemFrequency = lineItemsByOrderFrequency.get(id);
			if (category != null) {
				if (frequenciesByCategory.containsKey(category)) {
					Integer frequency = frequenciesByCategory.get(category);
					frequenciesByCategory.put(category, frequency + lineItemFrequency);
				} else {
					frequenciesByCategory.put(category, lineItemFrequency);
				}
			}
		}
		
		frequenciesByCategory = sortByValues(frequenciesByCategory);
		assertThat(frequenciesByCategory.size(), equalTo(10));
	}
	
	@Test
	public void searchOrdersForRestaurant() throws ParseException, IOException {
		String restaurantName = "bbqgrill";
		Map<Long, Object> orders = buildOrdersMap(restaurantName);		
		assertThat(orders.size(), equalTo(86));
	}
	
	@Test
	public void searchMenuForRestaurant() throws ParseException, IOException {
		String restaurant = "bbqgrill";
		Map<String, JSONArray> menuMap = buildMenuMap(restaurant);
		assertThat(menuMap.size(), equalTo(10));
	}
	
	private Map<Long, Integer> mapLineItemsToOrderFrequency(String restaurantName, Map<Long,Integer> lineItemCount)
			throws ParseException, IOException {
		JSONArray orders = (JSONArray) new JSONParser().parse(ordersAsJson(restaurantName));
		for (Object orderObject : orders) {
			JSONObject order = (JSONObject) orderObject;
			JSONArray lineItems = (JSONArray) order.get("lineItems");
			for (Object lineItemObject : lineItems) {
				JSONObject lineItem = (JSONObject) lineItemObject;
				Long lineItemId = (Long) lineItem.get("id");
				if (lineItemCount.containsKey(lineItemId)) {
					Integer count = (Integer) lineItemCount.get(lineItemId);
					lineItemCount.put(lineItemId, count + 1);
				} else {
					lineItemCount.put(lineItemId, 1);
				}
			}
		}
		return lineItemCount;
	}
	
	private Map<Long, String> mapIdsToNames(String restaurant, Map<Long, String> names) throws ParseException, IOException {
		Map<String, JSONArray> menu = buildMenuMap(restaurant);
		for (String key : menu.keySet()) {
			JSONArray jsonArray = menu.get(key);
			for (Object menuAttribute : jsonArray) {
				Long id = (Long) ((JSONObject) menuAttribute).get("id");
				String name = (String) ((JSONObject) menuAttribute)
						.get("name");
				names.put(id, name);
			}
		}
		return names;
	}
	
	private Map<Long, String> mapIdsToCategories(String restaurant, Map<Long, String> categories) throws ParseException, IOException {
		Map<String, JSONArray> menu = buildMenuMap(restaurant);
		for (String key : menu.keySet()) {
			JSONArray jsonArray = menu.get(key);
			for (Object menuAttribute : jsonArray) {
				Long id = (Long) ((JSONObject) menuAttribute).get("id");
				String category = (String) ((JSONObject) menuAttribute)
						.get("category");
				categories.put(id, category);
			}
		}
		return categories;
	}
	
	private <K extends Comparable<?>,V extends Comparable<? super V>> Map<K,V> sortByValues(Map<K,V> map) {
        List<Map.Entry<K,V>> entries = new LinkedList<Map.Entry<K,V>>(map.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<K,V>>() {
            @Override
            public int compare(Entry<K, V> o1, Entry<K, V> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        Map<K,V> sortedMap = new LinkedHashMap<K,V>();     
        for(Map.Entry<K,V> entry: entries){
            sortedMap.put(entry.getKey(), entry.getValue());
        }      
        return sortedMap;
    }

	private Map<Long, Object> buildOrdersMap(String restaurantName) throws ParseException, IOException {
		JSONArray orders = (JSONArray) new JSONParser().parse(ordersAsJson(restaurantName));
		Map<Long, Object> orderMap = new HashMap<>();
		for (Object orderObject : orders) {
			JSONObject order = (JSONObject) orderObject;
			orderMap.put((Long) order.get("orderId"), order);			
		}
		return orderMap;
	}

	private String ordersAsJson(String restaurantName) throws IOException {
		return readFile(String.format("orders-%s.json", restaurantName));
	}
	
	private String menuAsJson(String restaurantName) throws IOException {
		return readFile(String.format("menu-%s.json", restaurantName));
	}

	private Map<String, JSONArray> buildMenuMap(String restaurantName) throws ParseException, IOException {
		JSONObject menuObject = (JSONObject) new JSONParser().parse(menuAsJson(restaurantName));
		JSONObject menu = (JSONObject) menuObject.get("menu");
		Map<String, JSONArray> menuMap = new HashMap<>();
		for (Object key : menu.keySet()) {
			JSONArray menuItems = (JSONArray) menu.get(key);
			menuMap.put((String) key, menuItems);
		}
		return menuMap;
	}
	
	private BigDecimal salesValue(Object orderObject) {
		return new BigDecimal((double) ((JSONObject) orderObject).get("totalValue"))
			.setScale(2, BigDecimal.ROUND_UP);
	}
	
	private String readFile(String resourceName) throws IOException {
		URL url = Resources.getResource(resourceName);
		return IOUtils.toString(new InputStreamReader(url.openStream()));
	}


}
