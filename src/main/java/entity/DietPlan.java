package entity;

import java.util.Date;
import java.util.List;

/**
 * The representation of user's dietary plan.
 */
public class DietPlan {

    private int planId;
    private int userId;
    private Date calendarDate;
    private List<Integer> recipeIds;
    private String goals;

    public DietPlan(int planId, int userId, Date calendarDate, List<Integer> recipeIds, String goals) {
        this.planId = planId;
        this.userId = userId;
        this.calendarDate = calendarDate;
        this.recipeIds = recipeIds;
        this.goals = goals;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getCalendarDate() {
        return calendarDate;
    }

    public void setCalendarDate(Date calendarDate) {
        this.calendarDate = calendarDate;
    }

    public List<Integer> getRecipeIds() {
        return recipeIds;
    }

    public void setRecipeIds(List<Integer> recipeIds) {
        this.recipeIds = recipeIds;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }
}

