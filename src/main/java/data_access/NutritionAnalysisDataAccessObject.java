package data_access;

import entity.Nutrient;
import entity.Recipe;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NutritionAnalysisDataAccessObject implements NutritionAnalysisDataAccess{
    private static final String APP_ID = "6ec3d1f4";
    private static final String APP_KEY = "0bae38670d4f3777d686730b59f0e707";
    private static final String NA_URL = "https://api.edamam.com/api/nutrition-details";
    private static final String BASE_URL = "https://api.edamam.com/api/recipes/v2";
    private final OkHttpClient httpClient = new OkHttpClient();

    public NutritionAnalysisDataAccessObject() {
        // Empty constructor
    }

    @Override
    public List<Nutrient> analyzeNutrition(Recipe recipe) throws IOException {
        List<Nutrient> nutrientsList = new ArrayList<>();

        // Build the HTTP request
        Request request = buildNutritionRequest(recipe);

        // Process the response
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.body() != null) {
                nutrientsList = parseNutrientsResponse(response.body().string());
            }
        } catch (IOException e) {
            throw new IOException("Error processing nutrition analysis", e);
        }

        return nutrientsList;
    }

    private Request buildNutritionRequest(Recipe recipe) {
        HttpUrl urlBuilder = HttpUrl.parse(NA_URL).newBuilder()
                .addQueryParameter("app_id", APP_ID)
                .addQueryParameter("app_key", APP_KEY)
                .build();

        JSONObject requestBody = new JSONObject();
        requestBody.put("title", recipe.getTitle());
        requestBody.put("ingr", recipe.getJsonIngredient());

        return new Request.Builder()
                .url(urlBuilder)
                .post(RequestBody.create(
                        requestBody.toString(),
                        MediaType.get("application/json; charset=utf-8")
                ))
                .build();
    }

    private List<Nutrient> parseNutrientsResponse(String jsonData) {
        List<Nutrient> nutrientsList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONObject totalNutrients = jsonObject.getJSONObject("totalNutrients");

        for (String key : totalNutrients.keySet()) {
            JSONObject nutrient = totalNutrients.getJSONObject(key);
            String aNutrient = nutrient.getString("label")
                    + ": "
                    + nutrient.getInt("quantity")
                    + nutrient.getString("unit");

            nutrientsList.add(new Nutrient(aNutrient));
        }
        return nutrientsList;
    }
}
