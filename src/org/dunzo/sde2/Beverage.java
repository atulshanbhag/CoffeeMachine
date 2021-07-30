package org.dunzo.sde2;

/**
 * Beverage --- Class representing beverages served by the Coffee Machine.
 * 
 * @author Atul Shanbhag
 *
 */
/**
 * @author Atul Shanbhag
 *
 */
public class Beverage {
	private String name;
	private Recipe recipe;

	/**
	 * @param name
	 * @param recipe
	 */
	public Beverage(String name, Recipe recipe) {
		this.name = name;
		this.recipe = recipe;
	}

	/**
	 * Returns name of the beverage.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the recipe object for the beverage. Throws an exception if recipe is
	 * not defined for whatever reason.
	 * 
	 * @return
	 * @throws IllegalArgumentException
	 */
	public Recipe getRecipe() throws IllegalArgumentException {
		if (recipe == null) {
			throw new IllegalArgumentException(this + " doesn't have any recipe!");
		}
		return recipe;
	}

	/**
	 * Returns time to prepare the beverage given the recipe.
	 * 
	 * @return
	 */
	public int getPrepareTime() {
		return recipe.getPrepareTime();
	}

	/**
	 * Confirm equality between Beverage objects
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		Beverage bev = (Beverage) obj;
		return (name.equals(bev.name) && recipe.equals(bev.getRecipe()));
	}

	/**
	 * Compute hashCode for Beverage object based on it's name.
	 */
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	/**
	 * Return a string representation for Beverage object.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("BEVERAGE(" + name + ")").append("\n");
		if (recipe != null) {
			sb.append("\t").append(recipe).append("\n");
		}

		return sb.toString();
	}
}
