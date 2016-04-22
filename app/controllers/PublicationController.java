package controllers;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import utils.Constants;
import utils.JsonListBridge;
import utils.RESTfulCalls;
import utils.RESTfulCalls.ResponseType;
import views.html.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.Author;
import models.ClimateService;
import models.PublicationFigure;
import models.PublicationTopic;
import models.PublicationTopicKeyword;
import models.Publications;
import models.User;
import javax.inject.Singleton;
import utils.FormatCheck;
import utils.NodeConstructor;

import utils.RESTfulContext;
import utils.RESTfulGet;
import utils.RESTfulPost;
@Singleton
public class PublicationController extends Controller {

	final static Form<Publications> publicationForm = Form
			.form(Publications.class);
	final static Form<User> userForm = Form.form(User.class);
	//Strategy
	static RESTfulContext getAPIContext = new RESTfulContext(new RESTfulGet());
	static RESTfulContext postAPIContext = new RESTfulContext(new RESTfulPost());
	//Singleton
	private static PublicationController publicationController = new PublicationController();
	private PublicationController() {}
	public static PublicationController getInstance() {
		return publicationController;
	}

	public static Result publicationRegistration() {
		AbstractFactory publicationFactoryInterface = FactoryProducer.getFactory("PublicationControllerInterface");
		
		PublicationFactory publicationFactory = new PublicationFactory();

		PublicationControllerInterface publication1 = publicationFactory.getExecution("publicationRegistration");

		publication1.execute();

//		return ok(publicationRegistration.render(response));
		 return ok("{\"success\":\"success\"}");
		// return redirect(routes.publicationRegistration.execute());
	}



//	public static Result publicationRegistration() {
//		List<Author> authorsList = new ArrayList<Author>();
//		JsonNode authorsNode = RESTfulCalls.getAPI(Constants.URL_HOST
//				+ Constants.CMU_BACKEND_PORT
//				+ Constants.GET_ALL_AUTHORS);
//
//		// if no value is returned or error or is not json array
//		if (authorsNode == null || authorsNode.has("error")
//				|| !authorsNode.isArray()) {
//			return ok(registerAPublication.render(authorsList));
//		}
//
//		// parse the json string into object
//		for (int i = 0; i < authorsNode.size(); i++) {
//			JsonNode json = authorsNode.path(i);
//			Author oneAuthor = new Author();
//			oneAuthor.setId(json.findPath("id").asLong());
//			oneAuthor.setAuthorName(json.findPath("authorName").asText());
//			oneAuthor.setInstitute(json.findPath("institute").asText());
//			authorsList.add(oneAuthor);
//		}
//
//		return ok(registerAPublication.render(authorsList));
//	}
	
