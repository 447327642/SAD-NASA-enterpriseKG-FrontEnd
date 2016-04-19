package utils;

import com.fasterxml.jackson.databind.JsonNode;

public class FormatCheck {
    public static Boolean check(JsonNode node){
        if (node == null || node.has("error")
                || !node.isArray())
            return true;
        return false;
    }
}
