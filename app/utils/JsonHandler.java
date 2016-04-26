package utils;

import com.fasterxml.jackson.databind.JsonNode;
/**
 * Created by xingyuchen on 4/26/16.
 */
public class JsonHandler {
    private JsonParser jsonParser;
    private JsonNode jsonNode;

    public JsonHandler(JsonParser jsonParser, JsonNode jsonNode) {
        this.jsonParser = jsonParser;
        this.jsonNode = jsonNode;
    }

    public Object parseToObject() {
        return jsonParser.toObject(jsonNode);
    }
}
