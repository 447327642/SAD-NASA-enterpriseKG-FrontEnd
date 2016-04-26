/**
 * @author xingyuchen
 * Created on Apr 19, 2016
 */
package utils;

import com.fasterxml.jackson.databind.JsonNode;
import models.PublicationFigure;
import models.Publications;

/**
 * @author xingyuchen
 *
 */
public class PublicationFigureParser implements JsonParser {

	@Override
	public JsonNode toJson(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PublicationFigure toObject(JsonNode json) {
		PublicationFigure publicationMetadata = new PublicationFigure();
		publicationMetadata.setId(json.path("id").asLong());
		publicationMetadata.setPath(json.path("path").asText());
		publicationMetadata.setCaption(json.path("caption").asText());
		return publicationMetadata;
	}

}