	public static Result showOverview() {
		List<Publications> mostRecentlyAddedPublications = new ArrayList<Publications>();
		List<Publications> mostRecentlyUsedPublications = new ArrayList<Publications>();
		List<Publications> mostPopularPublications = new ArrayList<Publications>();
		List<PublicationTopic> topics = new ArrayList<PublicationTopic>();
		
		JsonNode topicNode = RESTfulCalls.getAPI(Constants.URL_HOST
				+ Constants.CMU_BACKEND_PORT
				+ Constants.GET_ALL_PUBLICATION_TOPICS);

		JsonNode mostRecentlyAddedPublicationNode = RESTfulCalls.getAPI(Constants.URL_HOST
				+ Constants.CMU_BACKEND_PORT
				+ Constants.GET_MOST_RECENTLY_ADDED_PUBLICATIONS_CALL);
		JsonNode mostRecentlyUsedPublicationsNode = RESTfulCalls.getAPI(Constants.URL_HOST
				+ Constants.CMU_BACKEND_PORT
				+ Constants.GET_MOST_RECENTLY_USED_PUBLICATIONS_CALL);
		JsonNode mostPopularPublicationNode = RESTfulCalls.getAPI(Constants.URL_HOST
				+ Constants.CMU_BACKEND_PORT
				+ Constants.GET_MOST_POPULAR_PUBLICATIONS_CALL);

		// if no value is returned or error or is not json array
		if (mostRecentlyAddedPublicationNode == null || mostRecentlyAddedPublicationNode.has("error")
				|| !mostRecentlyAddedPublicationNode.isArray() ||
				mostRecentlyUsedPublicationsNode == null || mostRecentlyUsedPublicationsNode.has("error")
				|| !mostRecentlyUsedPublicationsNode.isArray() ||
				mostPopularPublicationNode == null || mostPopularPublicationNode.has("error")
				|| !mostPopularPublicationNode.isArray() ||
				topicNode == null || topicNode.has("error")
				|| !topicNode.isArray()) {
			return ok(overview.render(mostRecentlyAddedPublications, mostRecentlyUsedPublications, mostPopularPublications, topics));
		}

		// parse the json string into object
		for (int i = 0; i < mostRecentlyAddedPublicationNode.size(); i++) {
			JsonNode json = mostRecentlyAddedPublicationNode.path(i);
			Publications newPublication = deserializeJsonToPublication(json);
			String authorListAbbr = abbrAuthorList(newPublication.getAuthorList());
			newPublication.setAuthorList(authorListAbbr);
			mostRecentlyAddedPublications.add(newPublication);
		}

		/*
		// parse the json string into object
		for (int i = 0; i < mostRecentlyUsedPublicationsNode.size(); i++) {
			JsonNode json = mostRecentlyUsedPublicationsNode.path(i);
			Publications newPublication = deserializeJsonToPublication(json);
			mostRecentlyUsedPublications.add(newPublication);
		}*/
		JsonListBridge.j2l(mostRecentlyUsedPublicationsNode, mostRecentlyUsedPublications);

		/*
		// parse the json string into object
		for (int i = 0; i < mostPopularPublicationNode.size(); i++) {
			JsonNode json = mostPopularPublicationNode.path(i);
			Publications newPublication = deserializeJsonToPublication(json);
			mostPopularPublications.add(newPublication);
		}*/
		JsonListBridge.j2l(mostPopularPublicationNode, mostPopularPublications);

		
		// parse the json string into object
		for (int i = 0; i < topicNode.size(); i++) {
			JsonNode json = topicNode.path(i);
			PublicationTopic newTopic = new PublicationTopic();
			newTopic.setId(json.path("id").asLong());
			newTopic.setTopicName(json.path("topicName").asText());
			newTopic.setPublicationNum(json.path("publicationNum").asInt());
			topics.add(newTopic);
		}
		return ok(overview.render(mostRecentlyAddedPublications, mostRecentlyUsedPublications, mostPopularPublications, topics));

	}
	
	public static Result showAllPublications() {
		List<Publications> publicationList = new ArrayList<Publications>();
		//Facade by Shenggu
		//JsonNode publicationNode = NodeConstructor.ConstructJsonNodebyGet(Constants.GET_ALL_PUBLICATIONS);

		//Strategy by Haoyun
		JsonNode publicationNode = getAPIContext.executeStrategy(Constants.URL_HOST
				+ Constants.CMU_BACKEND_PORT
				+ Constants.GET_ALL_PUBLICATIONS, null);
		// if no value is returned or error or is not json array
		if (publicationNode == null || publicationNode.has("error")
				|| !publicationNode.isArray()) {
			return ok(allPublications.render(publicationList, publicationForm));
		}

		// parse the json string into object
		for (int i = 0; i < publicationNode.size(); i++) {
			JsonNode json = publicationNode.path(i);
			Publications onePublication = deserializeJsonToPublication(json);
			String authorListAbbr = abbrAuthorList(onePublication.getAuthorList());
			onePublication.setAuthorList(authorListAbbr);
			publicationList.add(onePublication);
		}

		return ok(allPublications.render(publicationList,
				publicationForm));
	}
	
	public static String abbrAuthorList(String oldAuthorList) {
		String[] parts = oldAuthorList.split(",");
		String authorListAbbr = parts[0];
		int j = 1;
		for (j = 1; j < 3 && j < parts.length; j++) {
			authorListAbbr  = authorListAbbr + ", " + parts[j];
		}
		if (j < parts.length)
			authorListAbbr += ", ...";
		return authorListAbbr;
	}

