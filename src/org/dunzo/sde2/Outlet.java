package org.dunzo.sde2;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Outlet --- Class representing an outlet in the Coffee Machine. Handles mixing
 * recipe ingredients and preparing beverage assigned to it.
 * 
 * @author Atul Shanbhag
 *
 */
public class Outlet {
	private int id;

	// Keep a lock on the outlet while preparing a beverage.
	private ReentrantLock lock;

	// Generate outlet id using a increasing counter.
	private static int idCounter = 0;

	/**
	 * 
	 */
	public Outlet() {
		idCounter++;
		this.id = idCounter;
		this.lock = new ReentrantLock();
	}

	/**
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Prepares beverage at outlet given all the ingredients in stock and update
	 * accordingly if successful. Runs validations on outlet if already being in
	 * use. Will wait for few seconds to free the outlet before next beverage can be
	 * prepared else throw an error.
	 * 
	 * @param beverage
	 * @param ingredients
	 * @throws IllegalArgumentException
	 * @throws InterruptedException
	 */
	public void prepareBeverage(Beverage beverage, Map<String, Ingredient> ingredients)
			throws IllegalArgumentException, InterruptedException {
		if (beverage == null) {
			throw new IllegalArgumentException("Beverage is not defined! Cannot prepare beverage!");
		}
		// Show a warning message if outlet already occupied.
		String beverageName = beverage.getName();
		if (lock.isLocked()) {
			System.out.println("Somebody already preparing a beverage at " + this + ".");
		}
		// Try to acquire lock on outlet for few seconds before failing out preparing
		// the beverage on this outlet.
		int prepareTime = beverage.getPrepareTime();
		if (lock.tryLock(prepareTime * 2, TimeUnit.MILLISECONDS)) {
			// Once lock is acquired, mix the ingredients and wait till beverage is prepared
			// and served.
			try {
				System.out.println("Preparing " + beverageName + " at " + this + " (ETA = "
						+ (double) (prepareTime / 1000) + " seconds).");
				TimeUnit.MILLISECONDS.sleep(prepareTime);
			} finally {
				// Unlock the lock on the outlet so other beverages can be prepared.
				lock.unlock();
				System.out.println("Prepared " + beverageName + " at " + this + ".");
			}
		} else {
			System.out
					.println("Cannot prepare " + beverageName + " in " + this + " right now. Please try again later.");
		}
	}

	/**
	 * Confirm equality between Outlet objects.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		Outlet out = (Outlet) obj;
		return (id == out.id);
	}

	/**
	 * Compute hashCode for Outlet object based on it's id.
	 */
	@Override
	public int hashCode() {
		return Integer.valueOf(id).hashCode();
	}

	/**
	 * Return a string representation for Outlet object.
	 */
	@Override
	public String toString() {
		return "OUTLET(" + id + ")";
	}
}
