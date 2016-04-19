/**
 * @author xingyuchen
 * Created on Apr 19, 2016
 */
package utils;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author xingyuchen
 *
 */
public interface JsonParser {
	
	public JsonNode toJson(Object object);

    public Object toObject(JsonNode json);
}
