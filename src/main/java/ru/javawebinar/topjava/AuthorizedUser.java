package ru.javawebinar.topjava;

import ru.javawebinar.topjava.util.MealsUtil;

/**
 * GKislin
 * 06.03.2015.
 */
public class AuthorizedUser {

    private static int id = MealsUtil.ADMIN_ID;

    private static int caloriesPerDay = MealsUtil.DEFAULT_CALORIES_PER_DAY;

    public static void setId(int userId){
        id = userId;
    }

    public static int id() {
        return id;
    }

    public static int getCaloriesPerDay() {
        return caloriesPerDay;
    }
}
