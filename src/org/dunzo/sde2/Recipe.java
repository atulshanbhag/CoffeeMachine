package org.dunzo.sde2;

import java.util.HashMap;
import java.util.Map;

/**
 * Recipe --- Class representing beverage recipes and their required
 * ingredients.
 * 
 * @author Atul Shanbhag
 *
 */
public class Recipe implements IRecipe {
	private String name;
	private final int prepareTime = 5000; // Assumption - all recipes take 5 seconds to prepare
	private Map<String, Ingredient> ingredients;

	/**
	 * @param name
	 */
	public Recipe(String name) {
		this.name = name;
		this.ingredients = new HashMap<String, Ingredient>();
	}

	/**
	 * Return recipe name.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return recipe preparation time.
	 * 
	 * @return
	 */
	public int getPrepareTime() {
		return prepareTime;
	}

	/**
	 * Return Ingredient object for given ingredient name.
	 * 
	 * @param ingredientName
	 * @return
	 */
	protected Ingredient getIngredient(String ingredientName) {
		return ingredients.get(ingredientName);
	}

	/**
	 * Return all ingredients for the recipe.
	 * 
	 * @return
	 */
	public Map<String, Ingredient> getIngredients() {
		return ingredients;
	}

	/**
	 * Method to add ingredients to the recipe. Throws an error if unable to
	 * successfully add the ingredient.
	 */
	@Override
	public void addIngredient(Ingredient ingredient) throws IllegalArgumentException {
		if (ingredient == null) {
			throw new IllegalArgumentException("Ingredient is not defined! Cannot add to " + this);
		}
		String ingredientName = ingredient.getName();
		this.ingredients.put(ingredientName, ingredient);
	}

	/**
	 * Method to remove ingredients from the recipe. Throws an error if unable to
	 * successfully remove the ingredient.
	 */
	@Override
	public void removeIngredient(Ingredient ingredient) throws IllegalArgumentException {
		if (ingredient == null) {
			throw new IllegalArgumentException("Ingredient is not defined! Cannot remove from " + this);
		}
		// Validate if ingredient exists in the recipe before attempting to remove.
		String ingredientName = ingredient.getName();
		if (this.ingredients.containsKey(ingredient.getName())) {
			this.ingredients.remove(ingredientName);
		} else {
			throw new IllegalArgumentException(
					ingredient + " doesn't exist for " + this + "! Cannot remove from " + this);
		}
	}

	/**
	 * Confirm equality between Recipe objects.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		Recipe rec = (Recipe) obj;
		return (name.equals(rec.name) && ingredients.equals(rec.ingredients));
	}

	/**
	 * Compute hashCode for Recipe object based on it's name.
	 */
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	/**
	 * Return a string representation for Recipe object.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("RECIPE(" + name + ")").append("\n");
		if (ingredients != null) {
			for (Ingredient ing : ingredients.values()) {
				sb.append("\t\t").append(ing).append("\n");

			}
		}

		return sb.toString();
	}
}
