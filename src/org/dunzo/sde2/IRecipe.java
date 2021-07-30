package org.dunzo.sde2;

/**
 * IRecipe --- Interface for Recipes.
 * 
 * @author Atul Shanbhag
 *
 */
public interface IRecipe {
	/**
	 * Method to add ingredients to the recipe. Throws an error if unable to
	 * successfully add the ingredient.
	 * 
	 * @param ingredient
	 * @throws IllegalArgumentException
	 */
	public void addIngredient(Ingredient ingredient) throws IllegalArgumentException;

	/**
	 * Method to remove ingredients from the recipe. Throws an error if unable to
	 * successfully remove the ingredient.
	 * 
	 * @param ingredient
	 * @throws IllegalArgumentException
	 */
	public void removeIngredient(Ingredient ingredient) throws IllegalArgumentException;
}
