package utils;

import models.Publications;

import java.util.List;

import static controllers.PublicationController.deserializeJsonToPublication;

/**
 * Created by Weiyi Wang on 4/18/2016.
 * andrew id: weiyiw
 * mail: weiyiw@andrew.cmu.edu
 */
public class JsonListBridge {
    public static void j2l(JsonNode in, List<Publications> out) {
        for (int i = 0; i < in.size(); i++) {
            JsonNode json = in.path(i);
            Publications newPublication = deserializeJsonToPublication(json);
            out.add(newPublication);
        }
    }
}