	// Xingyu Chen added
	public static Result mostRecentlyAddedPublications() {
		List<Publications> publications = new ArrayList<Publications>();
		JsonNode publicationNode =  NodeConstructor.ConstructJsonNodebyGet(Constants.GET_MOST_RECENTLY_ADDED_PUBLICATIONS_CALL);

		// if no value is returned or error or is not json array
		if (FormatCheck.check(publicationNode)) {
			return ok(mostRecentlyAddedPublications.render(publications));
		}

		/*
		// parse the json string into object
		for (int i = 0; i < publicationNode.size(); i++) {
			JsonNode json = publicationNode.path(i);
			Publications newPublication = deserializeJsonToPublication(json);
			publications.add(newPublication);
		}*/
		JsonListBridge.j2l(publicationNode, publications);

		return ok(mostRecentlyAddedPublications.render(publications));
	}
	
	public static Result mostRecentlyUsedPublications() {
		List<Publications> publications = new ArrayList<>();

		JsonNode publicationsNode = NodeConstructor.ConstructJsonNodebyGet(Constants.GET_MOST_RECENTLY_USED_PUBLICATIONS_CALL);

		// if no value is returned or error or is not json array
		if (FormatCheck.check(publicationsNode)) {
			return ok(mostRecentlyUsedPublications.render(publications));
		}

		/*
		// parse the json string into object
		for (int i = 0; i < publicationsNode.size(); i++) {
			JsonNode json = publicationsNode.path(i);
			Publications newPublication = deserializeJsonToPublication(json);
			publications.add(newPublication);
		}*/
		JsonListBridge.j2l(publicationsNode, publications);


		return ok(mostRecentlyUsedPublications.render(publications));
	}
	
	// Xingyu Chen added
	public static Result mostPopularPublications() {
		List<Publications> publications = new ArrayList<Publications>();
		JsonNode publicationNode = NodeConstructor.ConstructJsonNodebyGet(Constants.GET_MOST_POPULAR_PUBLICATIONS_CALL);

		// if no value is returned or error or is not json array
		if (FormatCheck.check(publicationNode)) {
			return ok(mostPopularPublications.render(publications));
		}

		/*
		// parse the json string into object
		for (int i = 0; i < publicationNode.size(); i++) {
			JsonNode json = publicationNode.path(i);
			Publications newPublication = deserializeJsonToPublication(json);
			publications.add(newPublication);
		}*/
		JsonListBridge.j2l(publicationNode, publications);

		return ok(mostPopularPublications.render(publications));
	}
	
	

    public static Result createNewPublication(){
    	MultipartFormData body = request().body().asMultipartFormData();
    	ObjectNode jsonData = Json.newObject();
    	try{
    		FilePart uploadFile = body.getFile("uploadFile");
    		String paperTitle = body.asFormUrlEncoded().get("paperTitle")[0];
    		String authorList = "";
    		String publicationChannel = "";
    		String year = body.asFormUrlEncoded().get("year")[0];
    		String fileID = "";
    		int hashCode = 0;
    		if (body.asFormUrlEncoded().get("authorList") != null) {
    			String[] parsedAuthorList = body.asFormUrlEncoded().get("authorList");
    			if (parsedAuthorList.length != 0) {
	    			authorList = parsedAuthorList[0];
	    			for (int i = 1; i < parsedAuthorList.length; i++) {
	    				authorList = authorList + "," + parsedAuthorList[i];
	    			}
    			}
    		}
    		if (body.asFormUrlEncoded().get("publicationChannel") != null) {
    			publicationChannel = body.asFormUrlEncoded().get("publicationChannel")[0];
    		}
    		if (uploadFile != null) {
        		String fileName = uploadFile.getFilename();
        		File file = uploadFile.getFile();

    			Date date = new Date();
    			hashCode = Math.abs(fileName.hashCode() + date.hashCode());
    			String hashFilePath = Constants.PUBLICATION_PATH + hashCode + ".pdf";
    			Path newPath = Paths.get(hashFilePath);
    			Files.move(file.toPath(), newPath, REPLACE_EXISTING);
    			fileID = hashFilePath;
    		}

			jsonData.put("paperTitle", paperTitle);
			jsonData.put("authorList", authorList);
			jsonData.put("publicationChannel", publicationChannel);
			jsonData.put("year", year);
			jsonData.put("fileID", fileID);
			jsonData.put("hashCode", hashCode);
			System.out.println(fileID);
			JsonNode response = RESTfulCalls.postAPI(Constants.URL_HOST + Constants.CMU_BACKEND_PORT
					+ Constants.ADD_PUBLICATION, jsonData);

			// flash the response message
			Application.flashMsg(response);
			return ok(createSuccess.render());

    	}catch (IllegalStateException e) {
			e.printStackTrace();
			Application.flashMsg(RESTfulCalls
					.createResponse(ResponseType.CONVERSIONERROR));
		} catch (Exception e) {
			e.printStackTrace();
			Application.flashMsg(RESTfulCalls
					.createResponse(ResponseType.UNKNOWN));
		}
    	List<Author> authorsList = new ArrayList<>();
		return ok(registerAPublication.render(authorsList));
    }
    
