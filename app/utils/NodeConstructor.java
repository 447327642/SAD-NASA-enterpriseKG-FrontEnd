package utils;
/**
 * Created by LumiG on 4/21/16.
 */


import com.fasterxml.jackson.databind.JsonNode;
import utils.RESTfulCalls;
import utils.RESTfulCalls.ResponseType;

public class NodeConstructor {
    public static JsonNode ConstructJsonNodebyGet(String methodString){
        return RESTfulCalls.getAPI(Constants.URL_HOST
                + Constants.CMU_BACKEND_PORT
                + methodString);
    }
}
