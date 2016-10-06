package ru.javawebinar.topjava;

import ru.javawebinar.topjava.matcher.ModelMatcher;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Objects;

import static ru.javawebinar.topjava.model.BaseEntity.START_SEQ;

/**
 * GKislin
 * 13.03.2015.
 */
public class MealTestData {

    private static final Meal MEAL_ADMIN_1 = new Meal(START_SEQ, LocalDateTime.of(2016, Month.OCTOBER, 30, 12, 50, 0), "description", 2000);
    private static final Meal MEAL_ADMIN_2 = new Meal(START_SEQ+1, LocalDateTime.of(2016, Month.OCTOBER, 30, 18, 0, 0), "description", 2000);
    private static final Meal MEAL_ADMIN_3 = new Meal(START_SEQ+2, LocalDateTime.of(2016, Month.OCTOBER, 30, 20, 0, 0), "description", 2000);
    private static final Meal MEAL_USER_1 = new Meal(START_SEQ+3, LocalDateTime.of(2016, Month.OCTOBER, 31, 11, 0, 0), "description", 2000);
    private static final Meal MEAL_USER_2 = new Meal(START_SEQ+4, LocalDateTime.of(2016, Month.OCTOBER, 31, 15, 30, 0), "description", 2000);
    private static final Meal MEAL_USER_3 = new Meal(START_SEQ+5, LocalDateTime.of(2016, Month.OCTOBER, 31, 21, 40, 0), "description", 2000);

    public static final ModelMatcher<Meal> MATCHER = new ModelMatcher<>(
            (expected, actual) -> expected == actual ||
                    (Objects.equals(expected.getDateTime(), actual.getDateTime())
                            && Objects.equals(expected.getId(), actual.getId())
                            && Objects.equals(expected.getDescription(), actual.getDescription())
                            && Objects.equals(expected.getCalories(), actual.getCalories())
                    )
    );

}