    /**
     * To do by Haoyun
     * @return
     */
    
    // Get all publications
 	public static Result searchPublications() {
 		return ok(searchPublication.render(publicationForm));
 	}


 	public static Result getSearchResult(){
 		Form<Publications> cs = publicationForm.bindFromRequest();
 		ObjectNode jsonData = Json.newObject();

 		String paperTitle = "";
 		String author = "";
 		String publicationChannel = "";
 		String year = "";

 		try {
 			paperTitle = cs.field("paperTitle").value();
 			author = cs.field("author").value();
 			publicationChannel = cs.field("publicationChannel").value();
 			year = cs.field("year").value();
 			//versionNo = cs.field("Version Number").value();

 		} catch (IllegalStateException e) {
 			e.printStackTrace();
 			Application.flashMsg(RESTfulCalls
 					.createResponse(ResponseType.CONVERSIONERROR));
 		} catch (Exception e) {
 			e.printStackTrace();
 			Application.flashMsg(RESTfulCalls.createResponse(ResponseType.UNKNOWN));
 		}
		// here to go
 		List<Publications> response = queryPublication(paperTitle, author, publicationChannel, year);
 		return ok(allPublications.render(response, publicationForm));
 	}

 	public static List<Publications> queryPublication(String paperTitle, String author, String publicationChannel, String year) {

 		List<Publications> publicationList = new ArrayList<>();
 		ObjectMapper mapper = new ObjectMapper();
 		ObjectNode queryJson = mapper.createObjectNode();
 		queryJson.put("paperTitle", paperTitle);
 		queryJson.put("author", author);
 		queryJson.put("publicationChannel", publicationChannel);
 		queryJson.put("year", year);

 		// JsonNode publicationNode = RESTfulCalls.postAPI(Constants.URL_HOST
 		// 		+ Constants.CMU_BACKEND_PORT + Constants.QUERY_PUBLICATION, queryJson);

 		//Strategy by Haoyun
 		JsonNode publicationNode = postAPIContext.executeStrategy(Constants.URL_HOST
 				+ Constants.CMU_BACKEND_PORT + Constants.QUERY_PUBLICATION, queryJson);
 		// parse the json string into object
 		for (int i = 0; i < publicationNode.size(); i++) {
 			JsonNode json = publicationNode.path(i);
 			Publications newPublication = deserializeJsonToPublication(json);
 			String authorListAbbr = abbrAuthorList(newPublication.getAuthorList());
 			newPublication.setAuthorList(authorListAbbr);
 			publicationList.add(newPublication);
 		}
 		return publicationList;
 	}
 	
 	public static Result showPublicationMetadata(int publicationId) {
		List<PublicationFigure> publicationMetadataList = new ArrayList<PublicationFigure>();
		JsonNode publicationNode = NodeConstructor.ConstructJsonNodebyGet(Constants.GET_PUBLICATION_METADATA_BY_PUBLICATION_ID + publicationId);

		// if no value is returned or error or is not json array
		if (FormatCheck.check(publicationNode)) {
			return ok(publicationMetadata.render(publicationMetadataList));
		}

		// parse the json string into object
		for (int i = 0; i < publicationNode.size(); i++) {
			JsonNode json = publicationNode.path(i);
			PublicationFigure newPublicationMetadata = deserializeJsonToPublicationFigure(json);
			publicationMetadataList.add(newPublicationMetadata);
		}

		return ok(publicationMetadata.render(publicationMetadataList));
	}
 	
