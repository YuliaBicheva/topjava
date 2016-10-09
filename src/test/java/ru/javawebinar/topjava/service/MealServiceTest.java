package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.TimeUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class MealServiceTest {

    @Autowired
    protected MealService mealService;

    @Autowired
    protected DbPopulator dbPopulator;

    @Before
    public void setUp(){
        dbPopulator.execute();
    }

    @Test
    public void testSave(){
        Meal newMeal = new Meal(LocalDateTime.of(2016, Month.OCTOBER, 30, 12, 50, 0), "description", 2000);
        Meal create = mealService.save(newMeal, UserTestData.ADMIN_ID);
        newMeal.setId(create.getId());
        List<Meal> expected = new ArrayList<>(ADMIN_MEALS_LIST);
        expected.add(newMeal);
        Collections.sort(expected);
        MATCHER.assertCollectionEquals(expected, mealService.getAll(UserTestData.ADMIN_ID));
    }

    @Test
    public void testUpdate(){
        Meal meal = new Meal(MEAL_USER_1);
        meal.setDateTime(LocalDateTime.of(2016, Month.JANUARY, 1, 0, 0, 0));
        mealService.update(meal, UserTestData.USER_ID);
        MATCHER.assertEquals(meal, mealService.get(meal.getId(),UserTestData.USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundUpdate(){
        Meal meal = new Meal(MEAL_USER_1);
        mealService.update(meal, UserTestData.ADMIN_ID);
    }

    @Test
    public void testDelete(){
        mealService.delete(MEAL_USER_1.getId(), UserTestData.USER_ID);
        List<Meal> expected = new ArrayList<>(USER_MEALS_LIST);
        expected.remove(MEAL_USER_1);
        Collections.sort(expected);
        MATCHER.assertCollectionEquals(expected, mealService.getAll(UserTestData.USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundDelete(){
        mealService.delete(MEAL_USER_1.getId(), UserTestData.ADMIN_ID);
    }

    @Test
    public void testGet(){
        MATCHER.assertEquals(MEAL_ADMIN_2, mealService.get(MEAL_ADMIN_2.getId(), UserTestData.ADMIN_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundGet(){
        mealService.get(MEAL_ADMIN_2.getId(), UserTestData.USER_ID);
    }

    @Test
    public void testGetBeetweenDateTime(){
        LocalDateTime startDateTime = LocalDateTime.of(2016, Month.OCTOBER, 30, 10, 00);
        LocalDateTime endDateTime = LocalDateTime.of(2016, Month.OCTOBER, 30, 19, 0);

        MATCHER.assertCollectionEquals(
                USER_MEALS_LIST.stream().filter(m -> TimeUtil.isBetween(
                        m.getDateTime(),startDateTime, endDateTime)).collect(Collectors.toList()),
                mealService.getBetweenDateTimes(startDateTime, endDateTime, UserTestData.USER_ID)
        );

    }

    @Test
    public void testGetBeetweenDate(){
        LocalDate startDate = LocalDate.of(2016, Month.OCTOBER, 30);
        LocalDate endDate = LocalDate.of(2016, Month.OCTOBER, 31);

        MATCHER.assertCollectionEquals(
                ADMIN_MEALS_LIST.stream().filter(m -> TimeUtil.isBetween(
                        m.getDate(),startDate, endDate)).collect(Collectors.toList()),
                mealService.getBetweenDates(startDate, endDate, UserTestData.ADMIN_ID)
        );

    }
}