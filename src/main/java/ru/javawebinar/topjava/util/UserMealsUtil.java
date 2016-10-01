package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 700),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        List<UserMealWithExceed> filteredUserMeals = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);

        filteredUserMeals.stream().forEach(System.out::println);

    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesGroupByDate = mealList.stream().
                collect(Collectors.groupingBy(m -> m.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));
        return mealList.stream().filter(m -> TimeUtil.isBetween(m.getDateTime().toLocalTime(), startTime, endTime))
                .map(m -> new UserMealWithExceed(m.getDateTime(), m.getDescription(), m.getCalories(),
                        caloriesGroupByDate.get(m.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExceed>  getFilteredWithExceededLoop(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesGroupByDate = summingCaloriesByDate(mealList);

        List<UserMealWithExceed> filteredUserMealWithExceed = new LinkedList<>();
        for(UserMeal um: mealList){
            if(TimeUtil.isBetween(um.getDateTime().toLocalTime(), startTime, endTime)){
                filteredUserMealWithExceed.add(new UserMealWithExceed(um.getDateTime(),um.getDescription(),um.getCalories(),
                        caloriesGroupByDate.get(um.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }

        return filteredUserMealWithExceed;
    }

    private static Map<LocalDate, Integer> summingCaloriesByDate(List<UserMeal> mealList) {
        Map<LocalDate, Integer> caloriesGroupByDate = new TreeMap<>();
        for(UserMeal um: mealList){
            LocalDate mealDate = um.getDateTime().toLocalDate();
            caloriesGroupByDate.put(um.getDateTime().toLocalDate(), caloriesGroupByDate.getOrDefault(mealDate,0) + um.getCalories());
        }
        return  caloriesGroupByDate;
    }
}
