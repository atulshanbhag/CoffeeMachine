package org.dunzo.sde2.test;

import java.util.concurrent.TimeUnit;

import org.dunzo.sde2.CoffeeMachine;
import org.json.JSONObject;

/**
 * @author Atul Shanbhag
 *
 */
public class Test3 extends AbstractTest {

	/**
	 *
	 */
	@Override
	public void run(String filePath) throws InterruptedException {
		JSONObject jsonObj = loadJSON(filePath);
		CoffeeMachine machine = new CoffeeMachine("Chai Point");
		machine.initializeFromJSON(jsonObj);

		machine.start();
		System.out.println();

		machine.showDetails();
		System.out.println();

		machine.serveBeverage(1, "hot_coffee");
		System.out.println();
		TimeUnit.MILLISECONDS.sleep(100);

		machine.serveBeverage(2, "black_tea");
		System.out.println();
		TimeUnit.MILLISECONDS.sleep(100);

		machine.serveBeverage(3, "green_tea");
		System.out.println();
		TimeUnit.MILLISECONDS.sleep(100);

		machine.serveBeverage(4, "hot_tea");
		System.out.println();
		TimeUnit.MILLISECONDS.sleep(100);

		machine.showLowQuantityIngredients();
		System.out.println();

		machine.close();
		System.out.println();
	}

}
