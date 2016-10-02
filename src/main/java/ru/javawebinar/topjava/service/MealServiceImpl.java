package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.mock.MealDaoImpl;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;

/**
 * Created by yulia on 02.10.16.
 */
public class MealServiceImpl implements MealService {

    private MealDao mealDao;

    public MealServiceImpl() {
        this.mealDao = new MealDaoImpl();
    }

    @Override
    public Meal save(Meal meal) {
        return this.mealDao.saveOrUpdate(meal);
    }

    @Override
    public void update(Meal meal) {
        this.mealDao.saveOrUpdate(meal);
    }

    @Override
    public void delete(Integer id) {
        this.mealDao.delete(id);
    }

    @Override
    public Meal findById(Integer id) {
        return this.mealDao.findById(id);
    }

    @Override
    public List<Meal> findAll() {
        return this.mealDao.findAll();
    }
}
