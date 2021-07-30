package org.dunzo.sde2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * InputReader --- Reads metadata for Coffee Machine from an input JSON file and
 * returns a parsed JSON object to simulate a Coffee Machine.
 * 
 * @author Atul Shanbhag
 *
 */
public class InputReader {
	private String filePath;
	private JSONObject jsonObj;

	/**
	 * @param filePath
	 */
	public InputReader(String filePath) {
		this.filePath = filePath;
		this.jsonObj = null;
	}

	/**
	 * Returns JSON object.
	 * 
	 * @return
	 */
	public JSONObject getJsonObj() {
		return jsonObj;
	}

	/**
	 * Returns a string representation of the input stream loaded from an input JSON
	 * file.
	 * 
	 * @param inputStream
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private String readFromInputStream(InputStream inputStream) throws FileNotFoundException, IOException {
		// Use StringBuilder to build resulting JSON string
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
		}
		return sb.toString();
	}

	/**
	 * Converts JSON string to JSONObject.
	 * 
	 * @param jsonString
	 * @return
	 * @throws JSONException
	 */
	private JSONObject parseJSONString(String jsonString) throws JSONException {
		return this.jsonObj = new JSONObject(jsonString);
	}

	/**
	 * Reads input JSON file and converts to JSON object for further procedures.
	 */
	public void read() {
		InputStream inputStream = null;
		try {
			File file = new File(filePath);
			inputStream = new FileInputStream(file);
			String jsonString = this.readFromInputStream(inputStream);
			this.jsonObj = this.parseJSONString(jsonString);
		} catch (FileNotFoundException e) {
			System.out.println("Input JSON file not found!");
			e.printStackTrace();
		} catch (JSONException e) {
			System.out.println("Error while parsing input JSON file!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error while loading input JSON file!");
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
