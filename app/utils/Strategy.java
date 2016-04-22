package utils;
import com.fasterxml.jackson.databind.JsonNode;
public interface Strategy {
	public JsonNode doOperation(String apiString, JsonNode jsonData);
}
