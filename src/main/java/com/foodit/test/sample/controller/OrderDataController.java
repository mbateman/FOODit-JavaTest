package com.foodit.test.sample.controller;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
import com.googlecode.objectify.Key;
import com.threewks.thundr.view.string.StringView;

public class OrderDataController {

	public StringView countOrders(String restaurant) throws ParseException {
		JSONArray orders = orderData(restaurant);
		return new StringView("There are " + orders.size() + " orders for " + restaurant);
	}

	public StringView salesTotal(String restaurant) throws ParseException {
		JSONArray orders = orderData(restaurant);
		BigDecimal totalSales = BigDecimal.ZERO;
		for (Object orderObject : orders) {
			totalSales = totalSales.add(salesValue(orderObject));
		}
		
		return new StringView("The total sales for " + restaurant +" is £" + totalSales.toString());
	}
	
	public StringView mostFrequentlyOrderedMealsOnFoodIt() throws ParseException, IOException {
		List<String> restaurants = Lists.newArrayList("bbqgrill", "butlersthaicafe", "jashanexquisiteindianfood", "newchinaexpress");
		Map<Long, Integer> lineItemsToOrderFrequency = new HashMap<>();
		Map<Long, String> idsToRestaurantNames = new HashMap<>();
		for (String restaurant : restaurants) {
			mapLineItemsToOrderFrequency(restaurant, lineItemsToOrderFrequency);
			mapIdsToNames(restaurant, idsToRestaurantNames);
		}
		
		StringBuilder builder = new StringBuilder();
		Map<Long, Integer> lineItemsByOrderFrequency = sortByValues(lineItemsToOrderFrequency);
		for (Long id : lineItemsByOrderFrequency.keySet()) {
			String meal = idsToRestaurantNames.get(id);
			Integer frequency = lineItemsByOrderFrequency.get(id);
			if (meal != null)
				builder.append(meal +  ": "+ frequency + orderPlurality(frequency));
		}
		
		return new StringView(builder.toString());
	}
	
	public StringView mostFrequentlyOrderedCategoryPerRestaurant(String restaurant)	throws ParseException, IOException {
		Map<Long, Integer> lineItemsByOrderFrequency = new HashMap<>();
		Map<Long, String> categories = new HashMap<>();
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
		
		StringBuilder builder = new StringBuilder();
		frequenciesByCategory = sortByValues(frequenciesByCategory);
		for (String category : frequenciesByCategory.keySet()) {
			Integer frequency = frequenciesByCategory.get(category);
			builder.append(category +": " + frequency + orderPlurality(frequency));
		}
		
		return new StringView(builder.toString());
	}
	
	private JSONArray orderData(String restaurant) throws ParseException {
		RestaurantData restaurantLoadData = restaurantData(restaurant);
		String data = restaurantLoadData.getOrdersJson().getValue();
		return (JSONArray) new JSONParser().parse(data);
	}

	private RestaurantData restaurantData(String restaurant) {
		return ofy().load().key(Key.create(RestaurantData.class, restaurant)).now();
	}
	
	private BigDecimal salesValue(Object order) {
		return new BigDecimal((double) ((JSONObject) order)
			.get("totalValue")).setScale(2, BigDecimal.ROUND_UP);
	}

	private String orderPlurality(Integer frequency) {
		return frequency == 1 ? " order\n" : " orders\n";
	}
	
	private Map<Long, String> mapIdsToCategories(String restaurant, Map<Long, String> categories) throws ParseException, IOException {
		Map<String, JSONArray> menu = buildMenuMap(restaurant);
		for (String key : menu.keySet()) {
			JSONArray jsonArray = menu.get(key);
			//System.out.println(jsonArray);
			for (Object menuAttribute : jsonArray) {
				Long id = (Long) ((JSONObject) menuAttribute).get("id");
				String category = (String) ((JSONObject) menuAttribute)
						.get("category");
				categories.put(id, category);
			}
		}
		return categories;
	}
	
	private Map<Long, Integer> mapLineItemsToOrderFrequency(String restaurant, Map<Long,Integer> lineItemCount)
			throws ParseException, IOException {
		JSONArray orders = orderData(restaurant);
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
			//System.out.println(jsonArray);
			for (Object menuAttribute : jsonArray) {
				Long id = (Long) ((JSONObject) menuAttribute).get("id");
				String name = (String) ((JSONObject) menuAttribute)
						.get("name");
				names.put(id, name);
			}
		}
		return names;
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
	
	private Map<String, JSONArray> buildMenuMap(String restaurant) throws ParseException, IOException {
		JSONObject menuObject = (JSONObject) new JSONParser().parse(menuData(restaurant));
		JSONObject menu = (JSONObject) menuObject.get("menu");
		Map<String, JSONArray> menuMap = new HashMap<>();
		for (Object key : menu.keySet()) {
			JSONArray menuItems = (JSONArray) menu.get(key);
			menuMap.put((String) key, menuItems);
		}
		return menuMap;
	}
	
	private String menuData(String restaurant) throws ParseException {
		RestaurantData restaurantLoadData = restaurantData(restaurant);
		return restaurantLoadData.getMenuJson().getValue();
	}
	
}

