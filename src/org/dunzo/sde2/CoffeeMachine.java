package org.dunzo.sde2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * CoffeeMachine --- Class representing a basic Coffee Machine that can add or
 * remove ingredients and prepare different types of beverages based on the
 * ingredients in stock, at one of it's n outlets.
 * 
 * @author Atul Shanbhag
 *
 */
public class CoffeeMachine extends ICoffeeMachine {
	// Disable serve and prepare when machine is not intialized using the metadata.
	private boolean initialized;

	// Uses an executor service to run multiple coffee prepare and serve processes
	// simultaneously.
	private ExecutorService executor;

	/**
	 * @param description
	 */
	public CoffeeMachine(String description) {
		super(description);
		this.initialized = false;
		this.lock = new ReentrantLock();
	}

	/**
	 * Check if the executors are running or not.
	 * 
	 * @return
	 */
	private boolean isRunning() {
		return (executor != null && !executor.isTerminated());
	}

	/**
	 * Turn the machine on if it isn't turned on or running already.
	 */
	@Override
	public void start() {
		if (isRunning()) {
			throw new RuntimeException("Coffee Machine is already turned on and running!");
		}
		executor = Executors.newFixedThreadPool(2 * nOutlets);
		System.out.println("Turned on Coffee Machine!");
	}

	/**
	 * Turn the machine off if it isn't turned off or terminated already.
	 */
	@Override
	public void close() {
		if (!isRunning()) {
			throw new RuntimeException("Coffee Machine is already turned off!");
		}
		executor.shutdown();
		try {
			if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
				executor.shutdownNow();
				if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
					throw new RuntimeException("Unable to turn off Coffee Machine after 2 minutes!");
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (!executor.isTerminated()) {
				executor.shutdownNow();
			}
		}
		System.out.println("Turned off Coffee Machine!");
	}

	/**
	 * CoffeeMachineJSONLoader --- Helper nested class to parse JSON objects and
	 * initialize Coffee Machine. Throws appropriate exceptions if any step is
	 * failed.
	 * 
	 * @author Atul Shanbhag
	 *
	 */
	private static class CoffeeMachineJSONLoader {
		/**
		 * Initialize the outlets in Coffee Machine.
		 * 
		 * @param machine
		 * @param outlets
		 * @throws IllegalArgumentException
		 * @throws JSONException
		 */
		private static void initializeOutlets(CoffeeMachine machine, JSONObject outlets)
				throws IllegalArgumentException, JSONException {
			if (outlets == null) {
				throw new IllegalArgumentException(
						"Error in initializing outlets for Coffee Machine with given JSON object!");
			}

			machine.nOutlets = outlets.getInt("count_n");
			machine.outlets = new HashMap<Integer, Outlet>();
			for (int i = 0; i < machine.nOutlets; i++) {
				Outlet outlet = new Outlet();
				machine.outlets.put(outlet.getId(), outlet);
			}
		}

		/**
		 * Initialize the ingredients stocks in Coffee Machine.
		 * 
		 * @param machine
		 * @param totalItems
		 * @throws IllegalArgumentException
		 * @throws JSONException
		 */
		private static void initializeIngredients(CoffeeMachine machine, JSONObject totalItems)
				throws IllegalArgumentException, JSONException {
			if (totalItems == null) {
				throw new IllegalArgumentException(
						"Error in initializing ingredients for Coffee Machine with given JSON object!");
			}

			machine.ingredients = new HashMap<String, Ingredient>();
			Iterator<String> itemsIterator = totalItems.keys();
			while (itemsIterator.hasNext()) {
				String itemName = itemsIterator.next();
				int itemQuantity = totalItems.getInt(itemName);
				Ingredient ingredient = new Ingredient(itemName, itemQuantity);
				machine.ingredients.put(itemName, ingredient);
			}
		}

		/**
		 * Initialize the recipe object for given Beverage to be added to the Coffee
		 * Machine.
		 * 
		 * @param machine
		 * @param beverageName
		 * @param recipeObj
		 * @return
		 * @throws IllegalArgumentException
		 * @throws JSONException
		 */
		private static Recipe getBeverageRecipe(CoffeeMachine machine, String beverageName, JSONObject recipeObj)
				throws IllegalArgumentException, JSONException {
			if (recipeObj == null) {
				throw new IllegalArgumentException(
						"Error in initializing beverages for Coffee Machine with given recipe JSON object!");
			}

			Recipe recipe = new Recipe(beverageName);

			Iterator<String> recipeIterator = recipeObj.keys();
			while (recipeIterator.hasNext()) {
				String ingredientName = recipeIterator.next();
				int ingredientQuantity = recipeObj.getInt(ingredientName);

				Ingredient ingredient = new Ingredient(ingredientName, ingredientQuantity);
				recipe.addIngredient(ingredient);
			}

			return recipe;
		}

