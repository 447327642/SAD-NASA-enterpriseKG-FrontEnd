/**
 * @author xingyuchen
 * Created on Apr 19, 2016
 */
package utils;

import com.fasterxml.jackson.databind.JsonNode;

import models.Publications;

/**
 * @author xingyuchen
 *
 */
public class PublicationParser implements JsonParser {

	@Override
	public Publications toObject(JsonNode json) {
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

}
