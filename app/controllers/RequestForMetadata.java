package controllers;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import utils.RESTfulContext;
import utils.RESTfulGet;
import utils.RESTfulPost;
import utils.RESTfulCalls;
import utils.Constants;


/**
 * Created by xingyuchen on 4/22/16.
 */
public class RequestForMetadata implements RestFulTask{
    private int id;

    public RequestForMetadata(int id) {
        this.id = id;
    }

    @Override
    public JsonNode excute() {
        JsonNode publicationMetadataNode = RESTfulCalls.getAPI(Constants.URL_HOST
                + Constants.CMU_BACKEND_PORT
                + Constants.GET_PUBLICATION_METADATA_BY_PUBLICATION_ID + id);
        return publicationMetadataNode;
    }
}