 	public static Result showPublicationsByTopicId(int topicId) {
 		List<Publications> publications = new ArrayList<>();

		JsonNode publicationsNode = NodeConstructor.ConstructJsonNodebyGet(Constants.GET_ALL_PUBLICATIONS_BY_TOPIC_ID + topicId);

		// if no value is returned or error or is not json array
		if (FormatCheck.check(publicationsNode)) {
			return ok(allPublications.render(publications, publicationForm));
		}

		// parse the json string into object
		for (int i = 0; i < publicationsNode.size(); i++) {
			JsonNode json = publicationsNode.path(i);
			Publications newPublication = deserializeJsonToPublication(json);
			String authorListAbbr = abbrAuthorList(newPublication.getAuthorList());
			newPublication.setAuthorList(authorListAbbr);
			publications.add(newPublication);
		}

		return ok(allPublications.render(publications, publicationForm));
 	}
 	
 	
 	public static Result showAllPublicationTopics() {
 		List<PublicationTopic> topics = new ArrayList<PublicationTopic>();
		JsonNode topicNode = NodeConstructor.ConstructJsonNodebyGet(Constants.GET_ALL_PUBLICATION_TOPICS);

		// if no value is returned or error or is not json array
		if (FormatCheck.check(topicNode)) {
			return ok(allPublicationTopics.render(topics));
		}

		// parse the json string into object
		for (int i = 0; i < topicNode.size(); i++) {
			JsonNode json = topicNode.path(i);
			PublicationTopic newTopic = new PublicationTopic();
			newTopic.setId(json.path("id").asLong());
			newTopic.setTopicName(json.path("topicName").asText());
			newTopic.setPublicationNum(json.path("publicationNum").asInt());
			topics.add(newTopic);
		}

		return ok(allPublicationTopics.render(topics));
 	}
 	
