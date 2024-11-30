package data_access;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import entity.MealPlanEntry;
import entity.Recipe;
import org.json.JSONArray;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.*;

public class MealPlanningDataAccessObject implements MealPlanningDataAccess {

    private final Map<Integer, MealPlanEntry> mealPlanEntries = new HashMap<>();
    private final String filePath = "meal_plan.json";
    private final SavedRecipesDataAccess savedRecipesDataAccess;
    private int nextEntryId = 1;

    public MealPlanningDataAccessObject(SavedRecipesDataAccess savedRecipesDataAccess) {
        this.savedRecipesDataAccess = savedRecipesDataAccess;
        loadFromJsonFile();
    }

    @Override
    public MealPlanEntry getMealPlanEntry(int userId, int entryId) {
        MealPlanEntry entry = mealPlanEntries.get(entryId);
        if (entry != null && entry.getUserId() == userId) {
            return entry;
        }
        return null;
    }

    @Override
    public void updateMealStatus(int userId, int entryId, String status) {
        MealPlanEntry entry = mealPlanEntries.get(entryId);
        if (entry != null && entry.getUserId() == userId) {
            entry.setStatus(status);
            saveToJsonFile();
        }
        else {
            throw new IllegalArgumentException("Entry not found or unauthorized access");
        }
    }

    @Override
    public void addMealPlanEntry(int userId, int recipeId, LocalDate date, String mealType) {
        Recipe recipe = savedRecipesDataAccess.getSavedRecipe(userId, recipeId);
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe not found in saved recipes");
        }

        MealPlanEntry entry = new MealPlanEntry(nextEntryId++, recipe, date, userId, mealType);
        mealPlanEntries.put(entry.getEntryId(), entry);
        saveToJsonFile();
    }

    @Override
    public void removeMealPlanEntry(int userId, int mealPlanEntryId) {
        MealPlanEntry entry = mealPlanEntries.get(mealPlanEntryId);
        if (entry != null && entry.getUserId() == userId) {
            mealPlanEntries.remove(mealPlanEntryId);
            saveToJsonFile();
        }
    }

    @Override
    public List<MealPlanEntry> getWeeklyPlan(int userId, LocalDate weekStart) {
        return mealPlanEntries.values().stream()
                .filter(entry -> entry.getUserId() == userId)
                .filter(entry -> {
                    LocalDate date = entry.getDate();
                    return !date.isBefore(weekStart) && date.isBefore(weekStart.plusDays(7));
                })
                .toList();
    }

    private void saveToJsonFile() {
        try (Writer writer = new FileWriter(filePath)) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(JSONArray.class, new JSONArrayTypeAdapter())
                    .create();
            gson.toJson(mealPlanEntries, writer);
        }
        catch (IOException e) {
            System.err.println("error");
        }
    }

    private void loadFromJsonFile() {
        try (Reader reader = new FileReader(filePath)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(JSONArray.class, new JSONArrayTypeAdapter())
                    .create();

            Type type = new TypeToken<Map<Integer, MealPlanEntry>>() {}.getType();
            Map<Integer, MealPlanEntry> loadedEntries = gson.fromJson(reader, type);

            if (loadedEntries != null) {
                mealPlanEntries.putAll(loadedEntries);
                nextEntryId = loadedEntries.keySet().stream().max(Integer::compareTo).orElse(0) + 1;
            }
        }
        catch (IOException e) {
            System.err.println("error");
        }
    }
}