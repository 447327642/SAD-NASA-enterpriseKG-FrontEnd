package utils;

import com.fasterxml.jackson.databind.JsonNode;

public class ClimateServiceImage {
    public static Boolean checkExist(JsonNode node){
        if (node == null || node.has("error")
                || !node.isArray() || node.path("imageUrl") == null)
            return true;
        return false;
    }
    
    public static Boolean checkScatterPlot(JsonNode node){
        if (node.path("name") != null && node.path("name").asText().equals("scatterPlot2Vars"))
            return true;
        return false;
    }
    
    public static Boolean checkTwoDimSlice3D(JsonNode node){
        if (node.path("name") != null && node.path("name").asText().equals("twoDimSlice3D"))
            return true;
        return false;
    }
}