		/**
		 * Initialize the beverages supported by the Coffee Machine.
		 * 
		 * @param machine
		 * @param beverages
		 * @throws IllegalArgumentException
		 * @throws JSONException
		 */
		private static void initializeBeverages(CoffeeMachine machine, JSONObject beverages)
				throws IllegalArgumentException, JSONException {
			if (beverages == null) {
				throw new IllegalArgumentException(
						"Error in initializing beverages for Coffee Machine with given JSON object!");
			}

			machine.beverages = new HashMap<String, Beverage>();
			Iterator<String> beveragesIterator = beverages.keys();
			while (beveragesIterator.hasNext()) {
				String beverageName = beveragesIterator.next();
				JSONObject recipeObj = beverages.getJSONObject(beverageName);

				Recipe recipe = getBeverageRecipe(machine, beverageName, recipeObj);

				Beverage beverage = new Beverage(beverageName, recipe);
				machine.beverages.put(beverageName, beverage);
			}
		}

		/**
		 * Initialize the Coffee Machine based on it's metadata parsed from a JSON
		 * object.
		 * 
		 * @param machine
		 * @param jsonObj
		 * @throws IllegalArgumentException
		 * @throws JSONException
		 */
		public static void initializeCoffeeMachine(CoffeeMachine machine, JSONObject jsonObj)
				throws IllegalArgumentException, JSONException {
			if (machine == null) {
				throw new IllegalArgumentException("Coffee Machine is not defined! Aborting initialization!");
			}
			if (jsonObj == null) {
				throw new IllegalArgumentException("Error in initializing Coffee Machine with given JSON object!");
			}

			JSONObject data = jsonObj.getJSONObject("machine");
			JSONObject outlets = data.getJSONObject("outlets");
			initializeOutlets(machine, outlets);

			JSONObject totalItemsQuantity = data.getJSONObject("total_items_quantity");
			initializeIngredients(machine, totalItemsQuantity);

			JSONObject beverages = data.getJSONObject("beverages");
			initializeBeverages(machine, beverages);
		}
	}

	/**
	 * @param jsonObj
	 * @throws IllegalArgumentException
	 * @throws JSONException
	 */
	public void initializeFromJSON(JSONObject jsonObj) throws IllegalArgumentException, JSONException {
		if (jsonObj == null) {
			throw new IllegalArgumentException("JSON object is not defined! Cannot initialize Coffee Machine!");
		}

		CoffeeMachineJSONLoader.initializeCoffeeMachine(this, jsonObj);
		this.initialized = true;
	}

	/**
	 * Display the supported beverages and their recipes for the machine.
	 */
	@Override
	public void showDetails() {
		if (!initialized) {
			throw new RuntimeException("This Coffee Machine was not initialized! Cannot display details yet!");
		}

		System.out.println("Name = " + description + "\n");

		System.out.println("Outlets = " + nOutlets);
		for (Outlet out : outlets.values()) {
			System.out.println(out);
		}
		System.out.println("\n");

		System.out.println("Ingredients = " + ingredients.size());
		for (Ingredient ing : ingredients.values()) {
			System.out.println(ing);
		}
		System.out.println("\n");

		System.out.println("Beverages = " + beverages.size());
		for (Beverage bev : beverages.values()) {
			System.out.println(bev);
		}
		System.out.println("\n");
	}

	/**
	 * Adds a quantity of the ingredient to the stock.
	 */
	@Override
	public void addIngredient(Ingredient ingredient, int quantity)
			throws IllegalArgumentException, InterruptedException {
		if (ingredient == null) {
			throw new IllegalArgumentException("Ingredient is not defined! Cannot add quantity to this ingredient!");
		}
		ingredient.addQuantity(quantity);
		System.out.println("Added " + quantity + " to " + ingredient.getName() + "!");
	}

	/**
	 * Adds a quantity of the ingredient to the stock given ingredient name.
	 * 
	 * @throws InterruptedException
	 */
	@Override
	public void addIngredient(String ingredientName, int quantity) throws InterruptedException {
		Ingredient ingredient = getIngredient(ingredientName);
		addIngredient(ingredient, quantity);
	}

	/**
	 * Validate that all the ingredients required for the beverage are in stock and
	 * available. Throw an exception if there is any discrepancy.
	 */
	@Override
	public void validateIngredientsAvailable(Beverage beverage, Outlet outlet) throws IllegalArgumentException {
		Recipe bevRecipe = beverage.getRecipe();
		Map<String, Ingredient> bevIngredients = bevRecipe.getIngredients();
		List<String> unavailableIngredients = new ArrayList<String>();
		for (String ingredientName : bevIngredients.keySet()) {
			Ingredient stockIngredient = getIngredient(ingredientName);
			// For every ingredient required for beverage, check if it's available in stock.
			if (stockIngredient == null) {
				unavailableIngredients.add(ingredientName);
			}
		}
		if (!unavailableIngredients.isEmpty()) {
			throw new IllegalArgumentException(
					beverage.getName() + " cannot be prepared at " + outlet + " because ingredient(s) ("
							+ String.join(", ", unavailableIngredients) + ") is(are) not available!");
		}
	}

