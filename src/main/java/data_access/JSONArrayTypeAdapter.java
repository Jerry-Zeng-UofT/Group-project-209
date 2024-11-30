package data_access;

import com.google.gson.*;
import org.json.JSONArray;

import java.lang.reflect.Type;

public class JSONArrayTypeAdapter implements JsonDeserializer<JSONArray>, JsonSerializer<JSONArray> {

    @Override
    public JsonElement serialize(JSONArray src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < src.length(); i++) {
            jsonArray.add(src.get(i).toString());
        }
        return jsonArray;
    }

    @Override
    public JSONArray deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JSONArray jsonArray = new JSONArray();
        for (JsonElement element : json.getAsJsonArray()) {
            jsonArray.put(element.getAsString());
        }
        return jsonArray;
    }
}