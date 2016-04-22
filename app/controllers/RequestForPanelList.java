package controllers;

import utils.Constants;
import utils.RESTfulCalls;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Created by xingyuchen on 4/22/16.
 */
public class RequestForPanelList implements RestFulTask{

    private int id;

    public RequestForPanelList(int id) {
        this.id = id;
    }

    @Override
    public JsonNode excute() {
        JsonNode publicationPanelNode = RESTfulCalls.getAPI(Constants.URL_HOST
                + Constants.CMU_BACKEND_PORT
                + Constants.GET_PUBLICATION_PANEL_BY_PUBLICATION_ID + id);
        return publicationPanelNode;
    }
}
