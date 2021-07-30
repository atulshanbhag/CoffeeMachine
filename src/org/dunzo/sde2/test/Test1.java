package org.dunzo.sde2.test;

import java.util.concurrent.TimeUnit;

import org.dunzo.sde2.CoffeeMachine;
import org.json.JSONObject;

/**
 * @author Atul Shanbhag
 *
 */
public class Test1 extends AbstractTest {

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

		machine.serveBeverage(1, "hot_tea");
		TimeUnit.MILLISECONDS.sleep(100);
		System.out.println();

		// This process will wait for a little while because outlet#1 is occupied
		machine.serveBeverage(1, "hot_coffee");
		TimeUnit.MILLISECONDS.sleep(100);
		System.out.println();

		// This hot coffee cannot be prepared because an earlier request will consume ingredients
		machine.serveBeverage(2, "hot_coffee");
		TimeUnit.MILLISECONDS.sleep(100);
		System.out.println();

		machine.serveBeverage(3, "green_tea");
		TimeUnit.MILLISECONDS.sleep(100);
		System.out.println();

		machine.serveBeverage(4, "black_tea");
		TimeUnit.MILLISECONDS.sleep(100);
		System.out.println();
		
		// Will fail because only 4 outlets available
		try {
			machine.serveBeverage(5, "hot_coffee");
			TimeUnit.MILLISECONDS.sleep(100);
			System.out.println();
		} catch(Exception e) {
			System.out.println(e.getMessage() + "\n");
		}

		machine.showLowQuantityIngredients();
		System.out.println();
		
		machine.showDetails();
		System.out.println();

		machine.close();
		System.out.println();
	}

}
