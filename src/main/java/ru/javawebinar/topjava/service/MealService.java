package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

/**
 * GKislin
 * 15.06.2015.
 */
public interface MealService {

    void save(Meal meal, int userId);

    List<Meal> getAll(int userId) throws NotFoundException;

    List<Meal> getBetween(int userId, LocalDate startDate, LocalDate endDate) throws NotFoundException;

    void delete(int id, int userId) throws NotFoundException;

    Meal get(int id, int userId) throws NotFoundException;

}
