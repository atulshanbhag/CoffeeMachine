package org.dunzo.sde2;

import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ICoffeeMachine --- Defines an abstraction over the CoffeeMachine class.
 * Contains all the necessary features in a Coffee Machine to be implemented by
 * it's variants.
 * 
 * @author Atul Shanbhag
 *
 */
/**
 * @author Atul Shanbhag
 *
 */
public abstract class ICoffeeMachine {
	protected int nOutlets;
	protected String description;

	// Stores all the outlets keyed by it's outlet no. as id
	protected Map<Integer, Outlet> outlets;

	// Stores all beverages that the machine can prepare and serve
	protected Map<String, Beverage> beverages;

	// Stores all ingredients that the machine has in stock
	protected Map<String, Ingredient> ingredients;

	// Defines a quantity to trigger running low indicator for any ingredient
	protected final static int MINIMUM_INGREDIENT_QUANTITY = 50;

	// Lock stock ingredients before updating or querying them
	protected ReentrantLock lock;

	/**
	 * @param description
	 */
	public ICoffeeMachine(String description) {
		this.description = description;
	}

	/**
	 * Returns no. of outlets in the machine.
	 * 
	 * @return
	 */
	public int getnOutlets() {
		return nOutlets;
	}

	/**
	 * Returns the description for the machine.
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the Outlet for the given outlet no.
	 * 
	 * @param outletNo
	 * @return
	 */
	protected Outlet getOutlet(int outletNo) {
		return outlets.get(outletNo);
	}

	/**
	 * Returns the Beverage for the given beverage name.
	 * 
	 * @param beverageName
	 * @return
	 */
	protected Beverage getBeverage(String beverageName) {
		return beverages.get(beverageName);
	}

	/**
	 * Returns the Ingredient for the given ingredient name.
	 * 
	 * @param ingredientName
	 * @return
	 */
	protected Ingredient getIngredient(String ingredientName) {
		return ingredients.get(ingredientName);
	}

	/**
	 * Turn the machine on if it isn't turned on or running already.
	 */
	public abstract void start();

	/**
	 * Turn the machine off if it isn't turned off or terminated already.
	 */
	public abstract void close();

	/**
	 * Display the supported beverages and their recipes for the machine.
	 */
	public abstract void showDetails();

	/**
	 * Adds a new ingredient to the stock if not added before.
	 * 
	 * @param name
	 * @param quantity
	 * @throws IllegalArgumentException
	 */
	public abstract void addIngredient(String name, int quantity) throws IllegalArgumentException;

	/**
	 * Adds a quantity of the ingredient to the stock.
	 * 
	 * @param ingredient
	 * @param quantity
	 * @throws IllegalArgumentException
	 * @throws InterruptedException
	 */
	public abstract void addIngredientQuantity(Ingredient ingredient, int quantity)
			throws IllegalArgumentException, InterruptedException;

	/**
	 * Adds a quantity of the ingredient to the stock given ingredient name.
	 * 
	 * @param ingredientName
	 * @param quantity
	 * @throws InterruptedException
	 */
	public abstract void addIngredientQuantity(String ingredientName, int quantity) throws InterruptedException;

	/**
	 * Adds a new beverage to the Coffee Machine if not supported before.
	 * 
	 * @param beverage
	 * @throws IllegalArgumentException
	 */
	public abstract void addBeverage(Beverage beverage) throws IllegalArgumentException;

	/**
	 * Validate that all the ingredients required for the beverage are in stock and
	 * available. Throw an exception if there is any discrepancy.
	 * 
	 * @param beverage
	 * @param outlet
	 * @throws IllegalArgumentException
	 */
	protected abstract void validateIngredientsAvailable(Beverage beverage, Outlet outlet)
			throws IllegalArgumentException;

	/**
	 * Validate that all the ingredients required for the beverage are in stock and
	 * sufficient. Throw an exception if there is any discrepancy.
	 * 
	 * @param beverage
	 * @param outlet
	 * @throws IllegalArgumentException
	 */
	protected abstract void validateIngredientsSufficient(Beverage beverage, Outlet outlet)
			throws IllegalArgumentException;

	/**
	 * After validations, run this method to assign an outlet to the beverage and
	 * prepare and serve it.
	 * 
	 * @param outlet
	 * @param beverage
	 */
	protected abstract void prepareBeverage(Outlet outlet, Beverage beverage);

	/**
	 * Method to serve beverage at a particular outlet. Runs validations for
	 * beverages and outlets, and the required ingredients for the beverage recipes.
	 * 
	 * @param outletNo
	 * @param beverageName
	 * @throws IllegalArgumentException
	 */
	public abstract void serveBeverage(int outletNo, String beverageName) throws IllegalArgumentException;

	/**
	 * Displays all ingredients running low on quantity.
	 */
	public abstract void showLowQuantityIngredients();
}
