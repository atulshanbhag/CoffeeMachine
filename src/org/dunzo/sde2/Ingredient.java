package org.dunzo.sde2;

/**
 * Ingredient --- Class representing recipe ingredients.
 * 
 * @author Atul Shanbhag
 */
public class Ingredient {
	private String name;
	private int quantity;

	/**
	 * @param name
	 * @param quantity
	 */
	public Ingredient(String name, int quantity) {
		this.name = name;
		this.quantity = quantity;
	}

	/**
	 * Returns ingredient name.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns ingredient quantity.
	 * 
	 * @return
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Adds a given quantity of the ingredient and updates total quantity.
	 * 
	 * @param q
	 */
	public void addQuantity(int q) throws IllegalArgumentException {
		if (q < 0) {
			throw new IllegalArgumentException("Cannot add a negative amount of quantity to the ingredient!");
		}
		quantity += q;
	}

	/**
	 * Consumes given quantity from total quantity. Throws an error if consumed
	 * quantity is more than initial quantity.
	 * 
	 * @param q
	 */
	public void consumeQuantity(int q) throws IllegalArgumentException {
		if (q < 0) {
			throw new IllegalArgumentException("Cannot consume a negative amount of quantity from ingredient!");
		}
		if (q > quantity) {
			throw new IllegalArgumentException("You can consume at most " + quantity + " of " + name + "!");
		}
		quantity -= q;
	}

	/**
	 * Confirm equality between Ingredient objects.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		Ingredient ing = (Ingredient) obj;
		return (name.equals(ing.name) && quantity == ing.quantity);
	}

	/**
	 * Compute hashCode for Ingredient object based on it's name.
	 */
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	/**
	 * Return a string representation for Ingredient object.
	 */
	@Override
	public String toString() {
		return "INGREDIENT(" + name + ", " + quantity + ")";
	}
}
