package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import utils.RESTfulCalls.ResponseType;

public class RESTfulPost implements Strategy{

	@Override
	public JsonNode doOperation(String apiString, JsonNode jsonData) {
		// TODO Auto-generated method stub
		System.out.println("apiString: " + apiString);
		System.out.println("jsonData: " + jsonData.get("purpose"));
		Promise<WSResponse> responsePromise = WS.url(apiString).post(jsonData);
		final Promise<JsonNode> bodyPromise = responsePromise.map(new Function<WSResponse, JsonNode>() {
					public JsonNode apply(WSResponse response)
							throws Throwable {
						if ((response.getStatus() == 201 || response
								.getStatus() == 200)) {
							try {
								return response.asJson();
							}
							catch (Exception e){
								//If response is in Json format, return as json, otherwise just plain success
								return createResponse(ResponseType.SUCCESS);
							}
						} else { // other response status from the server
							return createResponse(ResponseType.SAVEERROR);
						}
					}
				});
		try {
			return bodyPromise.get(10000L);
		} catch (Exception e) {
			return createResponse(ResponseType.TIMEOUT);
		}
	}
	
	public JsonNode createResponse(ResponseType type) {
		ObjectNode jsonData = Json.newObject();
		switch (type) {
			case SUCCESS:
				jsonData.put("success", "Success!");
				break;
			case GETERROR:
				jsonData.put("error", "Cannot get data from server");
				break;
			case SAVEERROR:
				jsonData.put("error", "Cannot be saved. The data must be invalid!");
				break;
			case DELETEERROR:
				jsonData.put("error", "Cannot be deleted on server");
				break;
			case RESOLVEERROR:
				jsonData.put("error", "Cannot be resolved on server");
				break;
			case TIMEOUT:
				jsonData.put("error", "No response/Timeout from server");
				break;
			case CONVERSIONERROR:
				jsonData.put("error", "Conversion error");
				break;
			default:
				jsonData.put("error", "Unknown errors");
				break;
		}
		return jsonData;
	}
}
