package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Created by xingyuchen on 4/22/16.
 */
public interface RestFulTask {
    public JsonNode excute();
}
