package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Author;
import play.mvc.Result;
import utils.Constants;
import utils.RESTfulCalls;
import views.html.registerAPublication;

import java.util.ArrayList;
import java.util.List;

import static play.mvc.Results.ok;

/**
 * Created by Holysoul on 4/19/16.
 */
public class PublicationRegistration implements PublicationControllerInterface {
    @Override
    public Result execute() {
        String result = "fail";

        List<Author> authorsList = new ArrayList<Author>();
        JsonNode authorsNode = RESTfulCalls.getAPI(Constants.URL_HOST
                + Constants.CMU_BACKEND_PORT
                + Constants.GET_ALL_AUTHORS);

        // if no value is returned or error or is not json array
        if (authorsNode == null || authorsNode.has("error")
                || !authorsNode.isArray()) {
            return ok(registerAPublication.render(authorsList));
        }

        // parse the json string into object
        for (int i = 0; i < authorsNode.size(); i++) {
            JsonNode json = authorsNode.path(i);
            Author oneAuthor = new Author();
            oneAuthor.setId(json.findPath("id").asLong());
            oneAuthor.setAuthorName(json.findPath("authorName").asText());
            oneAuthor.setInstitute(json.findPath("institute").asText());
            authorsList.add(oneAuthor);
        }
        return ok(registerAPublication.render(authorsList));
    }
}