 	// TODO: to be modified
 	public static Result showAllPublicationTopicKeywords() {
 		List<PublicationTopic> topics = new ArrayList<PublicationTopic>();
		JsonNode topicNode = NodeConstructor.ConstructJsonNodebyGet(Constants.GET_ALL_PUBLICATION_TOPICS);

		// if no value is returned or error or is not json array
		if (FormatCheck.check(topicNode)) {
			return ok(allPublicationTopics.render(topics));
		}

		// parse the json string into object
		for (int i = 0; i < topicNode.size(); i++) {
			JsonNode json = topicNode.path(i);
			PublicationTopic newTopic = new PublicationTopic();
			newTopic.setId(json.path("id").asLong());
			newTopic.setTopicName(json.path("topicName").asText());
			newTopic.setPublicationNum(json.path("publicationNum").asInt());
			topics.add(newTopic);
		}

		return ok(allPublicationTopics.render(topics));
 	}
 	
 	
 	public static Result showPublicationPanel(int publicationId) {
 		
 		List<PublicationFigure> publicationMetadataList = new ArrayList<PublicationFigure>();
		List<RestFulTask> restFulTasks = new ArrayList<>();
		restFulTasks.add(new RequestForMetadata(publicationId));
		restFulTasks.add(new RequestForPanelList(publicationId));

		for (RestFulTask task: restFulTasks) {
			task.excute();
		}

		JsonNode publicationMetadataNode = RESTfulCalls.getAPI(Constants.URL_HOST
				+ Constants.CMU_BACKEND_PORT
				+ Constants.GET_PUBLICATION_METADATA_BY_PUBLICATION_ID + publicationId);

		List<Publications> publicationPanelList = new ArrayList<Publications>();
		JsonNode publicationPanelNode = RESTfulCalls.getAPI(Constants.URL_HOST
				+ Constants.CMU_BACKEND_PORT
				+ Constants.GET_PUBLICATION_PANEL_BY_PUBLICATION_ID + publicationId);
		
		List<PublicationTopicKeyword> publicationTopicKeywordList = new ArrayList<>();
		JsonNode publicationTopicKeywordNode = RESTfulCalls.getAPI(Constants.URL_HOST
				+ Constants.CMU_BACKEND_PORT
				+ Constants.GET_PUBLICATION_TOPIC_KEYWORD_BY_PUBLICATION_ID + publicationId);
		
		JsonNode publicationTopicNode = RESTfulCalls.getAPI(Constants.URL_HOST
				+ Constants.CMU_BACKEND_PORT
				+ Constants.GET_TOPIC_BY_PUBLICATION_ID + publicationId);
		// if no value is returned or error or is not json array
		if (publicationMetadataNode == null || publicationMetadataNode.has("error") || !publicationMetadataNode.isArray() 
				|| publicationPanelNode == null || publicationPanelNode.has("error") || !publicationPanelNode.isArray() 
				|| publicationTopicKeywordNode == null || publicationTopicKeywordNode.has("error") || !publicationTopicKeywordNode.isArray()
				|| publicationTopicNode == null || publicationTopicNode.has("error")) {
			return ok(publicationPanel.render(publicationPanelList, publicationMetadataList, "", ""));
		}

		// parse the json string into object
		for (int i = 0; i < publicationPanelNode.size(); i++) {
			JsonNode json = publicationPanelNode.path(i);
			Publications newPublicationPanel = deserializeJsonToPublication(json);
			publicationPanelList.add(newPublicationPanel);
		}
		
		// parse the json string into object
		for (int i = 0; i < publicationMetadataNode.size(); i++) {
			JsonNode json = publicationMetadataNode.path(i);
			PublicationFigure newPublicationMetadata = deserializeJsonToPublicationFigure(json);
			publicationMetadataList.add(newPublicationMetadata);
		}
		
		// parse the json string into object
		for (int i = 0; i < publicationTopicKeywordNode.size(); i++) {
			JsonNode json = publicationTopicKeywordNode.path(i);
			PublicationTopicKeyword newPublicationTopicKeyword = deserializeJsonToPublicationTopicKeyword(json);
			publicationTopicKeywordList.add(newPublicationTopicKeyword);
		}
		
		Collections.sort(publicationTopicKeywordList, new Comparator<PublicationTopicKeyword>() {
			@Override
			public int compare(PublicationTopicKeyword k1, PublicationTopicKeyword k2) {
				return k2.getCount() - k1.getCount();
			}
		});
		
		StringBuilder topicKeywords = new StringBuilder();
		for(PublicationTopicKeyword topicKeyword : publicationTopicKeywordList) {
			topicKeywords.append(topicKeyword.getTopicKeywordName()+", ");
		}
		if(topicKeywords.length() != 0) topicKeywords.delete(topicKeywords.length()-2, topicKeywords.length());
		// Parse Topic
		String topic = publicationTopicNode.path("topicName").asText();
		return ok(publicationPanel.render(publicationPanelList, publicationMetadataList, topicKeywords.toString(), topic));
	}
    
 	public static PublicationTopicKeyword deserializeJsonToPublicationTopicKeyword(JsonNode json) {
 		PublicationTopicKeyword oneTopicKeyword = new PublicationTopicKeyword();
 		oneTopicKeyword.setId(json.path("id").asLong());
 		oneTopicKeyword.setTopicKeywordName(json.path("topicKeywordName").asText());
 		oneTopicKeyword.setCount(json.path("count").asInt());
 		return oneTopicKeyword;
 	}
    public static Publications deserializeJsonToPublication(JsonNode json) {
		Publications onePublication = new Publications();
		onePublication.setId(json.path("id").asLong());
		onePublication.setPaperTitle(json.path("paperTitle").asText());
		onePublication.setPublicationChannel(json.path("publicationChannel").asText());
		onePublication.setYear(json.path("year").asInt());
		onePublication.setAuthorList(json.path("authorList").asText());
		onePublication.setFileID(json.path("fileID").asText());
		onePublication.setDataset(json.path("dataset").asText());
		onePublication.setInstrument(json.path("instrument").asText());
		onePublication.setVariable(json.path("variable").asText());
		return onePublication;
	}
    
    public static PublicationFigure deserializeJsonToPublicationFigure(JsonNode json) {
    	PublicationFigure publicationMetadata = new PublicationFigure();
    	publicationMetadata.setId(json.path("id").asLong());
    	publicationMetadata.setPath(json.path("path").asText());
    	publicationMetadata.setCaption(json.path("caption").asText());
		return publicationMetadata;
	}
}
