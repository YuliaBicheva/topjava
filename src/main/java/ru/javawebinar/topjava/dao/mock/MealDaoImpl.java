package ru.javawebinar.topjava.dao.mock;

import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yulia on 02.10.16.
 */
public class MealDaoImpl implements MealDao{

    private static final AtomicInteger idx = new AtomicInteger(0);

    private static final ConcurrentMap<Integer, Meal> meals = new ConcurrentHashMap<>();

    {
        saveOrUpdate(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        saveOrUpdate(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        saveOrUpdate(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        saveOrUpdate(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        saveOrUpdate(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        saveOrUpdate(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
    }

    @Override
    public Meal saveOrUpdate(Meal meal) {
        if(meal.getId() == null){
            Integer id = idx.incrementAndGet();
            meal.setId(id);
        }
        meals.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public boolean delete(Integer id) {
        return meals.remove(id) != null;
    }

    @Override
    public Meal findById(Integer id) {
        return meals.get(id);
    }

    @Override
    public List<Meal> findAll() {
        return new ArrayList<>(meals.values());
    }
}
