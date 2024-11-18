package entity;

import java.util.List;

/**
 * The shoppingList of user.
 */
public class ShoppingList {

    private int listId;
    private int userId;
    private List<String> items;
    private List<Integer> foodIds;
    private String status;

    public ShoppingList(int listId, int userId, List<String> items, List<Integer> foodIds, String status) {
        this.listId = listId;
        this.userId = userId;
        this.items = items;
        this.foodIds = foodIds;
        this.status = status;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public List<Integer> getFoodIds() {
        return foodIds;
    }

    public void setFoodIds(List<Integer> foodIds) {
        this.foodIds = foodIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