	/**
	 * Validate that all the ingredients required for the beverage are in stock and
	 * sufficient. Throw an exception if there is any discrepancy.
	 */
	@Override
	public void validateIngredientsSufficient(Beverage beverage, Outlet outlet) throws IllegalArgumentException {
		Recipe bevRecipe = beverage.getRecipe();
		Map<String, Ingredient> bevIngredients = bevRecipe.getIngredients();
		List<String> insufficientIngredients = new ArrayList<String>();
		for (String ingredientName : bevIngredients.keySet()) {
			Ingredient bevIngredient = bevIngredients.get(ingredientName);
			Ingredient stockIngredient = getIngredient(ingredientName);

			int bevIngredientQuantity = bevIngredient.getQuantity();
			int stockQuantity = stockIngredient.getQuantity();

			// For every ingredient required for beverage, compare it's quantity with stock
			// quantity.
			if (bevIngredientQuantity > stockQuantity) {
				insufficientIngredients.add(ingredientName);
			}
		}
		if (!insufficientIngredients.isEmpty()) {
			throw new IllegalArgumentException(
					beverage.getName() + " cannot be prepared at " + outlet + " because ingredient(s) ("
							+ String.join(", ", insufficientIngredients) + ") is(are) not sufficient!");
		}
	}

	/**
	 * Picks quantities of beverage recipe ingredients from stock ingredient
	 * quantities.
	 * 
	 * @param beverage
	 */
	private void mixIngredients(Outlet outlet, Beverage beverage) {
		Recipe bevRecipe = beverage.getRecipe();
		Map<String, Ingredient> bevIngredients = bevRecipe.getIngredients();
		for (String ingredientName : bevIngredients.keySet()) {
			Ingredient bevIngredient = bevIngredients.get(ingredientName);
			Ingredient stockIngredient = getIngredient(ingredientName);

			int bevIngredientQuantity = bevIngredient.getQuantity();
			stockIngredient.consumeQuantity(bevIngredientQuantity);
		}
	}

	/**
	 * After validations, run this method to assign an outlet to the beverage and
	 * prepare and serve it.
	 */
	@Override
	protected void prepareBeverage(Outlet outlet, Beverage beverage) {
		if (!isRunning()) {
			throw new RuntimeException("Coffee Machine is not turned on! Cannot prepare " + beverage.getName() + "!");
		}
		// Prepare beverage at outlet and run this process in a separate thread.
		executor.submit(() -> {
			try {
				mixIngredients(outlet, beverage);
				outlet.prepareBeverage(beverage, ingredients);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Method to serve beverage at a particular outlet. Runs validations for
	 * beverages and outlets, and the required ingredients for the beverage recipes.
	 */
	@Override
	public void serveBeverage(int outletNo, String beverageName) throws IllegalArgumentException {
		if (!initialized) {
			throw new RuntimeException("Coffee Machine was not setup! Cannot serve any beverages yet!");
		}

		if (!isRunning()) {
			throw new RuntimeException("Coffee Machine was not setup! Cannot serve any beverages yet!");
		}

		if (outletNo > nOutlets) {
			throw new IllegalArgumentException(
					"Choose a valid outlet among the " + nOutlets + " available for this Coffee Machine!");
		}

		if (!beverages.containsKey(beverageName)) {
			throw new IllegalArgumentException(
					"This beverage is not being served by the Coffee Machine! Enter a valid beverage!");
		}

		Outlet outlet = getOutlet(outletNo);
		Beverage beverage = getBeverage(beverageName);

		// Acquire a lock before preparing beverages.
		if (lock.tryLock()) {
			try {
				// Validate if all ingredients in recipe are available in stock.
				try {
					validateIngredientsAvailable(beverage, outlet);
				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
					return;
				}

				// Validate if all ingredients in recipe are sufficient in stock.
				try {
					validateIngredientsSufficient(beverage, outlet);
				} catch (Exception e) {
					System.out.println(e.getMessage());
					return;
				}

				// Prepare beverage at given outlet if validations were successful.
				prepareBeverage(outlet, beverage);
			} finally {
				lock.unlock();
			}
		}

	}

	/**
	 * Displays all ingredients running low on quantity.
	 */
	@Override
	public void showLowQuantityIngredients() {
		// Display low quantities when not serving any beverages.
		if (lock.tryLock()) {
			try {
				List<Ingredient> lowQuantityIngredients = new ArrayList<Ingredient>();
				for (Ingredient ingredient : ingredients.values()) {
					int quantity = ingredient.getQuantity();
					if (quantity < MINIMUM_INGREDIENT_QUANTITY) {
						lowQuantityIngredients.add(ingredient);
					}
				}
				if (lowQuantityIngredients.isEmpty()) {
					System.out.println("Coffee Machine has enough quantity of each ingredient!");
				} else {
					System.out.println("Following ingredients are low in quantity");
					for (Ingredient ingredient : lowQuantityIngredients) {
						System.out.println("\t" + ingredient);
					}
				}
			} finally {
				lock.unlock();
			}
		}
	}
}
