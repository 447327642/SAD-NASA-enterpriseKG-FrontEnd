package utils;
import com.fasterxml.jackson.databind.JsonNode;
public class RESTfulContext {
	private Strategy strategy;
	
	public RESTfulContext(Strategy strategy) {
		this.strategy = strategy;
	}
	
	public JsonNode executeStrategy(String apiString, JsonNode jsonData) {
		return strategy.doOperation(apiString, jsonData);
	}
}
