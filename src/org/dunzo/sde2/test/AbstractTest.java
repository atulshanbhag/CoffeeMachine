package org.dunzo.sde2.test;

import org.dunzo.sde2.InputReader;
import org.json.JSONObject;

/**
 * @author Atul Shanbhag
 *
 */
public abstract class AbstractTest {
	/**
	 * @param filePath
	 * @throws InterruptedException
	 */
	public abstract void run(String filePath) throws InterruptedException;

	/**
	 * @param filePath
	 * @return
	 */
	protected JSONObject loadJSON(String filePath) {
		InputReader inputReader = new InputReader(filePath);
		inputReader.read();
		return inputReader.getJsonObj();
	}
}
