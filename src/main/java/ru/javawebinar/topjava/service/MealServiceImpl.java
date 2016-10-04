package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.ExceptionUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

/**
 * GKislin
 * 06.03.2015.
 */
@Service
public class MealServiceImpl implements MealService {

    private static final Logger LOG = LoggerFactory.getLogger(MealServiceImpl.class);

    @Autowired
    private MealRepository repository;

    @Override
    public void save(Meal meal, int userId) {
        LOG.debug("Save meal {} for userId {}", meal, userId);
        repository.save(meal, userId);
    }

    @Override
    public List<Meal> getAll(int userId) throws NotFoundException {
        LOG.debug("getAll() by userId {}", userId);
        return repository.getAll(userId);
    }

    @Override
    public List<Meal> getBetween(int userId, LocalDate startDate, LocalDate endDate) throws NotFoundException {
        LOG.debug("getBeetwenl() by userId {} beetwen {} and {}", userId, startDate, endDate);
        return repository.getBetween(userId, startDate, endDate);
    }

    @Override
    public void delete(int id, int userId) throws NotFoundException {
        LOG.debug("Delete meal with id {} by userId {}", id, userId);
        ExceptionUtil.checkNotFound(repository.delete(id, userId), id + " for user with userId " + userId);
    }

    @Override
    public Meal get(int id, int userId) throws NotFoundException {
        LOG.debug("Get by id {} by userId {}", id, userId);
        return ExceptionUtil.checkNotFound(repository.get(id, userId), id + " for user with userId " + userId);
    }

}
