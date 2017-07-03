import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Pizza {
	
	/**
	 * Pizza API is a simple pizza ordering API and serves the below functionalities 
	 * 1- displaying all the restaurants available
	 * 2- find a specific restaurant using its restaurant id
	 * 3- display all the available menu for the selected restaurant
	 * 4- filter menu based on the category
	 * 5- create order and show final price
	 * 6- show order details
	 */
	
	//initializing all the required parameters
	static JSONArray AllRestaurants;
	static JSONObject Restaurant;
	static JSONArray Menu;
	static Map<Integer,Map> orderMap = new HashMap<Integer,Map>();
	static Random randomGenerator = new Random();
	static String newLine = System.getProperty("line.separator");
	
	/**
	 * main method 
	 * Read Restaurant json which contains all the available restaurants and its menu.
	 * catch and print exception if in case reading the json file.
	 */
	 public static void main(String[] args) {
		 	
		 	System.out.println("## PIZZA API ##");
		 	System.out.println(newLine);
		 	
	        JSONParser parser = new JSONParser();
	        
	        //read json file
	        try {
	        	AllRestaurants = (JSONArray) parser.parse(new FileReader("Restaurant.json"));
	        	
	        	System.out.println("All available Restaurants : ");
	        	for(Object o : AllRestaurants) {
					 JSONObject tempRestaurant = (JSONObject) o;
					 System.out.println(tempRestaurant.get("name"));					 					 
	        	}
	        	System.out.println(newLine);
	        	
	        	//get the details of restaurant with id=1
	        	//assuming user selected restaurant with id=1
	        	getRestaurantById(1);
	        	
	        	//get the menu available for the restaurant with id=1	        	
	        	displayMenu(1);
	        	
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	 
	 /**
		 * getRestaurantById method 
		 * @param  restaurantId  restaurant id for which the details needed
		 * get the details of restaurant with given id and store it in the Restaurant object
		 */
	 public static void getRestaurantById(int restaurantId) {
		 for(Object o : AllRestaurants) {
			 JSONObject tempRestaurant = (JSONObject) o;
			 if((Long)tempRestaurant.get("id")==restaurantId) {
				 Restaurant = tempRestaurant;
				 System.out.println("Restaurant details with id : "+restaurantId);
				 System.out.println("Name: "+Restaurant.get("name"));
				 System.out.println("address1: "+Restaurant.get("address1"));
				 System.out.println("address2: "+Restaurant.get("address2"));
				 System.out.println(newLine);
			 }
		 }
	 }
	 
	 /**
		 * displayMenu method 
		 * @param  restaurantId  restaurant id for which the menu is needed
		 * get the details of menu with given restaurant id and store it in the Menu object
		 */
	 public static void displayMenu(int restaurantId) {
		 
		//check whether restaurant was already selected
		 if(Restaurant == null) {
			 //if null get the restaurant details from AllRestaurant object
			 getRestaurantById(restaurantId);
		 }else {
			 System.out.println("Menu of the Restaurant with id : "+restaurantId);
			 
			 Menu = (JSONArray) Restaurant.get("menu");
			 for(Object o : Menu) {
				 JSONObject tempMenu = (JSONObject) o;
				 System.out.println("Menu name = "+tempMenu.get("name")+", id = "+tempMenu.get("menuid")+", category = "+tempMenu.get("category"));					 					 
        	}
			 System.out.println(newLine);
			 
			 //filter the menu based on the category pizza
			 //assume customer want to filter the menu based on category pizza
			 filterMenu("Pizza");
		 }
	 }
	 
	 /**
		 * filterMenu method 
		 * @param  category  category based on which the menu needs to be filtered
		 * filter the menu based on given category and store it in filteredMenuMap
		 */
	 public static void filterMenu(String category) {
		 //map to store filtered menu
		 System.out.println("Filtered menu list based on selected category : "+category);
		 
		 Map<String, Object> filteredMenuMap = new HashMap<String, Object>();
		 //iterate through menu
		 for(Object o : Menu) {
			 JSONObject tempMenu = (JSONObject) o;
			 if(tempMenu.get("category").toString().equals(category)) {
				 //store in filteredMenuMap
				 filteredMenuMap.put(tempMenu.get("name").toString(), o);
				 System.out.println(tempMenu.get("name").toString());
			 }
		 }
		 System.out.println(newLine);
		 
		 //create the order based on selected menu and quantities 
		 createOrder(filteredMenuMap);
	 }
	 
	 /**
		 * createOrder method 
		 * @param  filteredMenuMap  filtered menu items based on the category
		 * get the customer selected items and its respective quantities and calculate the price
		 * Assumption : Lets assume user selected Hawaii 2 quantities and Vesuvius 1 quantities
		 */
	 public static void createOrder(Map<String, Object> filteredMenuMap) {
		 int price = 0;
		 //Map to store selected items and its quantities
		 Map<String, Integer> selectedItemsMap = new HashMap<String, Integer>();
		 //Add assumed values in map
		 selectedItemsMap.put("Hawaii", 2);
		 selectedItemsMap.put("Vesuvius", 1);
		  
		 System.out.println("Find below the final order with price : ");
		 
		 for (Map.Entry<String, Integer> entry : selectedItemsMap.entrySet())
		 {			 
			if(filteredMenuMap.containsKey(entry.getKey())) {
				JSONObject obj = (JSONObject) filteredMenuMap.get(entry.getKey());
				Long itemPrice = (Long) obj.get("price");
				System.out.println(entry.getKey()+" = "+ entry.getValue() +" , price per quantity = "+itemPrice);
				price = (int) (price+ (itemPrice * entry.getValue()));				
			}
		 }
		 
		 System.out.println("TOTAL AMOUNT TO PAY : "+price+"SEK");
		 
		 //generate random number to assign it to orderId value
		 //set range as 1-100 for demo purpose
		 int orderid = randomGenerator.nextInt(100);
		 System.out.println("Your Order number : "+orderid);
	 }
	 
	 
}
